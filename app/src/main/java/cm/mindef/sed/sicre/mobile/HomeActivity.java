package cm.mindef.sed.sicre.mobile;

import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cm.mindef.sed.sicre.mobile.adapters.ViewPagerAdapter;
import cm.mindef.sed.sicre.mobile.fragments.AlertFragment;
import cm.mindef.sed.sicre.mobile.fragments.ChercherFragment;
import cm.mindef.sed.sicre.mobile.fragments.EnregistrementFragment;
import cm.mindef.sed.sicre.mobile.fragments.PerquisitionFragment;
import cm.mindef.sed.sicre.mobile.utils.Constant;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        thisActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationview) ;


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
                    SharedPreferences prefs = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
                    String stay_connected = prefs.getString(Constant.STAY_CONNECTED, null);
                    //if (stay_connected == null){
                        SharedPreferences.Editor editor = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE).edit();
                        editor.remove(Constant.USERNAME);
                        editor.remove(Constant.PASSWORD);
                        editor.remove(Constant.STAY_CONNECTED);
                   // }else{

                    //}
                    finish();
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

                SharedPreferences prefs = getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
                String username = prefs.getString(Constant.USERNAME, null);
                String password = prefs.getString(Constant.PASSWORD, null);


                if (username == null || password == null) {
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
                String string = username;
                connected_user_name.setText(string);

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



}
