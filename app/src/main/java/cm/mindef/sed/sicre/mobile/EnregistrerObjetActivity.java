package cm.mindef.sed.sicre.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import cm.mindef.sed.sicre.mobile.db.EnregistrementDatasource;
import cm.mindef.sed.sicre.mobile.domain.EnregObjet;
import cm.mindef.sed.sicre.mobile.domain.EnregVehicule;
import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static cm.mindef.sed.sicre.mobile.RecorderAudioActivity.RequestPermissionCode;

public class EnregistrerObjetActivity extends AppCompatActivity implements LocationListener {

    private Toolbar toolbar;
    private User user;
    private int total;

    public static AppCompatActivity thisActivity;

    private EditText EditText_titre, EditText_description, EditText_name_proprietaire;

    private TextInputLayout TextInputLayout_titre,
            TextInputLayout_description, TextInputLayout_name_proprietaire;

    private TextView error_message;

    private Button btn_photo_objet, btn_enregistrer_objet;

    private Bitmap bitmapPhoto;

    private ByteArrayOutputStream bitByteArrayOutputStream;

    private ImageView photo_objet;
    public final int RequestPermissionCode_Camera = 1;

    private ProgressBar progress_save;

    private File imageFile;
    private FloatingActionButton save;


    private LocationManager locationManager;

