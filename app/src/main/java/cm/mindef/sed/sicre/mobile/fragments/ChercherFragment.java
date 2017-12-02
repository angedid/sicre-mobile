package cm.mindef.sed.sicre.mobile.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cm.mindef.sed.sicre.mobile.AutenticationActivity;
import cm.mindef.sed.sicre.mobile.HomeActivity;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.SearchActivity;
import cm.mindef.sed.sicre.mobile.domain.DocType;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChercherFragment extends Fragment {


    private static View fragmentView;

    private Button btn_chercher_individu, btn_chercher_vehicule, btn_chercher_objet;

    private static AppCompatActivity homeActivity;

    private boolean isViewShown = false;


    public static Map<String, List<DocType>> mapDocType;

    public ChercherFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_chercher, container, false);
        fragmentView = view;

        if (!isViewShown) {

            fetchData();
        }

        if (mapDocType == null){
            mapDocType = new HashMap<>();
        }

        return view;
    }

    private void fetchData() {

        homeActivity = (AppCompatActivity) getActivity();
        Constant.who_is_showing = getClass().getSimpleName();

        TextView networState_logger = (TextView) fragmentView.findViewById(R.id.networState_logger);

        // Button button = (Button) activity_main.findViewById(R.id.imageButton);

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
        btn_chercher_individu = (Button) fragmentView.findViewById(R.id.btn_chercher_individu);
        btn_chercher_vehicule = (Button) fragmentView.findViewById(R.id.btn_chercher_vehicule);
        btn_chercher_objet = (Button) fragmentView.findViewById(R.id.btn_chercher_objet);

        btn_chercher_individu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSearchInterface(view);
            }
        });


        btn_chercher_vehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSearchInterface(view);
            }
        });

        btn_chercher_objet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSearchInterface(view);
            }
        });
    }

    public void gotoSearchInterface(View view){
        int id = view.getId();
        int type = -1;
        switch (id){
            case R.id.btn_chercher_individu:
                type =  Constant.SEARCH_INDIVIDU;
                break;
            case R.id.btn_chercher_vehicule:
                type =  Constant.SEARCH_VEHICULE;
                break;
            case R.id.btn_chercher_objet:
                type = Constant.SEARCH_OBJECT;
                break;
            default:
                break;
        }
        if (type == -1){
            Toast.makeText(getActivity().getApplicationContext(), "Mauvaise action...", Toast.LENGTH_LONG).show();
            
        }else {
            Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
            intent.putExtra(Constant.TYPE, type);
            //HomeActivity homeActivity = (HomeActivity)getActivity();
            intent.putExtra(Constant.SEARCH_CRITERIA, HomeActivity.searchCriteria);
            startActivity(intent);
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

                if (Constant.who_is_showing.equals("ChercherFragment")){


                    // RelativeLayout activity_main = (RelativeLayout)Logger.thisActivity.findViewById(R.id.activity_main); //context.getResources().getLayout(R.layout.activity_main).get;
                    TextView networState_logger = (TextView) fragmentView.findViewById(R.id.networState_logger);

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
}
