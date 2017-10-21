package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import dmax.dialog.SpotsDialog;

public class SearchActivity extends AppCompatActivity {

    private String keyType;
    private Map<String, String> correspondance;
    private Toolbar toolbar;

    private TextView type_shower;

    private int selectedIndex = 0;

    public static AppCompatActivity thisActivity;

    private AppCompatSpinner spinner_critere_recherche_individu, spinner_critere_recherche_vehicule,
            spinner_critere_recherche_objet;

    private EditText EditText_mot_cle;

    private Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        thisActivity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        spinner_critere_recherche_individu = (AppCompatSpinner) findViewById(R.id.spinner_critere_recherche_individu);
        spinner_critere_recherche_vehicule = (AppCompatSpinner) findViewById(R.id.spinner_critere_recherche_vehicule);
        spinner_critere_recherche_objet = (AppCompatSpinner) findViewById(R.id.spinner_critere_recherche_objet);

        spinner_critere_recherche_individu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_critere_recherche_vehicule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_critere_recherche_objet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText_mot_cle = (EditText) findViewById(R.id.EditText_mot_cle);

        correspondance = Constant.getSearchTypeValue();

        Bundle bundle = getIntent().getExtras();
        keyType = bundle.getString("type");

        type_shower = (TextView) findViewById(R.id.type_shower);
        type_shower.setText(correspondance.get(keyType));


        if (keyType.equals("" + Constant.SEARCH_INDIVIDU)){
            //Toast.makeText(getApplicationContext(), "enter", Toast.LENGTH_LONG).show();
            hideOther(R.id.spinner_critere_recherche_individu);
        }

        if (keyType.equals("" + Constant.SEARCH_VEHICULE)){
            hideOther(R.id.spinner_critere_recherche_vehicule);
        }

        if (keyType.equals("" + Constant.SEARCH_OBJECT)){
            hideOther(R.id.spinner_critere_recherche_objet);
        }

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    private void submitForm() {
        if (EditText_mot_cle.getText().toString().isEmpty()){

            Toast.makeText(getApplicationContext(), getString(R.string.invalide_data), Toast.LENGTH_LONG).show();
            return;
        }

        sendRequest();
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

    public void hideOther(int exceptMe){

        if (spinner_critere_recherche_individu.getId() == exceptMe){
            spinner_critere_recherche_vehicule.setVisibility(View.GONE);
            spinner_critere_recherche_objet.setVisibility(View.GONE);
            spinner_critere_recherche_individu.setVisibility(View.VISIBLE);
        }

        if (spinner_critere_recherche_vehicule.getId() == exceptMe){
            spinner_critere_recherche_individu.setVisibility(View.GONE);
            spinner_critere_recherche_objet.setVisibility(View.GONE);
            spinner_critere_recherche_vehicule.setVisibility(View.VISIBLE);
        }

        if (spinner_critere_recherche_objet.getId() == exceptMe){
            spinner_critere_recherche_individu.setVisibility(View.GONE);
            spinner_critere_recherche_vehicule.setVisibility(View.GONE);
            spinner_critere_recherche_objet.setVisibility(View.VISIBLE);
        }

    }

    public String makeQuery(){


        Credentials credentials = Credentials.getInstance(this);

        String query = "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword() ;

        query += "&" + Constant.DOMAINE + "=" + keyType;
        query += "&" + Constant.CRITERIA + "=" + selectedIndex;
        query += "&" + Constant.KEY_WORD + "=" + EditText_mot_cle.getText().toString();

        return  query;
    }

    public void sendRequest(){
        String query = makeQuery();
        Researcher researcher = new Researcher();
        researcher.execute(Constant.URL_LINK, query);
    }

    private class Researcher extends AsyncTask<String, Integer, String> {
        private AlertDialog dialog;
        //private String username, password;
        public Researcher() {

            dialog = new SpotsDialog(SearchActivity.thisActivity);
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
            String query = strings[1];
            String resultat = "";
            URL url = null;
            HttpsURLConnection urlConnection = null;

            SSLContext context = null;
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

                url = new URL(url_str + query);


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
            }

            if (1==1) return Constant.KO;

            try {

                urlConnection.setRequestMethod("GET");

                //urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                /*OutputStream os = urlConnection.getOutputStream();

                OutputStreamWriter out = new OutputStreamWriter(os);
                out.write(query);
                out.close();*/
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


            Log.e("result log", result);

            if (!result.equals(Constant.KO)){
                //error_message.setVisibility(View.VISIBLE);
                //error_message.setText(R.string.connection_error);
                return;
            }


            Intent intent = new Intent(getApplicationContext(), ResultSearchActivity.class);
            String resultat = null;
            try {
                InputStream inputStream  =  thisActivity.getAssets().open("objet.json");
                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    br = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                } catch (IOException e) {

                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {

                        }
                    }
                }
                inputStream.close();
                //os.close();
                resultat = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            intent.putExtra(Constant.RESULT, resultat);
            //intent.putExtra(Constant.DOMAINE,  Constant.SEARCH_INDIVIDU);
            intent.putExtra(Constant.DOMAINE,  Constant.SEARCH_VEHICULE);
            intent.putExtra(Constant.SEARCH_REULT_DISPLAY_VALUE, Constant.getSearchTypeValue().get("" + Constant.SEARCH_VEHICULE_RESULT));

            startActivity(intent);
            //inputPassword.setText("");

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
