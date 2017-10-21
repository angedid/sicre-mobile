package cm.mindef.sed.sicre.mobile.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.RecorderAudioActivity;
import cm.mindef.sed.sicre.mobile.RecorderVideoActivity;
import cm.mindef.sed.sicre.mobile.VideoPlayerActivity;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionSoundAdapter;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionVideoAdapter;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static cm.mindef.sed.sicre.mobile.R.id.sound_list_view;
import static cm.mindef.sed.sicre.mobile.RecorderAudioActivity.RequestPermissionCode;
import static cm.mindef.sed.sicre.mobile.utils.Constant.REQUEST_VIDEO_CAPTURE;

/**
 * Created by root on 15/10/17.
 */

public class VideoFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{

    private  View rootView;
    private Perquisition perquisition;
    private ListView video_list_view;
    private Button add_preuve_video;
    private PerquisitionVideoAdapter perquisitionVideoAdapter;

    private boolean isViewShown = false;
    private Uri videoUri;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        rootView = view;

        if (!isViewShown) {
            fetchData();
        }

        return view;
    }


    private void reccordVideo() {

        dispatchTakeVideoIntent();

        /*Intent intent = new Intent(getActivity().getApplicationContext(), RecorderVideoActivity.class);
        intent.putExtra(Constant.PERQUISITION, perquisition);
        startActivity(intent);*/
    }


    private List<String> getSavedVideo(String perquisitionId) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir(Constant.PERQUISITION_VIDEO_ROOT + perquisitionId, Context.MODE_PRIVATE);
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


        }
        else if (isVisibleToUser == false) {  }


    }

    public void fetchData(){
        TextView affaire = rootView.findViewById(R.id.affaire);

        Bundle bundle = getActivity().getIntent().getExtras();
        perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
        //Log.e("PERQUISITION", perquisition.toString());
        affaire.setText(perquisition.getAffaire());

        video_list_view = rootView.findViewById(R.id.video_list_view);
        //local_image_list_view = rootView.findViewById(R.id.local_image_list_view);
        //Log.e("SIZEEEEEEE", "" + perquisition.getTextLinks().size());
        List<String> localVideo = getSavedVideo(perquisition.getId());
        for (String s : localVideo) perquisition.getVideoLinks().add(s);

        perquisitionVideoAdapter = new PerquisitionVideoAdapter(getActivity().getApplicationContext(), perquisition.getVideoLinks());
        Log.e("Video Links", perquisition.getVideoLinks().toString());
        video_list_view.setAdapter(perquisitionVideoAdapter);

        video_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri video = Uri.parse(perquisition.getVideoLinks().get(position));
                Intent intent = new Intent(getActivity().getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra(Constant.VIDEO_LINK, perquisition.getVideoLinks().get(position));
                startActivity(intent);
            }
        });

        add_preuve_video = rootView.findViewById(R.id.add_preuve_video);
        add_preuve_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()){
                    reccordVideo();
                }else {
                    requestPermission();
                }

            }
        });
    }




    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null ) {
            if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }else {
                Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.no_camera_feature), Toast.LENGTH_LONG).show();
            }

        }
    }



    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        byteBuffer.close();
        return byteBuffer.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
             videoUri = intent.getData();
            try {
                InputStream iStream =   getActivity().getContentResolver().openInputStream(videoUri);
                byte[] bytes = getBytes(iStream);

                ContextWrapper cw = new ContextWrapper(getActivity());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir(Constant.PERQUISITION_VIDEO_ROOT + perquisition.getId(), Context.MODE_PRIVATE);
                // Create imageDir

                File[] files = directory.listFiles();

                Arrays.sort(files);

                int surfix = (int) (System.currentTimeMillis()/1000);
                String fileName = "video_";

                if (files.length == 0){
                    fileName += surfix;
                }else {
                    String lastFileName = files[files.length-1].getName();
                    int lastNum = Integer.parseInt(lastFileName.split("_")[1].split("\\.")[0]) + 1;
                    fileName += "" + lastNum;
                    Toast.makeText(getActivity().getApplicationContext(), "lastNum: " + lastNum, Toast.LENGTH_LONG).show();
                }

                File mypath = new File(directory,  fileName + ".mp4");

                OutputStream outputStream = new FileOutputStream(mypath);
                outputStream.write(bytes);

                outputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("URIIIIIII", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("URIIIIIII getBytes", e.getMessage());
            }
        }
    }



    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{CAMERA}, Constant.RequestPermissionCode);

    }

    public boolean checkPermission() {
        //int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
        // WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED ;
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            reccordVideo();
        }


    }
}
