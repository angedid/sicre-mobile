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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import cm.mindef.sed.sicre.mobile.adapters.CustomSpinnerAdapter;
import cm.mindef.sed.sicre.mobile.domain.DocType;
import cm.mindef.sed.sicre.mobile.domain.SearchCriteria;
import cm.mindef.sed.sicre.mobile.fragments.ChercherFragment;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import dmax.dialog.SpotsDialog;

import static cm.mindef.sed.sicre.mobile.fragments.ChercherFragment.mapDocType;

public class SearchActivity extends AppCompatActivity {

    private int keyType;

    private String object;

    private Map<String, String> correspondance;

    private Toolbar toolbar;

    private TextView type_shower;

    private int selectedIndex = 0;

    private String selectedString;

    public static AppCompatActivity thisActivity;

    private AppCompatSpinner spinner_critere_recherche_individu, spinner_critere_recherche_vehicule, spinner_critere_recherche_objet;

    private EditText EditText_mot_cle;

    private Button btn_search;


    private CustomSpinnerAdapter customSpinnerAdapter;
    private List<DocType> docTypeList;
    private DocType selectedDoctype;


    private SearchCriteria searchCriteria;

    public static String searchData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        thisActivity = this;

        searchCriteria = (SearchCriteria) getIntent().getExtras().get(Constant.SEARCH_CRITERIA);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        spinner_critere_recherche_individu = (AppCompatSpinner) findViewById(R.id.spinner_critere_recherche_individu);

