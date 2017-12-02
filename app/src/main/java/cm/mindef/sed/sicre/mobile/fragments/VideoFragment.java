package cm.mindef.sed.sicre.mobile.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cm.mindef.sed.sicre.mobile.PerquisitionActivity;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.VideoPlayerActivity;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionVideoAdapter;
import cm.mindef.sed.sicre.mobile.db.PreuvesDataSources;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.domain.Preuve;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.oservable.event.RessourceCreated;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;
import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;
import static cm.mindef.sed.sicre.mobile.utils.Constant.REQUEST_VIDEO_CAPTURE;

/**
 * Created by root on 15/10/17.
 */

public class VideoFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback{

    private  View rootView;
    private Perquisition perquisition;
    private ListView video_list_view;
    private FloatingActionButton add_preuve_video;
    private PerquisitionVideoAdapter perquisitionVideoAdapter;

    private SwipeRefreshLayout swipeContainer;

    private boolean isViewShown = false;
    private Uri videoUri;
    private PerquisitionActivity perquisitionActivity;

    private FloatingActionButton save;
    private ProgressBar progress_save;


    private RequestQueue queue;
    private Button btn_refresh;

    //private StringRequest stringRequest;
    private JsonObjectRequest jsonObjectRequest;
    private JSONObject jsonObject;
    private String url;


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        rootView = view;

        save = rootView.findViewById(R.id.save);
        save.setVisibility(View.GONE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAll(v);
            }
        });
        progress_save = rootView.findViewById(R.id.progress_save);
        progress_save.setVisibility(View.GONE);

        perquisitionActivity = (PerquisitionActivity) getActivity();

        if (!isViewShown) {
            queue = MySingleton.getRequestQueue(this.getActivity().getApplicationContext());
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

        fetchData();
        PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
        preuvesDataSources.open();
        List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
        preuvesDataSources.close();

        if (preuveList.size() > 0){
            //PerquisitionActivity perquisitionActivity = (PerquisitionActivity) getActivity();
            perquisitionActivity.loadPerquisitionData();
        }
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
        affaire.setText(perquisition.getDescription());



        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {


                Credentials credentials =  Credentials.getInstance(getActivity().getApplicationContext());

                url = "http://198.50.199.116:8090/scriptcase/app/SICRE_2/m_perquisition_search_by_id/?" + Constant.USERNAME + "="+credentials.getUsername()+
                        "&" + Constant.PASSWORD + "=" + credentials.getPassword() + "&" + Constant.ID + "=" + perquisition.getId();

                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonObject = response;

                        perquisition = Perquisition.getInstance(response);
                        getActivity().getIntent().putExtra(Constant.PERQUISITION, perquisition);
                        fetchData();
                        swipeContainer.setRefreshing(false);

                    }
                },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                //Toast.makeText(getActivity().getApplication(), "Error..." + error.toString(), Toast.LENGTH_LONG).show();
                                Log.e("ERRORER 22", error.getCause() + " | " + error.getStackTrace() + " | " + error.getMessage() + " | " + error.toString());


                                swipeContainer.setRefreshing(false);

                                //loadPerquisition();

                            }
                        }
                );

                jsonObjectRequest.setTag(Constant.PERQUISITION_LIST_REQUEST_TAG);

// Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }

        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

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
        showSaveButton();
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


                String path =  directory.getAbsolutePath() + "/" + mypath.getName();

                PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
                preuvesDataSources.open();

                Preuve preuve = preuvesDataSources.createPreuve(path, Constant.latitudeNetwork, Constant.longitudeNetwork, "" + perquisition.getId(), "video");

                preuvesDataSources.close();

                OutputStream outputStream = new FileOutputStream(mypath);
                outputStream.write(bytes);

               // PerquisitionActivity.publisher.mySubscribe(new RessourceCreated(mypath.getAbsolutePath(), "video", getActivity().getApplicationContext(), perquisition));

                outputStream.close();

                showSaveButton();

                List<String> locatVideo = getSavedVideo(perquisition.getId());
                for (String s : locatVideo) perquisition.getVideoLinks().add(s);
                perquisitionVideoAdapter = new PerquisitionVideoAdapter(getActivity().getApplicationContext(), perquisition.getVideoLinks());
                video_list_view.setAdapter(perquisitionVideoAdapter);


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


    private void showSaveButton() {

        PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
        preuvesDataSources.open();

        List<Preuve> preuves = preuvesDataSources.getAllPreuves(perquisition.getId());
        if (preuves.size() > 0){
            if (save.getVisibility() == View.GONE){
                save.setVisibility(View.VISIBLE);
            }
        }

        preuvesDataSources.close();
    }

    /*@Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults){
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            reccordVideo();
        }


    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("onRequestPermissResult", "Request code: " + requestCode + "  permission: " + permissions );
        switch (requestCode) {
            case Constant.RequestPermissionCode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    reccordVideo();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.you_mustgrant_permission), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void saveAll(View view){
        Credentials credentials = Credentials.getInstance(perquisitionActivity.getApplicationContext());
        UploadFileAsync uploadFileAsync = new UploadFileAsync(credentials.getUsername(), credentials.getPassword());
        uploadFileAsync.execute(perquisitionActivity.getIntent().getExtras().getString(Constant.RESOURCES) + "/");
    }



    private  class UploadFileAsync extends AsyncTask<String, Void, String> {

        private AlertDialog dialog;
        private String username, password;
        private List<Preuve> results;
        private int total;

        public UploadFileAsync(String username, String password){
            this.username = username;
            this.password = password;
            dialog = new SpotsDialog(PerquisitionActivity.thisActivity.getApplicationContext());
            results = new ArrayList<>();

            PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
            preuvesDataSources.open();
            List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
            preuvesDataSources.close();

            total = preuveList.size();

        }
        @Override
        protected String doInBackground(String... params) {

            String url_string = params[0];

            Log.e("urlllllllllllll", url_string);
            String returnVal = "";
            // String uir_string = params[1];
            // Log.e("uriiiiiiiiiiiiiiii", uir_string);
            try {
                //String sourceFileUri = uir_string;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                //OutputStream out = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                try {

                    PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
                    preuvesDataSources.open();
                    List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
                    preuvesDataSources.close();

                    Log.e("result, total", " " + preuveList.size() + ", " + total);

                    for (Preuve preuve: preuveList){

                        //RessourceCreated ressourceCreated = (RessourceCreated) object;
                        File sourceFile = new File(preuve.getPath());
                        Log.e("ABSOLUTE PATH", sourceFile.getAbsolutePath());
                        if (sourceFile.isFile()) {



                            String upLoadServerUri =url_string; /*"http://idea-cm.club/magasino/image.php";*/
                            String urlParameters  = Constant.USERNAME + "=" + this.username + "&" + Constant.PASSWORD + "=" +
                                    this.password + "&" + Constant.KEY_WORD + "=" + preuve.getType() + "&" +
                                    Constant.ID + "=" + perquisition.getId() + "&" + Constant.LATITUDE + "=" + preuve.getLatitude()
                                    + "&" + Constant.LONGITUDE + "=" + preuve.getLongitude();


                            Log.e("11111111111111",upLoadServerUri + "(" + preuve.getLatitude() + ", " +preuve.getLongitude() + ")");
                            // open a URL connection to the Servlet
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL(upLoadServerUri + "?" + urlParameters);

                            Log.e("urlParameters", urlParameters);

                            // Open a HTTP connection to the URL
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            //conn.setChunkedStreamingMode(0);
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty(preuve.getType(), sourceFile.getAbsolutePath());
                            //conn.setRequestProperty();
                            //Log.e("2222222222222222",urlParameters);

                            dos = new DataOutputStream(conn.getOutputStream());
                            //out = new BufferedOutputStream(conn.getOutputStream());

                            //BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                            //writer.write(urlParameters);


                            //dos.write(postData);

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"" + preuve.getType() +  "\";filename=\"" +preuve.getPath() + "\"" + lineEnd);

                            dos.writeBytes(lineEnd);

                            Log.e("3333333333333333333333","333333333333333333333333333333333333333333333333");

                            // create a buffer of maximum size
                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            // read file and write it into form...
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            Log.e("bytesAvailable","" + bytesAvailable);

                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                                Log.e("image size:::::::::::", "" + bufferSize);
                            }

                            // send multipart form data necesssary after file
                            // data...
                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                            Log.e("444444444444","44444444444444444444444444444444444444444444444444444444444");

                            // Responses from the server (code and message)
                            int serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn.getResponseMessage();

                            InputStream in = conn.getInputStream();

                            Log.e("serverResponseCode", " " + serverResponseCode);

                            if (serverResponseCode >= 200 && serverResponseCode < 300) {

                                // messageText.setText(msg);
                                //Toast.makeText(ctx, "File Upload Complete.",
                                //      Toast.LENGTH_SHORT).show();

                                // recursiveDelete(mDirectory1);

                                BufferedReader br = null;
                                StringBuilder sb = new StringBuilder();
                                String line;
                                try {
                                    br = new BufferedReader(new InputStreamReader(in));
                                    while ((line = br.readLine()) != null) {
                                        sb.append(line);
                                    }

                                } catch (IOException e) {
                                    return Constant.KO + " 1           " + e.getMessage();
                                } finally {
                                    if (br != null) {
                                        try {
                                            br.close();
                                        } catch (IOException e) {
                                            return Constant.KO + " 2         " + e.getMessage();
                                        }
                                    }
                                }
                                in.close();
                                Log.e("55555555555555555","55555555555555555555555555555555555555555555555");
                                Log.e("sb.toString()", sb.toString());
                                //os.close();
                                returnVal = sb.toString();

                                results.add(preuve);
                                Log.e("results.size", "" + results.size());

                                fileInputStream.close();
                                //delete
                                File file = new File(preuve.getPath());
                                boolean deleted = file.delete();
                                Log.e("FILE DELETED", " " + deleted);
                                PreuvesDataSources preuvesDataSources1 = new PreuvesDataSources(getActivity().getApplicationContext());
                                preuvesDataSources1.open();
                                preuvesDataSources1.deletePreuve(preuve);
                                preuvesDataSources1.close();
                                //perquisition.removePreuve(preuve.getPath());
                                //PerquisitionActivity.publisher.unsubscribe(ressourceCreated);
                                Log.e("deleted: ", "" + preuve.toString());

                            }else {
                                Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                                returnVal =  Constant.KO + " 3     " ;
                            }



                        }else {
                            Log.e("NOT FILE", sourceFile.getAbsolutePath());
                        }
                    }



                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();
                    return Constant.KO +  " 4    " + e.getMessage();

                }
                // dialog.dismiss();

                // End else block


                //conn.disconnect();
            } catch (Exception ex) {
                // dialog.dismiss();
                ex.printStackTrace();
                return Constant.KO + " 5           " + ex.getMessage();
            }
            return returnVal;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (progress_save.getVisibility() == View.VISIBLE){
                progress_save.setVisibility(View.GONE);
            }

            Log.e("returnValllll", result +  " " + " " + results.size() + ", " + total);

            //Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();

            Log.e("result, total", " " + " " + results.size() + ", " + total);

            if (results.size() == total){
                save.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.successful_saved), Toast.LENGTH_LONG).show();
            }

            fetchData();
            //swipeContainer.setRefreshing(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("onPreExecute", "onPreExecute");
            if (progress_save.getVisibility() == View.GONE){
                progress_save.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}
