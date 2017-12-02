package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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

public class AutenticationActivity extends AppCompatActivity {

    private  static AutenticationActivity  thisActivity;
    private Toolbar toolbar;
    private EditText inputUsername, inputPassword;
    private TextInputLayout inputLayoutUserName, inputLayoutPassword;
    private Button btnSignUp;
    private TextView error_message;
    private CheckBox checkbox_stay_connected;
    //private LinearLayout wall_paper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_autentication);
        Constant.who_is_showing = getClass().getSimpleName();

        // RelativeLayout activity_main = (RelativeLayout)Logger.thisActivity.findViewById(R.id.activity_main); //context.getResources().getLayout(R.layout.activity_main).get;
        TextView networState_logger = (TextView) findViewById(R.id.networState_logger);

        // Button button = (Button) activity_main.findViewById(R.id.imageButton);

        if (Constant.isInternetOn(getApplicationContext())){
            networState_logger.setBackgroundColor(Color.rgb(0,200, 0));
            networState_logger.setTextColor(Color.WHITE);
            networState_logger.setText(getString(R.string.yes_internet));
            networState_logger.setVisibility(View.VISIBLE);

            final TextView tv = networState_logger;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv.setVisibility(View.GONE);
                }
            }, Constant.TIMEOUT);


        }else{

            networState_logger.setBackgroundColor(Color.rgb(200, 0 ,0));
            networState_logger.setTextColor(Color.WHITE);
            networState_logger.setText(getString(R.string.no_internet));
            networState_logger.setVisibility(View.VISIBLE);
        }

        //wall_paper = findViewById(R.id.wall_paper);
        //wall_paper.setVisibility(View.VISIBLE);


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
        checkbox_stay_connected = (CheckBox) findViewById(R.id.checkbox_stay_connected);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        error_message = (TextView) findViewById(R.id.error_message);

        inputUsername.addTextChangedListener(new MyTextWatcher(inputUsername));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInternetOn = Constant.isInternetOn(getApplicationContext());
                if (!isInternetOn){
                    Toast.makeText(getApplicationContext(), getString(R.string.verify_connection), Toast.LENGTH_LONG).show();
                    return;
                }
                error_message.setText("");
                error_message.setVisibility(View.GONE);
                submitForm();
            }
        });
        //intent.putExtra(Constant.USER, (User)bundle.get(Constant.USER));
        User user = (getIntent().getExtras() != null)?((User) getIntent().getExtras().get(Constant.USER)):null;
        if (user != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra(Constant.REMOTE_MESSAGE, (RemoteMessage) getIntent().getExtras().get(Constant.REMOTE_MESSAGE));
            intent.putExtra(Constant.USER, user);
            startActivity(intent);
        }
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

        String stayConnected = (checkbox_stay_connected.isChecked())? Constant.OK:Constant.KO;
        Authenticator authenticator = new Authenticator(inputUsername.getText().toString().trim(), inputPassword.getText().toString(), stayConnected);
        authenticator.execute(Constant.URL_LINK + Constant.LOGIN_PATH +  "?" + Constant.USERNAME + "=" + inputUsername.getText().toString().trim() +
                "&" + Constant.PASSWORD + "=" + inputPassword.getText().toString());
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

        if (inputPassword.getText().toString().isEmpty() /*|| inputPassword.getText().toString().length() < Constant.MIN_LENGTH_PASSWORD*/) {
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
                    //validatePassword();
                    break;
            }
        }
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
                error_message.setText(getString(R.string.an_error_occure));
                return;
            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(Constant.STATUS);
                if (status.equals(Constant.FAILED)){
                    error_message.setVisibility(View.VISIBLE);
                    error_message.setText(getString(R.string.connection_error));
                    return;
                }

                JSONObject userJsonObj = jsonObject.getJSONObject(Constant.USER);

                User user = User.getInstance(userJsonObj);

                if (user == null){
                    error_message.setVisibility(View.VISIBLE);
                    error_message.setText(getString(R.string.an_error_occure) + " voila" );
                    return;
                }


                JSONObject criteriaJsonObj = jsonObject.getJSONObject(Constant.SEARCH_CRITERIA);
                SearchCriteria searchCriteria = SearchCriteria.getInstance(criteriaJsonObj);

                Constant.searchCriteria = searchCriteria;

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

                if (checkbox_stay_connected.isChecked())
                    Credentials.saveCredential(getApplicationContext(), this.username, this.password, this.stayConnected);

                if (getIntent().getExtras()!= null && getIntent().getExtras().get(Constant.REMOTE_MESSAGE) != null){
                    intent.putExtra(Constant.REMOTE_MESSAGE, (RemoteMessage) getIntent().getExtras().get(Constant.REMOTE_MESSAGE));
                    User user1 = (User) getIntent().getExtras().get(Constant.USER);
                    intent.putExtra(Constant.USER, user1);
                    Log.e("NKALLA111", "Nkalla1111111111111111111111111");
                }

                startActivity(intent);
                inputPassword.setText("");
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

    public static class NetworkListener extends BroadcastReceiver {
        //private static final String TAG = "NetworkConnectivityListener";
        private NetworkInfo.State mState;
        private NetworkInfo mNetworkInfo;
        private NetworkInfo mOtherNetworkInfo;
        private String mReason;
        private boolean mIsFailover;
        private static final boolean DBG = true;
        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

                if (noConnectivity) {
                    mState = NetworkInfo.State.DISCONNECTED;
                } else {
                    mState = NetworkInfo.State.CONNECTED;
                }

                mNetworkInfo = (NetworkInfo)
                        intent.getParcelableExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
                mOtherNetworkInfo = (NetworkInfo)
                        intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

                mReason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
                mIsFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
                //context.getApplicationContext().getCon

                if (Constant.who_is_showing.equals("AutenticationActivity")){


                    // RelativeLayout activity_main = (RelativeLayout)Logger.thisActivity.findViewById(R.id.activity_main); //context.getResources().getLayout(R.layout.activity_main).get;
                    TextView networState_logger = (TextView) AutenticationActivity.thisActivity.findViewById(R.id.networState_logger);

                    // Button button = (Button) activity_main.findViewById(R.id.imageButton);

                    if (mState.toString().equals(Constant.CONNECTED)){
                        networState_logger.setBackgroundColor(Color.rgb(0,200, 0));
                        networState_logger.setTextColor(Color.WHITE);
                        networState_logger.setText(AutenticationActivity.thisActivity.getString(R.string.yes_internet));
                        networState_logger.setVisibility(View.VISIBLE);

                        final TextView tv = networState_logger;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tv.setVisibility(View.GONE);
                            }
                        }, Constant.TIMEOUT);


                    }else{

                        networState_logger.setBackgroundColor(Color.rgb(200, 0 ,0));
                        networState_logger.setTextColor(Color.WHITE);
                        networState_logger.setText(AutenticationActivity.thisActivity.getString(R.string.no_internet));
                        networState_logger.setVisibility(View.VISIBLE);
                    }

                    //View view = context.getResources().getLayout(R.layout.);
                    if (DBG) {
                        Log.e("ListenConnection", "Logger: onReceive(): mNetworkInfo=" + mNetworkInfo +  " mOtherNetworkInfo = "
                                + (mOtherNetworkInfo == null ? "[none]" : mOtherNetworkInfo +
                                " noConn=" + noConnectivity) + " mState=" + mState.toString());
                    }
                }

                //if (context.getClass() instanceof  )
            }

        }
    }
}


