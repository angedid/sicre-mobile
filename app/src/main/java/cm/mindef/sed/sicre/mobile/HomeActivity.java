package cm.mindef.sed.sicre.mobile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.pkmmte.view.CircularImageView;

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

import cm.mindef.sed.sicre.mobile.adapters.ViewPagerAdapter;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.domain.SearchCriteria;
import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.fragments.AlertFragment;
import cm.mindef.sed.sicre.mobile.fragments.ChercherFragment;
import cm.mindef.sed.sicre.mobile.fragments.EnregistrementFragment;
import cm.mindef.sed.sicre.mobile.fragments.PerquisitionFragment;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;
import dmax.dialog.SpotsDialog;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static cm.mindef.sed.sicre.mobile.RecorderAudioActivity.RequestPermissionCode;

public class HomeActivity extends AppCompatActivity  implements LocationListener {

    public static final int ALERTER_CODE = 1;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    public static AppCompatActivity thisActivity;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Toolbar toolbar;

    private User user;
    public static SearchCriteria searchCriteria;

    private BroadcastReceiver mReceiver;
    private IntentFilter intentFilter;


    private LocationManager locationManager;

    private Location location;
    private String provider;

    private LocationService locationService;
    private boolean locationBound=false;

    private ServiceConnection locationConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocationBinder binder = (LocationService.LocationBinder) service;
            //get service
            locationService = binder.getService();
            //pass list
            /*if (perquisition == null){
                Bundle bundle = getActivity().getIntent().getExtras();
                perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
            }
            musicSrv.setList(perquisition.getAudioLinks());*/
            locationBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationBound = false;
        }
    };


    public User getUser() {
        return user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        bindService(intent, locationConnection, Context.BIND_AUTO_CREATE);
        startService(intent);

        thisActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationview);

        user = (User) getIntent().getExtras().get(Constant.USER);
        searchCriteria = (SearchCriteria) getIntent().getExtras().get(Constant.SEARCH_CRITERIA);

        Log.e("USER", user.getUsername());

