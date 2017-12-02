package cm.mindef.sed.sicre.mobile.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cm.mindef.sed.sicre.mobile.AlerterActivity;
import cm.mindef.sed.sicre.mobile.HomeActivity;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.domain.Actu;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;


public class AlertFragment extends Fragment {

    private static View rootView;

    private boolean isViewShown = false;

    private static HomeActivity homeActivity;

    private FloatingActionButton btn_ajouter;


    private ListView listViewActu;
    private List<Actu> actus;
    private ListActuAdapter adapter;

    private SwipeRefreshLayout swipeContainer;


    public AlertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        rootView = view;

        if (!isViewShown) {

            fetchData();
        }
        return view;
    }

    private void fetchData() {

        homeActivity = (HomeActivity) getActivity();
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


        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {
                GetRecentActu getRecentActu = new GetRecentActu(getActivity().getApplicationContext());
                Credentials credentials = Credentials.getInstance(getActivity().getApplicationContext());
                String query = null;
                query = "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                        Constant.PASSWORD + "=" + credentials.getPassword();
                HomeActivity ha = (HomeActivity) getActivity();
                String ressource = ha.getUser().getAlert().getRead().getUrlResource() +  "/";
                Log.e("ressource", ressource);
                getRecentActu.execute(ressource + query);

            }

        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);
        listViewActu = (ListView) rootView.findViewById(R.id.news_list);

       // if (Util.news == null){
            GetRecentActu getRecentActu = new GetRecentActu(getActivity().getApplicationContext());
        Credentials credentials = Credentials.getInstance(getActivity().getApplicationContext());
        String query = null;
        query = "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                Constant.PASSWORD + "=" + credentials.getPassword();

        HomeActivity ha = (HomeActivity) getActivity();
        String ressource = ha.getUser().getAlert().getRead().getUrlResource() +  "/";
        Log.e("ressource", ressource);
            getRecentActu.execute(ressource + query);
        /*}else {
            actus = Util.news;
            adapter = new ListActuAdapter(getActivity().getApplicationContext(), actus);
            listViewActu.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }*/

        btn_ajouter = rootView.findViewById(R.id.btn_ajouter);
        btn_ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AlerterActivity.class);
                HomeActivity ha = (HomeActivity) getActivity();
                intent.putExtra(Constant.USER, ha.getUser());
                startActivity(intent);
            }
        });
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

                if (Constant.who_is_showing.equals("AlertFragment")){


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


    private class ListActuAdapter extends BaseAdapter {
        private Context context;
        private List<Actu> list;

        public ListActuAdapter(Context context, List<Actu> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int position) {
            return this.list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return this.list.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.news_list_item, null);
            }

            Actu actu = list.get(position);



            TextView actu_title = (TextView) convertView.findViewById(R.id.title);
            actu_title.setText(actu.getTitle());

            TextView actu_content = (TextView) convertView.findViewById(R.id.message);
            actu_content.setText(actu.getMessage());

            TextView actu_publisher = (TextView) convertView.findViewById(R.id.publisher);
            String[] date = actu.getDate().split("\\s");
            actu_publisher.setText("Publie par: " + actu.getPublisher() + "  le  " + date[0] + "  a  " + date[1] /*+ " Position(" + actu.getLatitude() + ", " + actu.getLongitude()*/) ;

            return convertView;

        }
    }

    private class GetRecentActu extends AsyncTask<String,Integer, String> {
       // private ProgressDialog dialog;
        private int returnedCode;
        private Context context;
        private View view;
        private ProgressBar progressBar;
        public GetRecentActu(Context context) {
            this.context = context;
            this.progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_actualite);
            //dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (this.progressBar.getVisibility() == View.GONE)
                this.progressBar.setVisibility(View.VISIBLE);
            // this.dialog.setMessage("Veuillez patienter");
            //dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultat = "";
            String urlString = strings[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    /*urlConnection.setDoOutput(true);
                    OutputStream os = urlConnection.getOutputStream();
                    OutputStreamWriter out = new OutputStreamWriter(os);
                    out.write(queryString);
                    out.close();*/
                    returnedCode = urlConnection.getResponseCode();

                    if(returnedCode == 200){
                        InputStream in = urlConnection.getInputStream();
                        BufferedReader br = null;
                        StringBuilder sb = new StringBuilder();
                        String line;
                        try {
                            br = new BufferedReader(new InputStreamReader(in));
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                        } catch (IOException e) {
                            return "Failed";
                        } finally {
                            if (br != null) {
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    return "Failed";
                                }
                            }
                        }
                        in.close();
                        //os.close();
                        resultat = sb.toString();
                    }else {
                        return "Failed";
                    }
                } catch (IOException e) {
                    return "Failed";
                }
            } catch (MalformedURLException e) {
                return "Failed" ;
            }

            return resultat;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute( result);
           /* if (dialog.isShowing()) {
                dialog.dismiss();
            }*/
            if (this.progressBar.getVisibility() == View.VISIBLE)
                this.progressBar.setVisibility(View.GONE);
            swipeContainer.setRefreshing(false);
            Log.e("result log alertFragmen", returnedCode + "\n\n" + result);
            String returnedMessage = "Déconnecté avec succès";
            if (result.equals("Failed")){
                returnedMessage = "Echec de chargement: ";
                if (returnedCode == 401) returnedMessage += "Non autorisé";
                if (returnedCode == 400) returnedMessage += "Mauvaise Requete";
                if (returnedCode == 404) returnedMessage += "Ressource non trouvée";
                //Toast.makeText(getActivity().getApplicationContext(), result + "\n\n" + returnedMessage, Toast.LENGTH_LONG).show();
            }else{
                JSONObject jsonObject = null;
                try {
                    //jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(result);//jsonObject.getJSONArray("regions");

                    List<Actu> actuList = new ArrayList<Actu>();

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject job = jsonArray.getJSONObject(i);


                        Actu a = new Actu(
                                (!job.isNull("id"))?job.getInt("id"):-1,
                                (!job.isNull("title"))?job.getString("title"):"",
                                (!job.isNull("message"))?job.getString("message"):"",
                                (!job.isNull("auteur"))?job.getString("auteur"):"",
                                (!job.isNull("date"))?job.getString("date"):"01/01/2000 01:01:00",
                                (!job.isNull("latitude"))?Double.parseDouble(job.getString("latitude")):0d,
                                (!job.isNull("longitude"))?Double.parseDouble(job.getString("longitude")):0d
                        );

                        /*Actu a = new Actu(
                                job.getInt("id"),
                                job.getString("title"),
                                job.getString("message"),
                                job.getString("auteur"),
                                job.getString("date"),
                                Double.parseDouble(job.getString("latitude")),
                                Double.parseDouble(job.getString("longitude"))
                        );*/
                        actuList.add(a);
                    }

                    if (actuList.size() > 0){
                        //Util.news = actuList;
                        actus = actuList;
                        adapter = new ListActuAdapter(getActivity().getApplicationContext(), actus);
                        listViewActu.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(HomeActivity.thisActivity, "Aucune Actualité trouvée", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), e.getMessage() + " | " + e.getCause(), Toast.LENGTH_LONG).show();
                    Log.e("result log alertFragmen", e.getMessage() + " | " + e.getCause());
                    Toast.makeText(getActivity().getApplicationContext(), "Echec: Erreur reseau...", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