    private Location location;
    private String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregistrer_objet);

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
        user = (User) getIntent().getExtras().get(Constant.USER);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        EditText_titre = findViewById(R.id.EditText_titre);
        EditText_name_proprietaire = findViewById(R.id.EditText_name_proprietaire);
       // EditText_matiere = findViewById(R.id.EditText_matiere);
        EditText_description = findViewById(R.id.EditText_description);

        TextInputLayout_titre = findViewById(R.id.TextInputLayout_titre);
        TextInputLayout_name_proprietaire = findViewById(R.id.TextInputLayout_name_proprietaire);
        //TextInputLayout_matiere = findViewById(R.id.TextInputLayout_matiere);
        TextInputLayout_description = findViewById(R.id.TextInputLayout_description);

        error_message = findViewById(R.id.error_message);
        btn_photo_objet = findViewById(R.id.btn_photo_objet);
        btn_enregistrer_objet = findViewById(R.id.btn_enregistrer_objet);
        photo_objet = findViewById(R.id.photo_objet);

        progress_save = findViewById(R.id.progress_save);


        btn_photo_objet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPicture(v);
            }
        });

        btn_enregistrer_objet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getObjet().getCreate().isCan()){
                    if (validateField()){
                        bitByteArrayOutputStream = new ByteArrayOutputStream();
                        if(bitmapPhoto != null){
                            bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bitByteArrayOutputStream);
                        }

                        Credentials credentials = Credentials.getInstance(getApplicationContext());
                        String query = null;
                        EnregObjet enregObjet = null;
                        try {
                            String questionMark = (user.getIndividu().getCreate().getUrlResource().contains("?"))? "&":"?";
                            query = questionMark + Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                                    Constant.PASSWORD + "=" + credentials.getPassword() +
                                    "&titre=" + URLEncoder.encode(EditText_titre.getText().toString().trim(), "UTF-8") +
                                    "&owner=" + URLEncoder.encode(EditText_name_proprietaire.getText().toString().trim(), "UTF-8") +
                                     "&description=" +  URLEncoder.encode(EditText_description.getText().toString().trim(), "UTF-8")+
                                    "&vehicule=" +  URLEncoder.encode("false", "UTF-8")+
                                    "&" + Constant.LATITUDE + "=" + Constant.latitudeNetwork +
                                    "&" + Constant.LONGITUDE + "=" + Constant.longitudeNetwork;
                            enregObjet = new EnregObjet(
                                    "0",
                                    EditText_name_proprietaire.getText().toString().trim(),
                                    EditText_titre.getText().toString().trim(),
                                    EditText_description.getText().toString().trim(),
                                    imageFile.getAbsolutePath(),
                                    "image",
                                    Constant.latitudeNetwork,
                                    Constant.longitudeNetwork


                            );
                            //EnregObjet(String id, String owner, String titre, String description, String path, String type, double latitude, double longitude) {
                        } catch (UnsupportedEncodingException e) {
                            //e.printStackTrace();
                        }

                        (new EnregistrerObjetActivity.UploadFileAsync(enregObjet, 0)).execute(user.getObjet().getCreate().getUrlResource() + "/"/*Constant.URL_LINK + Constant.ENREGISTREMENT_OBJET*/, query);

                    }

                }else {
                    Toast.makeText(getApplicationContext(), R.string.not_autorized, Toast.LENGTH_LONG).show();
                }

            }
        });

        save = findViewById(R.id.save);

        EnregistrementDatasource enregistrementDatasource = new EnregistrementDatasource(getApplicationContext());
        enregistrementDatasource.open();
        List<EnregObjet> enregObjets = enregistrementDatasource.getAllEnregObject();
        if (enregObjets.size() > 0){
            save.setVisibility(View.VISIBLE);
        }else {
            save.setVisibility(View.GONE);
        }
        enregistrementDatasource.close();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnregistrementDatasource enregistrementDatasource = new EnregistrementDatasource(getApplicationContext());
                enregistrementDatasource.open();
                List<EnregObjet> enregObjets = enregistrementDatasource.getAllEnregObject();
                enregistrementDatasource.close();

                total = 0;
                for (EnregObjet enregObjet : enregObjets){
                    Credentials credentials = Credentials.getInstance(getApplicationContext());
                    String query = null;
                    try {

                        String questionMark = (user.getIndividu().getCreate().getUrlResource().contains("?"))? "&":"?";
                        query = questionMark + Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                                Constant.PASSWORD + "=" + credentials.getPassword() +
                                "&titre=" + URLEncoder.encode(enregObjet.getTitre(), "UTF-8") +
                                "&owner=" + URLEncoder.encode(enregObjet.getOwner(), "UTF-8") +
                                "&description=" +  URLEncoder.encode(enregObjet.getDescription(), "UTF-8")+
                                "&vehicule=" +  URLEncoder.encode("false", "UTF-8")+
                                "&" + Constant.LATITUDE + "=" + enregObjet.getLatitude() +
                                "&" + Constant.LONGITUDE + "=" +enregObjet.getLongitude();
                        imageFile = new File(enregObjet.getPath());
                    } catch (UnsupportedEncodingException e) {
                        //e.printStackTrace();
                        //return;
                    }

                    (new EnregistrerObjetActivity.UploadFileAsync(enregObjet, 1)).execute(user.getIndividu().getCreate().getUrlResource() + "/", query);
                }

                if (total == enregObjets.size()){
                    save.setVisibility(View.GONE);
                }
            }
        });


    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateField() {
        if (!validateTitre()){
            return false;
        }
        if (!validateProprietaire()){
            return false;
        }

        if (!validateDescription()){
            return false;
        }
        return true;
    }

    private boolean validateTitre() {
        if (EditText_titre.getText().toString().trim().isEmpty()) {
            TextInputLayout_titre.setError(getString(R.string.err_name));
            requestFocus(EditText_titre);
            return false;
        } else {
            TextInputLayout_titre.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateProprietaire() {
        if (EditText_name_proprietaire.getText().toString().trim().isEmpty()) {
            TextInputLayout_name_proprietaire.setError(getString(R.string.err_name));
            requestFocus(EditText_name_proprietaire);
            return false;
        } else {
            TextInputLayout_name_proprietaire.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDescription() {
        if (EditText_description.getText().toString().trim().isEmpty()) {
            TextInputLayout_description.setError(getString(R.string.err_num_doc));
            requestFocus(EditText_description);
            return false;
        } else {
            TextInputLayout_description.setErrorEnabled(false);
        }

        return true;
    }



    public void getPicture(View view){

        if (checkPermission()){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, Constant.REQUEST_CODE_FOR_ADD_IMG_PERQUISITION);
        }else {
            requestPermission();
        }
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, CAMERA}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.e("onRequestPermissResult", "Request code: " + requestCode + "  permission: " + permissions);
        switch (requestCode) {
            case RequestPermissionCode: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

                } else  if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, Constant.REQUEST_CODE_FOR_ADD_IMG_PERQUISITION);


                }else {
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


    private File saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Constant.PERQUISITION_IMAGE_ROOT + Constant.ENREGISTREMENT_OBJET_LOCAL, Context.MODE_PRIVATE);
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
            Toast.makeText(getApplicationContext(), "lastNum: " + lastNum, Toast.LENGTH_LONG).show();
        }


        //Toast.makeText(getActivity().getApplicationContext(), "fileName: " + fileName, Toast.LENGTH_LONG).show();

        File mypath = new File(directory,  fileName + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            if(bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)){
                return mypath;
            }else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


       /* String path =  directory.getAbsolutePath() + "/" + mypath.getName();

        PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
        preuvesDataSources.open();

        Preuve preuve = preuvesDataSources.createPreuve(path, Constant.latitudeNetwork, Constant.longitudeNetwork, "" + perquisition.getId(), "image");

        preuvesDataSources.close();

        showSaveButton();*/

        //PerquisitionActivity.publisher.mySubscribe(new RessourceCreated(mypath.getAbsolutePath(), "image", getActivity().getApplicationContext(), perquisition));
        //Log.e("LENGTH PUBLISHER", "" + PerquisitionActivity.publisher.subscribers().size());

        return  null;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*  Back arrow*/
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("onRequestPermissResult", "Request code: " + requestCode + "  permission: " + permissions );
        switch (requestCode) {
            case RequestPermissionCode_Camera: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, Constant.REQUEST_CODE_FOR_ADD_IMG_PERQUISITION);

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
*/


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CODE_FOR_ADD_IMG_PERQUISITION && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmapPhoto = (Bitmap) extras.get("data");
            photo_objet.setImageBitmap(bitmapPhoto);
            imageFile = saveToInternalStorage(bitmapPhoto);

        }
    }

    private  class UploadFileAsync extends AsyncTask<String, Void, String> {

        private AlertDialog dialog;
        private EnregObjet enregObjet;
        private int old;

        public UploadFileAsync( EnregObjet enregObjet, int old){
            this.enregObjet = enregObjet;
            this.old = old;
        }
        @Override
        protected String doInBackground(String... params) {

            String url_string = params[0];

            Log.e("urlllllllllllll", url_string);
            String returnVal = "";
            String uir_string = params[1];
            Log.e("uriiiiiiiiiiiiiiii", uir_string);
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

                    if (imageFile.isFile()) {
                        FileInputStream fileInputStream = new FileInputStream(imageFile);
                        URL url = new URL(params[0] + params[1]);
                        Log.e("url.toString()", url.toString());
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
                        conn.setRequestProperty("image", imageFile.getAbsolutePath());

                        dos = new DataOutputStream(conn.getOutputStream());


                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\""+ "image" +"\";filename=\"" +imageFile.getName() + "\"" + lineEnd);

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

                            BufferedReader br = null;
                            StringBuilder sb = new StringBuilder();
                            String line;
                            try {
                                br = new BufferedReader(new InputStreamReader(in));
                                while ((line = br.readLine()) != null) {
                                    sb.append(line);
                                }

                            } catch (IOException e) {
                                return Constant.KO;
                            } finally {
                                if (br != null) {
                                    try {
                                        br.close();
                                    } catch (IOException e) {
                                        return Constant.KO;
                                    }
                                }
                            }
                            in.close();
                            Log.e("55555555555555555","55555555555555555555555555555555555555555555555");
                            Log.e("sb.toString()", sb.toString());
                            //os.close();
                            returnVal = sb.toString();

                            fileInputStream.close();

                            if (old == 1){
                                EnregistrementDatasource enregistrementDatasource = new EnregistrementDatasource(getApplicationContext());
                                enregistrementDatasource.open();
                                enregistrementDatasource.deleteEnreg(Constant.OBJECT, imageFile.getAbsolutePath());
                                enregistrementDatasource.close();
                            }

                            boolean deleted = imageFile.delete();
                            Log.e("FILE DELETED", " " + deleted);


                        }else {
                            Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                            returnVal =  Constant.KO  ;
                        }



                    }else {
                        //Log.e("NOT FILE", imageFile.getAbsolutePath());
                        return Constant.KO;
                    }
                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();
                    return Constant.KO;

                }
                // dialog.dismiss();

                // End else block


                //conn.disconnect();
            } catch (Exception ex) {
                // dialog.dismiss();
                ex.printStackTrace();
                return Constant.KO;
            }
            return returnVal;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.e("returnValllll", result);
            error_message = findViewById(R.id.error_message);
            //EditText EditText_titre, EditText_description, EditText_name_proprietaire;
            if (result.equals("1")){
                error_message.setText("Succesfully saved");
                error_message.setTextColor(Color.GREEN);
                photo_objet.setImageDrawable(null);
                EditText_name_proprietaire.setText("");
                EditText_titre.setText("");
                EditText_description.setText("");
                total++;
            }else{
                error_message.setText("Failed to save data");
                error_message.setTextColor(Color.RED);

                if (old == 0){
                    EnregistrementDatasource enregistrementDatasource = new EnregistrementDatasource(getApplicationContext());
                    enregistrementDatasource.open();
                    long id = enregistrementDatasource.createObjet(enregObjet.getOwner(), enregObjet.getTitre(), enregObjet.getDescription(), enregObjet.getPath(),
                            enregObjet.getLatitude(), enregObjet.getLongitude(), enregObjet.getType());
                    Log.e("idididid", "" + id);
                    enregistrementDatasource.close();
                    //createObjet(String owner, String titre, String description,  String path, double latitude, double longitude, String type)
                }
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new SpotsDialog(EnregistrerObjetActivity.thisActivity);
            dialog.setMessage(getResources().getString(R.string.chargement));
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