//        Log.e("searchCriteria", searchCriteria.toString());



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_IDLE) {

                } else if (state == SCROLL_STATE_DRAGGING) {

                } else if (state == SCROLL_STATE_SETTLING) {

                }
            }
        });

        viewPager.setOffscreenPageLimit(1);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        int index = 0;
        tabLayout.getTabAt(index++).setIcon(R.drawable.icon_perquisition);

        if (user.getPerquisition().getRead().isCan()){
            tabLayout.getTabAt(index++).setIcon(R.drawable.icons8_perquisition_24);
        }

        if (user.getIndividu().getCreate().isCan() &&  user.getVehicule().getCreate().isCan() && user.getObjet().getCreate().isCan()){
            tabLayout.getTabAt(index++).setIcon(R.drawable.icons8_registration_filled_24);
        }

        if (user.getAlert().getRead().isCan()){
            tabLayout.getTabAt(index++).setIcon(R.drawable.icons8_google_alerts_24);
        }

        tabLayout.setSelectedTabIndicatorHeight(8);

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.deconnexion) {
                    Credentials.disconnect(getApplicationContext());
                    finish();
                    Log.e("deconnexion", "Logout ");
                }


                return false;
            }

        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //LayoutInflater li=(LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //View view = li.inflate(R.layout.menu_drawer_header, null);

                /*SharedPreferences prefs = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
                String username = prefs.getString(Constant.USERNAME, null);
                String password = prefs.getString(Constant.PASSWORD, null);*/

                Credentials credentials = Credentials.getInstance(getApplicationContext());

                if (credentials.getUsername() == null || credentials.getPassword() == null) {
                    // Log.e("BAD CREDENTIALS", credentials.getUsername());
                    Menu menu = mNavigationView.getMenu();
                    int size = menu.size();
                    for (int i = 0; i < size; i++) {
                        MenuItem item = menu.getItem(i);
                        item.setVisible(false);
                    }
                    return;
                }
                Menu menu = mNavigationView.getMenu();
                int size = menu.size();
                for (int i = 0; i < size; i++) {
                    MenuItem item = menu.getItem(i);
                    item.setVisible(true);
                }
                View header = mNavigationView.getHeaderView(0);
                TextView connected_user_name = header.findViewById(R.id.connected_user_name);
                String string = credentials.getUsername();
                connected_user_name.setText(string);

                final CircularImageView circularImageView = header.findViewById(R.id.drawer_menu_header_photo);


                RequestQueue requestQueue = MySingleton.getRequestQueue(getApplicationContext());

                // Initialize a new ImageRequest
                ImageRequest imageRequest = new ImageRequest(
                        user.getPhotoUrl() + "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword(), // Image URL
                        new Response.Listener<Bitmap>() { // Bitmap listener
                            @Override
                            public void onResponse(Bitmap response) {
                                // Do something with response
                                //Log.e("SETTING IMAGE", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                                circularImageView.setImageBitmap(response);
                                //circularImageView.setImageURI(new );
                            }
                        },
                        0, // Image width
                        0, // Image height
                        ImageView.ScaleType.CENTER_CROP, // Image scale type
                        Bitmap.Config.RGB_565, //Image decode configuration
                        new Response.ErrorListener() { // Error listener
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERROR GET USER PHOTO", error.toString());
                                // Do something with error response
                                // error.printStackTrace();
                                // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                            }
                        }
                );

                // Add ImageRequest to the RequestQueue
                requestQueue.add(imageRequest);


                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.icons8_menu_24);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        FirebaseInstanceId.getInstance().getToken();

        if (getIntent().getExtras()!= null && getIntent().getExtras().get(Constant.REMOTE_MESSAGE) != null){
            Intent intention = new Intent(getApplicationContext(), NotificationActivity.class);
            intention.putExtra(Constant.REMOTE_MESSAGE, (RemoteMessage) getIntent().getExtras().get(Constant.REMOTE_MESSAGE));
            //User user = (User) getIntent().getExtras().get(Constant.USER);
            intention.putExtra(Constant.USER, (User) getIntent().getExtras().get(Constant.USER));
            startActivity(intention);
            Log.e("NKALLA222", "Nkalla2222222222222222222222222222222222");
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

                    SharedPreferences prefs = getSharedPreferences(Constant.TOKEN, MODE_PRIVATE);
                    String token = prefs.getString(Constant.TOKEN, null);
                    if (token != null){
                        //Credentials credentials = Credentials.getInstance(getApplicationContext());
                        sendToken(token);
                    }

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



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChercherFragment(), getString(R.string.recherche));
        if (user.getPerquisition().getRead().isCan()){
            adapter.addFragment(new PerquisitionFragment(), getString(R.string.perquisition));
        }
        if (user.getIndividu().getCreate().isCan() &&  user.getVehicule().getCreate().isCan() && user.getObjet().getCreate().isCan()){
            adapter.addFragment(new EnregistrementFragment(), getString(R.string.enregistrement));
        }

        if (user.getAlert().getRead().isCan()){
            adapter.addFragment(new AlertFragment(), getString(R.string.alert));
        }

        viewPager.setAdapter(adapter);
    }

        /*
        int index = 0;

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ChercherFragment(), getString(R.string.recherche));
        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(index++).setIcon(R.drawable.icons8_searchlight_24);

        if (user.getPerquisition().getRead().isCan()){
            adapter.addFragment(new PerquisitionFragment(), getString(R.string.perquisition));
            viewPager.setAdapter(adapter);
            tabLayout.getTabAt(index++).setIcon(R.drawable.icons8_perquisition_24);
        }

        if (user.getIndividu().getCreate().isCan() &&  user.getVehicule().getCreate().isCan() && user.getObjet().getCreate().isCan()){
            adapter.addFragment(new EnregistrementFragment(), getString(R.string.enregistrement));
            viewPager.setAdapter(adapter);
            tabLayout.getTabAt(index++).setIcon(R.drawable.icons8_registration_filled_24);
        }

        if (user.getAlert().getRead().isCan()){
            adapter.addFragment(new AlertFragment(), getString(R.string.alert));
            viewPager.setAdapter(adapter);
            tabLayout.getTabAt(index++).setIcon(R.drawable.icons8_google_alerts_24);
        }
        viewPager.setAdapter(adapter);
    }
    }*/


    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();



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

        intentFilter = new IntentFilter("android.intent.action.MAIN");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String token = intent.getStringExtra(Constant.TOKEN);//prefs.getString(Constant.TOKEN, null);
                sendToken(token);

            }
        };

        registerReceiver(mReceiver, intentFilter);

    }

    private void sendToken(String token){
        if (token != null){
            @SuppressLint("WifiManagerLeak") WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            String address = info.getMacAddress();
            Credentials credentials = Credentials.getInstance(getApplicationContext());
            String query = null;
            try {
                query =  Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                        Constant.PASSWORD + "=" + credentials.getPassword() + "&token=" +  URLEncoder.encode(token, "UTF-8")
                        + "&" + Constant.LATITUDE + "=" + Constant.latitudeNetwork
                        + "&" + Constant.LONGITUDE + "=" + Constant.longitudeNetwork + "&mac_address=" + address;
                (new UploadTokenAsync()).execute("http://198.50.199.116:8090/scriptcase/app/SICRE_2/m_alert/?", query);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private class UploadTokenAsync extends AsyncTask<String, Void, String> {
        private AlertDialog dialog;

        public UploadTokenAsync(){

            dialog = new SpotsDialog(HomeActivity.thisActivity);
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
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
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
                    Log.e("55555555555555555","55555555555555555555555555555555555555555555555");
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

            if (result.equals("1")){
                SharedPreferences.Editor editor = getSharedPreferences(Constant.TOKEN, MODE_PRIVATE).edit();
                editor.remove(Constant.TOKEN);
                editor.commit();
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

    @Override
    public void onDestroy() {
        stopService(new Intent(getApplicationContext(), LocationService.class));
        locationService=null;
        unbindService(locationConnection);
        super.onDestroy();

    }
}