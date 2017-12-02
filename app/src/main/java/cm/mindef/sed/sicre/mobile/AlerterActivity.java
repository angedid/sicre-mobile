package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static cm.mindef.sed.sicre.mobile.RecorderAudioActivity.RequestPermissionCode;

public class AlerterActivity extends AppCompatActivity  implements LocationListener {

    private Toolbar toolbar;

    public static AppCompatActivity thisActivity;

    private EditText EditText_title, EditText_message;

    private TextInputLayout TextInputLayout_title, TextInputLayout_message;

    private TextView error_message;

    private Button btn_enregistrer_alert;

    private LocationManager locationManager;
    private User user;

    private Location location;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerter);

        user = (User) getIntent().getExtras().get(Constant.USER);

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        /*
        private EditText , ;

    private TextInputLayout , ;

    private TextView error_message;

    private Button btn_enregistrer_affaire;

         */

        EditText_title = findViewById(R.id.EditText_title);
        EditText_message = findViewById(R.id.EditText_message);

        TextInputLayout_title = findViewById(R.id.TextInputLayout_title);
        TextInputLayout_message = findViewById(R.id.TextInputLayout_message);

        error_message = findViewById(R.id.error_message);
        btn_enregistrer_alert = findViewById(R.id.btn_enregistrer_alert);


        btn_enregistrer_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validateTitle() && validateMessage()){
                    Credentials credentials = Credentials.getInstance(getApplicationContext());
                    String query = null;
                    try {
                        query =  Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                                Constant.PASSWORD + "=" + credentials.getPassword() + "&titre=" +  URLEncoder.encode(EditText_title.getText().toString().trim(), "UTF-8") + "&message=" +
                                URLEncoder.encode(EditText_message.getText().toString().trim() , "UTF-8") + "&porte=" + "1"
                                + "&" + Constant.LATITUDE + "=" + Constant.latitudeNetwork
                                + "&" + Constant.LONGITUDE + "=" + Constant.longitudeNetwork;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    String ressource = user.getAlert().getCreate().getUrlResource() + "/";
                    (new AlerterActivity.UploadFileAsync()).execute(ressource, query);
                }

            }
        });


    }


    private boolean validateTitle() {
        if (EditText_title.getText().toString().trim().isEmpty()) {
            TextInputLayout_title.setError(getString(R.string.invalide_data));
            requestFocus(EditText_title);
            return false;
        } else {
            TextInputLayout_title.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMessage() {
        if (EditText_message.getText().toString().trim().isEmpty() || EditText_message.getText().toString().trim().length() < 10) {
            TextInputLayout_message.setError(getString(R.string.invalide_data));
            requestFocus(EditText_message);
            return false;
        } else {
            TextInputLayout_message.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    private class UploadFileAsync extends AsyncTask<String, Void, String> {
        private AlertDialog dialog;
        private int serverResponseCode;

        public UploadFileAsync(){
            dialog = new SpotsDialog(AlerterActivity.thisActivity);
        }
        @Override
        protected String doInBackground(String... params) {

            String url_string = params[0];

            Log.e("urlllllllllllll", url_string);
            String returnVal = "";
            String query = params[1];
            // Log.e("query", query);

            //String sourceFileUri = uir_string;

            HttpURLConnection conn = null;


            try {

                String upLoadServerUri = url_string;/*"http://idea-cm.club/magasino/enregistrement.php";*/

                URL url = new URL(upLoadServerUri);

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setRequestMethod("POST");
                Log.e("3333333333333333333333","333333333333333333333333333333333333333333333333");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.e("queryyyyyyyy", query);
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
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
                    Log.e("55555555555555555","55555555555555555555555555555555555555555555555 sb.toString() : " + sb.toString());
                    //os.close();
                    returnVal = sb.toString();

                }else {
                    Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                    returnVal =  Constant.KO + " 3     " ;
                }


            } catch (MalformedURLException e) {

                // dialog.dismiss();
                e.printStackTrace();
                return Constant.KO +  " 4    " + e.getMessage();

            } catch (ProtocolException e) {
                e.printStackTrace();
                return Constant.KO +  " 5   " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return Constant.KO +  " 6    " + e.toString();
            }
            // dialog.dismiss();

            // End else block



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
            if (serverResponseCode >= 200 && serverResponseCode < 300){
                error_message.setText("Succesfully saved");
                error_message.setTextColor(Color.GREEN);
                EditText_title.setText("");
                EditText_message.setText("");
            }else{
                error_message.setText("Failed to save data");
                error_message.setTextColor(Color.RED);
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getResources().getString(R.string.chargement));
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
