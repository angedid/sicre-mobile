package cm.mindef.sed.sicre.mobile.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import cm.mindef.sed.sicre.mobile.AddAffaireActivity;
import cm.mindef.sed.sicre.mobile.AffaireActivity;
import cm.mindef.sed.sicre.mobile.EnregistrerAffaireActivity;
import cm.mindef.sed.sicre.mobile.EnregistrerIndividuActivity;
import cm.mindef.sed.sicre.mobile.EnregistrerObjetActivity;
import cm.mindef.sed.sicre.mobile.EnregistrerVehiculeActivity;
import cm.mindef.sed.sicre.mobile.HomeActivity;
import cm.mindef.sed.sicre.mobile.PerquisitionActivity;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.adapters.AffaireAdapter;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionAdapter;
import cm.mindef.sed.sicre.mobile.domain.Affaire;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;


//https://medium.com/android-bits/android-security-tip-public-key-pinning-with-volley-library-fb85bf761857

public class EnregistrementFragment extends Fragment {


    private static View rootView;
    private RequestQueue queue;
    private Button btn_add_affaire;

    //private StringRequest stringRequest;
    /*private JsonArrayRequest jsonArrayRequest;
    private JSONArray jsonArrayResult;
    private String url;*/

    private ProgressBar loader;

    /*private List<Affaire> affaires;
    private AffaireAdapter affaireAdapter;
    private ListView listViewAffaire;
    private TextView liste_affaire_title;*/

    private  Button btn_enregistrer_individu, btn_enregistrer_vehicule, btn_enregistrer_objet;
            //btn_enregistrer_affaire;

    private boolean isViewShown = false;

    private static AppCompatActivity homeActivity;


    public EnregistrementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enregistrement1, container, false);
        rootView = view;

        if (!isViewShown) {
            //queue = MySingleton.getRequestQueue(this.getActivity().getApplicationContext());
            fetchData();
        }

        return view ;
    }

    private void fetchData() {


        homeActivity = (AppCompatActivity) getActivity();
        Constant.who_is_showing = getClass().getSimpleName();

        TextView networState_logger = (TextView) rootView.findViewById(R.id.networState_logger);


        if (Constant.isInternetOn(getActivity().getApplicationContext())){
            networState_logger.setBackgroundColor(Color.rgb(0,200, 0));
            networState_logger.setTextColor(Color.WHITE);
            networState_logger.setText(homeActivity.getString(R.string.yes_internet));
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
            networState_logger.setText(homeActivity.getString(R.string.no_internet));
            networState_logger.setVisibility(View.VISIBLE);
        }

        /*listViewAffaire = rootView.findViewById(R.id.liste_affaire);
        liste_affaire_title = rootView.findViewById(R.id.liste_affaire_title);
        liste_affaire_title.setText(getString(R.string.liste_affaire_title)+ " de " + Credentials.getInstance(getActivity().getApplicationContext()).getUsername());
        url ="https://jsonplaceholder.typicode.com/posts/1";
        loader = rootView.findViewById(R.id.loader);
        btn_add_affaire = rootView.findViewById(R.id.btn_add_affaire);
        btn_add_affaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(), AddAffaireActivity.class);
                startActivity(intent);


            }
        });*/

        /*SSLSocketFactory sslSocketFactory = getMySSLSocketFactory();

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack()); // For SSLCertificate pinning use Network network = new BasicNetwork(new HurlStack(null, sslSocketFactory));

        // Instantiate the RequestQueue with the cache and network.
        queue = new RequestQueue(cache, network);


        // Start the queue
        queue.start();*/


        //queue = Volley.newRequestQueue(this.getActivity());

        //Set<Perquisition> perquisitions = (Set<Perquisition>) Constant.data.get(Constant.PERQUISITION_LIST);
        //if (perquisitions == null){

       // Toast.makeText(getActivity().getApplicationContext(), "Entrer!", Toast.LENGTH_LONG).show();

        //String url ="http://www.example.com";

// Formulate the request and handle the response.
           /* StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://pridesoft.armp.cm/mobile/mlog.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity().getApplicationContext(), response.substring(0, response.length()), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), "Geting in troubles ...", Toast.LENGTH_LONG).show();
                        }
                    });
            queue.add(stringRequest);*/


       /* if (loader.getVisibility() == View.GONE){
            loader.setVisibility(View.VISIBLE);
        }

        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                jsonArrayResult = response;
                Toast.makeText(getActivity().getApplication(), response.toString(), Toast.LENGTH_LONG).show();
                if (loader.getVisibility() == View.VISIBLE){
                    loader.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getActivity().getApplication(), "Error...", Toast.LENGTH_LONG).show();
                        Log.e("ERRORER", error.getCause() + " | " + error.getStackTrace() + " | " + error.getMessage());
                        if (loader.getVisibility() == View.VISIBLE){
                            loader.setVisibility(View.GONE);
                        }


                        loadAffaire();

                    }
                }
        );

        jsonArrayRequest.setTag(Constant.AFFAIRE_LIST_REQUEST_TAG);

// Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);*/


        // }else {
        //loadPerquisitionsList();
        // }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public void onResume() {
        super.onResume();
        btn_enregistrer_individu = (Button) rootView.findViewById(R.id.btn_enregistrer_individu);
        btn_enregistrer_vehicule = (Button) rootView.findViewById(R.id.btn_enregistrer_vehicule);
        btn_enregistrer_objet = (Button) rootView.findViewById(R.id.btn_enregistrer_objet);
        //btn_enregistrer_affaire = (Button) rootView.findViewById(R.id.btn_enregistrer_affaire);

        btn_enregistrer_individu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EnregistrerIndividuActivity.class);
                HomeActivity ha = (HomeActivity) getActivity();
                User user = ha.getUser();
                intent.putExtra(Constant.USER, user);
                startActivity(intent);
            }
        });


        btn_enregistrer_vehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EnregistrerVehiculeActivity.class);
                HomeActivity ha = (HomeActivity) getActivity();
                User user = ha.getUser();
                intent.putExtra(Constant.USER, user);
                startActivity(intent);
            }
        });

        btn_enregistrer_objet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EnregistrerObjetActivity.class);
                HomeActivity ha = (HomeActivity) getActivity();
                User user = ha.getUser();
                intent.putExtra(Constant.USER, user);
                startActivity(intent);
            }
        });

        /*btn_enregistrer_affaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EnregistrerAffaireActivity.class);
                startActivity(intent);
            }
        });*/
    }

