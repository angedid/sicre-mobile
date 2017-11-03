package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.pkmmte.view.CircularImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cm.mindef.sed.sicre.mobile.adapters.ViewPagerAdapter;
import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.fragments.AlertFragment;
import cm.mindef.sed.sicre.mobile.fragments.ChercherFragment;
import cm.mindef.sed.sicre.mobile.fragments.EnregistrementFragment;
import cm.mindef.sed.sicre.mobile.fragments.PerquisitionFragment;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;
import dmax.dialog.SpotsDialog;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class HomeActivity extends AppCompatActivity {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    public static AppCompatActivity thisActivity;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Toolbar toolbar;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        thisActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationview) ;

        user = (User) getIntent().getExtras().get(Constant.USER);

        Log.e("USER", user.getUsername());


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_IDLE){

                }else if (state == SCROLL_STATE_DRAGGING){

                }else if (state == SCROLL_STATE_SETTLING){

                }
            }
        });

        viewPager.setOffscreenPageLimit(1);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.icons8_searchlight_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.icons8_perquisition_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.icons8_registration_filled_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.icons8_google_alerts_24);
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

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //LayoutInflater li=(LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //View view = li.inflate(R.layout.menu_drawer_header, null);

                /*SharedPreferences prefs = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
                String username = prefs.getString(Constant.USERNAME, null);
                String password = prefs.getString(Constant.PASSWORD, null);*/

                Credentials credentials = Credentials.getInstance(getApplicationContext());


                if (credentials.getUsername() == null || credentials.getPassword() == null) {
                    Log.e("BAD CREDENTIALS", credentials.getUsername());
                    Menu menu = mNavigationView.getMenu();
                    int size = menu.size();
                    for (int i = 0 ; i<size; i++){
                        MenuItem item = menu.getItem(i);
                        item.setVisible(false);
                    }
                    return;
                }
                Menu menu = mNavigationView.getMenu();
                int size = menu.size();
                for (int i = 0 ; i<size; i++){
                    MenuItem item = menu.getItem(i);
                    item.setVisible(true);
                }
                View header = mNavigationView.getHeaderView(0);
                TextView connected_user_name =  header.findViewById(R.id.connected_user_name);
                String string = credentials.getUsername();
                connected_user_name.setText(string);

                final CircularImageView circularImageView = header.findViewById(R.id.drawer_menu_header_photo);

               /* (new AsyncTask<String, Integer, String>(){

                    private AlertDialog dialog;


                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = new SpotsDialog(HomeActivity.thisActivity);
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

                        circularImageView.


                    }
                    }).execute(user.getPhotoUrl());*/

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

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChercherFragment(), getString(R.string.recherche));
        adapter.addFragment(new PerquisitionFragment(), getString(R.string.perquisition));
        adapter.addFragment(new EnregistrementFragment(), getString(R.string.enregistrement));
        adapter.addFragment(new AlertFragment(), getString(R.string.alert));
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onStop(){

        super.onStop();
    }



}
