package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import cm.mindef.sed.sicre.mobile.utils.Constant;

import dmax.dialog.SpotsDialog;

public class AutenticationActivity extends AppCompatActivity {

    private  static AutenticationActivity  thisActivity;
    private Toolbar toolbar;
    private EditText inputUsername, inputPassword;
    private TextInputLayout inputLayoutUserName, inputLayoutPassword;
    private Button btnSignUp;
    private TextView error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentication);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        String title = getSupportActionBar().getTitle().toString();
        Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
        ActionBar actionBar = getSupportActionBar();
        thisActivity = this;

        inputLayoutUserName = (TextInputLayout) findViewById(R.id.TextInputLayout_username);

        inputLayoutPassword = (TextInputLayout) findViewById(R.id.TextInputLayout_password);
        inputUsername = (EditText) findViewById(R.id.EditText_username);
        inputPassword = (EditText) findViewById(R.id.EditText_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        error_message = (TextView) findViewById(R.id.error_message);

        inputUsername.addTextChangedListener(new MyTextWatcher(inputUsername));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));


        SharedPreferences prefs = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
        String username = prefs.getString(Constant.USERNAME, null);
        String password = prefs.getString(Constant.PASSWORD, null);
        String stay_connected = prefs.getString(Constant.STAY_CONNECTED, null);
        if (username != null && password !=null && stay_connected != null){
            inputUsername.setText(username);
            inputPassword.setText(password);
            submitForm();
        }else{

        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_message.setText("");
                error_message.setVisibility(View.GONE);
                submitForm();
            }
        });
    }

    public void togleStayConnected(View view){
        CheckBox checkBox = (CheckBox) view;
        SharedPreferences.Editor editor = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE).edit();
        if (checkBox.isChecked()){
            editor.putString(Constant.STAY_CONNECTED, Constant.OK);
            editor.commit();
        }else{
            editor.remove(Constant.STAY_CONNECTED);
            editor.commit();
            SharedPreferences prefs = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
            String stay_connected = prefs.getString(Constant.STAY_CONNECTED, null);
            Toast.makeText(getApplicationContext(), "stay_connected: " + stay_connected, Toast.LENGTH_LONG).show();
        }



    }

    private void submitForm() {
        if (!validateUserName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Authenticator authenticator = new Authenticator(inputUsername.getText().toString().trim(), inputPassword.getText().toString());
        authenticator.execute(Constant.URL_LINK);
        //Toast.makeText(getApplicationContext(), "Thank You!: " + inputUsername.getText().toString() + " || " + inputPassword.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private boolean validateUserName() {
        if (inputUsername.getText().toString().trim().isEmpty()) {
            inputLayoutUserName.setError(getString(R.string.err_msg_username));
            requestFocus(inputUsername);
            return false;
        } else {
            inputLayoutUserName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().isEmpty() /*|| inputPassword.getText().toString().trim().length() < Constant.MIN_LENGTH_PASSWORD*/) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.EditText_username:
                    validateUserName();
                    break;
                case R.id.EditText_password:
                    validatePassword();
                    break;
            }
        }
    }


    private class Authenticator extends AsyncTask<String, Integer, String> {
        private AlertDialog dialog;
        private String username, password;
        public Authenticator(String username, String password) {
            this.username = username;
            this.password = password;

            dialog = new SpotsDialog(AutenticationActivity.thisActivity);
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
            }

            if (1==1) return Constant.KO;

            try {

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
                error_message.setVisibility(View.VISIBLE);
                error_message.setText(R.string.connection_error);
                return;
            }


            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra(Constant.USERNAME, this.username);
            intent.putExtra(Constant.PASSWORD, this.password);

            SharedPreferences.Editor editor = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE).edit();
            editor.putString(Constant.USERNAME, this.username);
            editor.putString(Constant.PASSWORD, this.password);
            editor.commit();

            startActivity(intent);
            inputPassword.setText("");

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