/*

    private void loadAffaire() {
        String resultat = null;
        try {
            InputStream inputStream  =  getActivity().getAssets().open("affaires.json");
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

        try {
            Log.e("RESULTAT AFFAIRE", resultat);
            JSONArray jsonarr = new JSONArray(resultat);
            affaires = new ArrayList<>();
            for (int i=0; i<jsonarr.length(); i++){
                affaires.add(Affaire.getInstance(jsonarr.getJSONObject(i)));
            }

            Log.e("outer AFFAIRE SIZE " , "" + affaires.size());

            if (affaires.size() > 0){
                Log.e("innet AFFAIRE SIZE " , "" + affaires.size());
                affaireAdapter = new AffaireAdapter(getActivity().getApplicationContext(), affaires);
                listViewAffaire.setAdapter(affaireAdapter);

                listViewAffaire.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Affaire affaire = affaires.get(position);

                        Intent intent = new Intent(getActivity().getApplicationContext(), AffaireActivity.class);
                        intent.putExtra(Constant.AFFAIRE, affaire);

                        startActivity(intent);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/

    /**
     * Pour SSl Certificate pinning
     * @return
     */

    public SSLSocketFactory getMySSLSocketFactory(){
        SSLContext context = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput  =  getActivity().getAssets().open("sicre.crt");
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

            HttpsURLConnection.setDefaultHostnameVerifier(new MyNullHostNameVerifier());

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            return context.getSocketFactory();

        }catch (CertificateException e){
            return null;
        } catch (IOException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (KeyStoreException e) {
            return null;
        } catch (KeyManagementException e) {
            return null;
        }

    }


    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            return null;
            // Error while creating file
        }
        return file;
    }


    private class MyNullHostNameVerifier implements HostnameVerifier {

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

                if (Constant.who_is_showing.equals("EnregistrementFragment")){


                    // RelativeLayout activity_main = (RelativeLayout)Logger.thisActivity.findViewById(R.id.activity_main); //context.getResources().getLayout(R.layout.activity_main).get;
                    TextView networState_logger = (TextView) rootView.findViewById(R.id.networState_logger);

                    // Button button = (Button) activity_main.findViewById(R.id.imageButton);

                    if (mState.toString().equals(Constant.CONNECTED)){
                        networState_logger.setBackgroundColor(Color.rgb(0,200, 0));
                        networState_logger.setTextColor(Color.WHITE);
                        networState_logger.setText(homeActivity.getString(R.string.yes_internet));
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
                        networState_logger.setText(homeActivity.getString(R.string.no_internet));
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
