package cm.mindef.sed.sicre.mobile.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.RecorderVideoActivity;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionImageAdapter;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.utils.Constant;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.CAMERA_SERVICE;
import static cm.mindef.sed.sicre.mobile.RecorderAudioActivity.RequestPermissionCode;

/**
 * Created by root on 15/10/17.
 */

public class ImageFragment extends Fragment{

    private View rootView;
    private Perquisition perquisition;
    private  ListView image_list_view;
    private PerquisitionImageAdapter perquisitionImageAdapter;
    private Button add_preuve_img;
    private boolean isViewShown = false;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        rootView = view;

        if (!isViewShown) {
            fetchData();
        }
        return view;
    }

    public boolean checkPermission() {
        //int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
               // WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{CAMERA}, RequestPermissionCode);
    }

    public void getPicture(View view){

        if (checkPermission()){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, Constant.REQUEST_CODE_FOR_ADD_IMG_PERQUISITION);
        }else {
            requestPermission();
        }


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser == true) {


            if (getView() != null) {

                isViewShown = true;
                fetchData();

            }else {
                isViewShown = false;
            }

        }else{

        }

    }

    private void fetchData() {

        TextView affaire = rootView.findViewById(R.id.affaire);

        Bundle bundle = getActivity().getIntent().getExtras();
        perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
        //Log.e("PERQUISITION", perquisition.toString());
        affaire.setText(perquisition.getAffaire());

        image_list_view = rootView.findViewById(R.id.image_list_view);
        //local_image_list_view = rootView.findViewById(R.id.local_image_list_view);
        //Log.e("SIZEEEEEEE", "" + perquisition.getTextLinks().size());
        List<String> locatImages = getSavedImages(perquisition.getId());
        for (String s : locatImages) perquisition.getImageLinks().add(s);
        perquisitionImageAdapter = new PerquisitionImageAdapter(getActivity().getApplicationContext(), perquisition.getImageLinks());
        image_list_view.setAdapter(perquisitionImageAdapter);

        // perquisitionLocalImageAdapter = new PerquisitionImageAdapter(getActivity().getApplicationContext(), getSavedImages(perquisition.getId()));
        //local_image_list_view.setAdapter(perquisitionLocalImageAdapter);


        add_preuve_img = rootView.findViewById(R.id.add_preuve_img);
        add_preuve_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture(v);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CODE_FOR_ADD_IMG_PERQUISITION && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            saveToInternalStorage(imageBitmap, perquisition.getId());
        }
    }

    /**
     * Ecriture / sauvegarde dans le systeme de fichier local
     * @param bitmapImage
     * @param perquisitionId
     * @return
     */
    private String saveToInternalStorage(Bitmap bitmapImage, String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Constant.PERQUISITION_IMAGE_ROOT + perquisitionId, Context.MODE_PRIVATE);
        // Create imageDir

        File[] files = directory.listFiles();

        Arrays.sort(files);

        int surfix = (int) (System.currentTimeMillis()/1000);
        String fileName = "image_";

        if (files.length == 0){
            fileName += surfix;
        }else {
            String lastFileName = files[files.length-1].getName();
            int lastNum = Integer.parseInt(lastFileName.split("_")[1].split("\\.")[0]) + 1;
            fileName += "" + lastNum;
            Toast.makeText(getActivity().getApplicationContext(), "lastNum: " + lastNum, Toast.LENGTH_LONG).show();
        }


        Toast.makeText(getActivity().getApplicationContext(), "fileName: " + fileName, Toast.LENGTH_LONG).show();

        File mypath=new File(directory,  fileName + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> locatImages = getSavedImages(perquisition.getId());
        for (String s : locatImages) perquisition.getImageLinks().add(s);
        perquisitionImageAdapter = new PerquisitionImageAdapter(getActivity().getApplicationContext(), perquisition.getImageLinks());
        image_list_view.setAdapter(perquisitionImageAdapter);

        return directory.getAbsolutePath();
    }

    /**
     * Lecture des fichiers enregistres
     * @param perquisitionId
     * @return
     */

    public List<String> getSavedImages(String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir(Constant.PERQUISITION_IMAGE_ROOT + perquisitionId, Context.MODE_PRIVATE);
        // Create imageDir
        File[] files = directory.listFiles();

        Arrays.sort(files);

        List<String> retVal = new ArrayList<>();
        for (int i=0; i<files.length; i++){
            String dirs[] = files[i].getAbsolutePath().split("\\/");
            String lastDir = dirs[dirs.length-2];

            String vet [] = lastDir.split("_");
            String id = vet[vet.length-1];
            if (id.equals(perquisitionId))
                retVal.add("file://" + files[i].getAbsolutePath());

        }

        return  retVal;

    }
}