        docTypeList = new ArrayList<DocType>();
        customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), docTypeList);
        spinner_critere_recherche_individu.setAdapter(customSpinnerAdapter);

        spinner_critere_recherche_vehicule = (AppCompatSpinner) findViewById(R.id.spinner_critere_recherche_vehicule);

        docTypeList = new ArrayList<DocType>();
        customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), docTypeList);
        spinner_critere_recherche_vehicule.setAdapter(customSpinnerAdapter);

        spinner_critere_recherche_objet = (AppCompatSpinner) findViewById(R.id.spinner_critere_recherche_objet);

        docTypeList = new ArrayList<DocType>();
        customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), docTypeList);
        spinner_critere_recherche_objet.setAdapter(customSpinnerAdapter);



        spinner_critere_recherche_individu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDoctype = docTypeList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_critere_recherche_vehicule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDoctype = docTypeList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_critere_recherche_objet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDoctype = docTypeList.get(i);
                /*selectedIndex = i;
                selectedString = spinner_critere_recherche_objet.getSelectedItem().toString();
                Log.e("selectedString", selectedString);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText_mot_cle = (EditText) findViewById(R.id.EditText_mot_cle);

        correspondance = Constant.getSearchTypeValue();

        Bundle bundle = getIntent().getExtras();
        keyType = bundle.getInt(Constant.TYPE);

        type_shower = (TextView) findViewById(R.id.type_shower);
        type_shower.setText(correspondance.get("" + keyType));


        if (keyType == Constant.SEARCH_INDIVIDU){
            //Toast.makeText(getApplicationContext(), "enter", Toast.LENGTH_LONG).show();
            hideOther(R.id.spinner_critere_recherche_individu);
            object = "individu";
            if (mapDocType.get("" + Constant.SEARCH_INDIVIDU) == null){
                loadDataIntoSpinner(searchCriteria.getIndividuLinkCriteria(), spinner_critere_recherche_individu, "" + Constant.SEARCH_INDIVIDU);
            }else{
                docTypeList = mapDocType.get("" + Constant.SEARCH_INDIVIDU);
                customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), docTypeList);
                spinner_critere_recherche_individu.setAdapter(customSpinnerAdapter);
                selectedDoctype = docTypeList.get(0);
            }

        }

        if (keyType == Constant.SEARCH_VEHICULE){
            hideOther(R.id.spinner_critere_recherche_vehicule);
            object = "vehicule";
            if (mapDocType.get("" + Constant.SEARCH_VEHICULE) == null){
                loadDataIntoSpinner(Constant.searchCriteria.getVehiculeLinkCriteria(), spinner_critere_recherche_vehicule, "" + Constant.SEARCH_VEHICULE);
            }else {
                docTypeList = mapDocType.get("" + Constant.SEARCH_VEHICULE);
                customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), docTypeList);
                spinner_critere_recherche_vehicule.setAdapter(customSpinnerAdapter);
                selectedDoctype = docTypeList.get(0);
            }

        }

        if (keyType == Constant.SEARCH_OBJECT){
            hideOther(R.id.spinner_critere_recherche_objet);
            object = "objet";
            if (mapDocType.get("" + Constant.SEARCH_OBJECT) == null){
                loadDataIntoSpinner(Constant.searchCriteria.getObjectLinkCriteria(), spinner_critere_recherche_objet, "" + Constant.SEARCH_OBJECT);
            }else {
                docTypeList = mapDocType.get("" + Constant.SEARCH_OBJECT);
                customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), docTypeList);
                spinner_critere_recherche_objet.setAdapter(customSpinnerAdapter);
                selectedDoctype = docTypeList.get(0);
            }

        }

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.isInternetOn(getApplicationContext())){
                    submitForm();
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private void loadDataIntoSpinner(String ressource, final AppCompatSpinner spinner, final String key) {

        Credentials credentials = Credentials.getInstance(getApplicationContext());
        String query = "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword();

        (new AsyncTask<String, Void, String> (){
            private AlertDialog dialog;
            @Override
            protected String doInBackground(String... params) {

                String url_string = params[0];

                Log.e("urlllllllllllll", url_string);
                String returnVal = "";
                // String query = params[1];
                // Log.e("query", query);
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

                    String upLoadServerUri =url_string; /*"http://idea-cm.club/magasino/enregistrement.php";*/

                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs

                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    InputStream in = conn.getInputStream();

                    if (serverResponseCode >= 200 && serverResponseCode < 300) {

                        Log.e("5050505050","Log.e(\"5050505050\",\"55555555555555555555555555555555555555555555555\");");
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
                        //os.close();
                        returnVal = sb.toString();

                    }else {
                        Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                        returnVal =  Constant.KO ;
                    }


                } catch (MalformedURLException e) {

                    // dialog.dismiss();
                    e.printStackTrace();
                    return Constant.KO;

                } catch (ProtocolException e) {
                    e.printStackTrace();
                    return Constant.KO ;
                } catch (IOException e) {
                    e.printStackTrace();
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

                if (result.equals(Constant.KO)){

                }else{
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        docTypeList = new ArrayList<DocType>();
                        if (jsonArray.length() > 0){
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                DocType docType = new DocType(jsonObject.getString("id"), jsonObject.getString("libele"));
                                docTypeList.add(docType);
                            }

                            selectedDoctype = docTypeList.get(0);
                            customSpinnerAdapter = new CustomSpinnerAdapter(getApplicationContext(), docTypeList);
                            spinner.setAdapter(customSpinnerAdapter);
                            mapDocType.put(key, docTypeList);
                            Log.e("KEYYYYYYYYYYYY", key);
                            Log.e("mapDocType", mapDocType.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new SpotsDialog(SearchActivity.thisActivity);
                dialog.setMessage(getResources().getString(R.string.chargement));
                dialog.show();

            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }
        }).execute(ressource + query);
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

        //username=admin&password=admin&object=individu&option=nom_prenom&nom=ABA&prenom=aba

        Credentials credentials = Credentials.getInstance(this);

        String query = "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword() ;

        query += "&" + Constant.OBJECT + "=" + object;
        try {
            query += "&" + Constant.OPTION + "=" + selectedDoctype.getId();
            query += "&" + Constant.KEY_WORD + "=" + URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return  query;
    }

    public void sendRequest(){
        String query = makeQuery();
        Researcher researcher = new Researcher();
        researcher.execute(Constant.URL_LINK + Constant.RESEARCH_PATH, query);
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
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(url_str + query);
                Log.e("URLLLLLLL", url.toString());
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

                url = new URL(url_str + query);


                Log.e("URL STRING RESEARCHER", url.toString());
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


            Log.e("result log 11111", result);

            if (result.equals(Constant.KO)){
                //error_message.setVisibility(View.VISIBLE);
                //error_message.setText(R.string.connection_error);
                Toast.makeText(SearchActivity.this, getString(R.string.an_error_occure), Toast.LENGTH_SHORT).show();
                return;
            }


            Intent intent = new Intent(getApplicationContext(), ResultSearchLiteActivity.class);
            /*String resultat = null;
            try {

                String fileName = (keyType == Constant.SEARCH_INDIVIDU)? "individu.json":(keyType == Constant.SEARCH_VEHICULE)? "individu.json":"individu.json";
                InputStream inputStream  =  thisActivity.getAssets().open(fileName);
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

            Log.e("RISULTATOOOOOOOOOOOOO", resultat);*/
            //intent.putExtra(Constant.RESULT, result);
            searchData = result;
            //intent.putExtra(Constant.DOMAINE,  Constant.SEARCH_INDIVIDU);

            intent.putExtra(Constant.DOMAINE,  keyType);
            intent.putExtra(Constant.SEARCH_REULT_DISPLAY_VALUE, Constant.getSearchTypeValue().get("" + keyType));

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
