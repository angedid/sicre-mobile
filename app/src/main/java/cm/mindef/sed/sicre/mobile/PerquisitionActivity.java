package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cm.mindef.sed.sicre.mobile.adapters.ViewPagerAdapter;
import cm.mindef.sed.sicre.mobile.db.PreuvesDataSources;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.domain.Preuve;
import cm.mindef.sed.sicre.mobile.fragments.ImageFragment;
import cm.mindef.sed.sicre.mobile.fragments.SoundFragment;
import cm.mindef.sed.sicre.mobile.fragments.TextFragment;
import cm.mindef.sed.sicre.mobile.fragments.VideoFragment;
import cm.mindef.sed.sicre.mobile.utils.Constant;

import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.oservable.Publisher;
import cm.mindef.sed.sicre.mobile.oservable.event.RessourceCreated;
import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static cm.mindef.sed.sicre.mobile.RecorderAudioActivity.RequestPermissionCode;

public class PerquisitionActivity extends AppCompatActivity implements LocationListener {

    public static AppCompatActivity thisActivity;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Toolbar toolbar;

    public static Publisher publisher;

    private LocationManager locationManager;

    private Location location;
    private String provider;


    private static Perquisition perquisition;
    private String ressource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perquisition);


        if (!checkPermission()) {
            requestPermission();
        } else {
            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            location = locationManager.getLastKnownLocation(provider);
        }


        if (location != null) {
            //System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            //latituteField.setText("Location not available");
            //longitudeField.setText("Location not available");
        }

        thisActivity = this;

        if (publisher == null) {
            publisher = Publisher.instance();
        }

        /*save = findViewById(R.id.save);
        save.setVisibility(View.GONE);
        progress_save = findViewById(R.id.progress_save);
        progress_save.setVisibility(View.GONE);*/

        perquisition = (Perquisition) getIntent().getExtras().get(Constant.PERQUISITION);
        ressource = getIntent().getExtras().getString(Constant.RESOURCES);

        Log.e("ressource", ressource);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    Toast.makeText(getApplicationContext(), "Selected index: " + (position + 1), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_IDLE) {

                } else if (state == SCROLL_STATE_DRAGGING) {

                } else if (state == SCROLL_STATE_SETTLING) {

                }
            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //tabLayout.getTabAt(0).setIcon(R.drawable.icons8_align_justify_24);
        tabLayout.getTabAt(0).setIcon(R.drawable.icons8_vintage_camera_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_headphone);
        tabLayout.getTabAt(2).setIcon(R.drawable.icons8_movie_24);
        tabLayout.setSelectedTabIndicatorHeight(8);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //loadPerquisitionData();

    }

    @Override
    protected void onPause() {
        if (locationManager != null)
            locationManager.removeUpdates(this);
        super.onPause();

    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (provider != null)
                locationManager.requestLocationUpdates(provider, 400, 1, this);
            return;
        }

    }


    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.e("onRequestPermissResult", "Request code: " + requestCode + "  permission: " + permissions);
        switch (requestCode) {
            case RequestPermissionCode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    // Get the location manager
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    // Define the criteria how to select the locatioin provider -> use
                    // default
                    Criteria criteria = new Criteria();
                    provider = locationManager.getBestProvider(criteria, false);
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    location = locationManager.getLastKnownLocation(provider);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), getString(R.string.you_mustgrant_permission), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }




    public void loadPerquisitionData() {
       /* List<String> strings = new ArrayList<>();
        List<String> images = getSavedImages(perquisition.getId());
        strings.addAll(images);
        List<String> audios = getSavedAudio(perquisition.getId());
        strings.addAll(audios);
        List<String> videos = getSavedVideo(perquisition.getId());
        strings.addAll(videos);*/
        /*PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getApplicationContext());
        preuvesDataSources.open();
        List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
        preuvesDataSources.close();
        if (preuveList.size() > 0){
                    //save.setVisibility(View.VISIBLE);
            Log.e("PREUVE DETECTE", preuveList.toString());
        }else {
            save.setVisibility(View.GONE);
        }*/

    }

    public List<String> getSavedImages(String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
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
                retVal.add(files[i].getAbsolutePath());

        }

        return  retVal;

    }


    public List<String> getSavedAudio(String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(Constant.PERQUISITION_SOUND_ROOT + perquisitionId, Context.MODE_PRIVATE);
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
                retVal.add(files[i].getAbsolutePath());

        }

        return  retVal;

    }

    private List<String> getSavedVideo(String perquisitionId) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
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
                retVal.add(files[i].getAbsolutePath());
        }

        return  retVal;
    }



    /*

    public String getNextAudioFileName(String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Constant.PERQUISITION_SOUND_ROOT + perquisitionId, Context.MODE_PRIVATE);
        // Create imageDir

        File[] files = directory.listFiles();

        Arrays.sort(files);

        int surfix = (int) (System.currentTimeMillis()/1000);
        String fileName = "audio_";

        if (files.length == 0){
            fileName += surfix;
        }else {
            String lastFileName = files[files.length-1].getName();
            int lastNum = Integer.parseInt(lastFileName.split("_")[1].split("\\.")[0]) + 1;
            fileName += "" + lastNum;
            Toast.makeText(getApplicationContext(), "lastNum: " + lastNum, Toast.LENGTH_LONG).show();
        }

        return directory.getAbsolutePath() + "/" + fileName + ".3gp";


    }

     */


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(new TextFragment(), getString(R.string.text));
        adapter.addFragment(new ImageFragment(), getString(R.string.image));
        adapter.addFragment(new SoundFragment(), getString(R.string.son));
        adapter.addFragment(new VideoFragment(), getString(R.string.video));
        viewPager.setAdapter(adapter);
    }

    public void saveAll(View view){

        Credentials credentials = Credentials.getInstance(getApplicationContext());
        UploadFileAsync uploadFileAsync = new UploadFileAsync(credentials.getUsername(), credentials.getPassword());
        uploadFileAsync.execute(ressource + "/" /*Constant.URL_LINK + Constant.PERQUISITION_SAVE*/);
    }

    @Override
    public void onLocationChanged(Location location) {
        Constant.longitudeNetwork = location.getLongitude();
        Constant.latitudeNetwork = location.getLatitude();
        Log.e("NEW LOCATION", "(" + Constant.latitudeNetwork + ", " + Constant.longitudeNetwork + ")");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
            total = publisher.subscribers().size();

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

                        PreuvesDataSources preuvesDataSources = new PreuvesDataSources(thisActivity);
                        preuvesDataSources.open();
                        List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
                        preuvesDataSources.close();

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
                            conn.setRequestProperty("image", sourceFile.getAbsolutePath());
                            //conn.setRequestProperty();
                            //Log.e("2222222222222222",urlParameters);

                            dos = new DataOutputStream(conn.getOutputStream());
                            //out = new BufferedOutputStream(conn.getOutputStream());

                            //BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                            //writer.write(urlParameters);


                            //dos.write(postData);

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" +preuve.getPath() + "\"" + lineEnd);

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
                                //os.close();
                                returnVal = sb.toString();

                                results.add(preuve);

                            }else {
                                Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                                returnVal =  Constant.KO + " 3     " ;
                            }


                                fileInputStream.close();
                            //delete
                            File file = new File(preuve.getPath());
                            boolean deleted = file.delete();
                            Log.e("FILE DELETED", " " + deleted);
                                PreuvesDataSources preuvesDataSources1 = new PreuvesDataSources(getApplicationContext());
                                preuvesDataSources1.open();
                                preuvesDataSources1.deletePreuve(preuve);
                                preuvesDataSources1.close();
                            //PerquisitionActivity.publisher.unsubscribe(ressourceCreated);
                            Log.e("deleted: ", "" + preuve.toString());
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

            /* if (progress_save.getVisibility() == View.VISIBLE){
                 progress_save.setVisibility(View.GONE);
             }*/

            Log.e("returnValllll", result);

            Toast.makeText(thisActivity, result, Toast.LENGTH_LONG).show();

            if (results.size() == total){
                //save.setVisibility(View.GONE);
                Toast.makeText(thisActivity, getString(R.string.successful_saved), Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog.setMessage(getApplicationContext().getString(R.string.chargement));
             dialog.show();*/
           /* if (progress_save.getVisibility() == View.GONE){
                progress_save.setVisibility(View.VISIBLE);
            }*/

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
