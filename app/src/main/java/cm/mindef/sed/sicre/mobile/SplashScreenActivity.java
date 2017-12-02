package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import cm.mindef.sed.sicre.mobile.domain.SearchCriteria;
import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import dmax.dialog.SpotsDialog;

public class SplashScreenActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    private TextView error_message;
    public static AppCompatActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        thisActivity = this;

        error_message = (TextView) findViewById(R.id.error_message);
        error_message.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Credentials credentials = Credentials.getInstance(getApplicationContext());
                if (credentials.getUsername() != null && credentials.getPassword() !=null && credentials.getStayConnected() != null){
                    Authenticator authenticator = new Authenticator(credentials.getUsername(), credentials.getPassword(), credentials.getStayConnected());
                    authenticator.execute(Constant.URL_LINK + Constant.LOGIN_PATH + "?" + Constant.USERNAME + "=" + credentials.getUsername() +
                    "&" + Constant.PASSWORD + "=" + credentials.getPassword());
                    Log.e("NKALLA00", "Nkalla000000000000000111111111111111111111");
                }else{
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Bundle bundle = getIntent().getExtras();
                    Intent i = new Intent(getApplicationContext(), AutenticationActivity.class);
                    if (bundle != null && bundle.get(Constant.REMOTE_MESSAGE) != null){
                        i.putExtra(Constant.USER, (User)bundle.get(Constant.USER));
                        i.putExtra(Constant.REMOTE_MESSAGE, (RemoteMessage)bundle.get(Constant.REMOTE_MESSAGE));
                    }

                    startActivity(i);
                    // close this activity
                    finish();

                }



            }
        }, SPLASH_TIME_OUT);

    }

    private class Authenticator extends AsyncTask<String, Integer, String> {
        private AlertDialog dialog;
        private String username, password;
        private String stayConnected;
        public Authenticator(String username, String password, String stayConnected) {
            this.username = username;
            this.password = password;
            this.stayConnected = stayConnected;
            this.stayConnected = stayConnected;

            dialog = new SpotsDialog(SplashScreenActivity.thisActivity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getResources().getString(R.string.chargement));
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String url_str = strings[0];

            String resultat = "";
            URL url = null;
            HttpURLConnection urlConnection = null;

            Log.e("URL", url_str);
            try {
                url = new URL(url_str);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            /*SSLContext context = null;
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");

                InputStream caInput  =  getAssets().open("sicre.crt");
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                } finally {
                    caInput.close();
                }

                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                url = new URL(url_str + "?" + Constant.USERNAME + "=" + this.username + "&" + Constant.PASSWORD + "=" + this.password);


                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setSSLSocketFactory(context.getSocketFactory());
                urlConnection.setHostnameVerifier(new NullHostNameVerifier());

            } catch (CertificateException e) {
                return Constant.KO;
            } catch (FileNotFoundException e) {
                return Constant.KO;
            } catch (IOException e) {
                return Constant.KO;
            } catch (NoSuchAlgorithmException e) {
                return Constant.KO;
            } catch (KeyStoreException e) {
                return Constant.KO;
            } catch (KeyManagementException e) {
                return Constant.KO;
            }*/

            //if (1==1) return Constant.KO;

            try {

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                InputStream in = urlConnection.getInputStream();

                int statusCode = urlConnection.getResponseCode();
                if (statusCode >= 200 && statusCode < 300){
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
                    //os.close();
                    resultat = sb.toString();
                }else{
                    return Constant.KO;
                }


            } catch (IOException e) {
                return Constant.KO;
            }

            return resultat;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute((String) result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            //wall_paper.setVisibility(View.GONE);

            Log.e("result log", result);

            if (result.equals(Constant.KO)){
                error_message.setVisibility(View.VISIBLE);
                error_message.setText(R.string.an_error_occure);
                return;
            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(Constant.STATUS);
                if (status.equals(Constant.FAILED)){
                    error_message.setVisibility(View.VISIBLE);
                    error_message.setText(R.string.connection_error);
                    return;
                }

                JSONObject userJsonObj = jsonObject.getJSONObject(Constant.USER);

                User user = User.getInstance(userJsonObj);

                if (user == null){
                    error_message.setVisibility(View.VISIBLE);
                    error_message.setText(R.string.an_error_occure);
                    return;
                }

                JSONObject criteriaJsonObj = jsonObject.getJSONObject(Constant.SEARCH_CRITERIA);
                //Log.e("criteriaJsonObj", criteriaJsonObj.toString());
                SearchCriteria searchCriteria = SearchCriteria.getInstance(criteriaJsonObj);



                if (searchCriteria == null){
                    error_message.setVisibility(View.VISIBLE);
                    error_message.setText(getString(R.string.an_error_occure) + " voila" );
                    return;
                }



                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra(Constant.USERNAME, this.username);
                intent.putExtra(Constant.PASSWORD, this.password);
                intent.putExtra(Constant.USER, user);

                intent.putExtra(Constant.SEARCH_CRITERIA, searchCriteria);

                SharedPreferences.Editor editor = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE).edit();
                editor.putString(Constant.USERNAME, this.username);
                editor.putString(Constant.PASSWORD, this.password);
                editor.commit();

               // if (checkbox_stay_connected.isChecked())
                 //   Credentials.saveCredential(getApplicationContext(), this.username, this.password, this.stayConnected);

                Bundle bundle = getIntent().getExtras();
                if (bundle != null && bundle.get(Constant.REMOTE_MESSAGE) != null){
                    intent.putExtra(Constant.USER, (User)bundle.get(Constant.USER));
                    intent.putExtra(Constant.REMOTE_MESSAGE, (RemoteMessage)bundle.get(Constant.REMOTE_MESSAGE));
                    Log.e("NKALLA", "Nkalla");
                }
                startActivity(intent);
               // inputPassword.setText("");
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }

        private class NullHostNameVerifier implements HostnameVerifier {

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                boolean retVal;
                try {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    retVal =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE_1_1 && Constant.HOSTNAME.equals("cm.mindef.sed.sicre.mobile");
                }catch (Exception e){
                    retVal = false;
                }
                return retVal;
            }
        }
    }
}
