package cm.mindef.sed.sicre.mobile;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import cm.mindef.sed.sicre.mobile.adapters.ViewPagerAdapter;
import cm.mindef.sed.sicre.mobile.fragments.EnregIndividuFragment;
import cm.mindef.sed.sicre.mobile.fragments.EnregObjetFragment;
import cm.mindef.sed.sicre.mobile.fragments.EnregVehiculeFragment;
import cm.mindef.sed.sicre.mobile.fragments.ImageFragment;
import cm.mindef.sed.sicre.mobile.fragments.SoundFragment;
import cm.mindef.sed.sicre.mobile.fragments.TextFragment;
import cm.mindef.sed.sicre.mobile.fragments.VideoFragment;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class AffaireActivity extends AppCompatActivity {

    public static AppCompatActivity thisActivity;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affaire);

        thisActivity = this;


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){
                    Toast.makeText(getApplicationContext(), "Selected index: " + (position + 1), Toast.LENGTH_LONG).show();
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

        tabLayout.getTabAt(0).setIcon(R.drawable.icons8_individu_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.icons8_vehicule_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.icons8_object_24);
        //tabLayout.getTabAt(3).setIcon(R.drawable.icons8_movie_24);
        tabLayout.setSelectedTabIndicatorHeight(8);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EnregIndividuFragment(), getString(R.string.individu));
        adapter.addFragment(new EnregVehiculeFragment(), getString(R.string.vehicule));
        adapter.addFragment(new EnregObjetFragment(), getString(R.string.autres));
        //adapter.addFragment(new VideoFragment(), getString(R.string.video));
        viewPager.setAdapter(adapter);
    }

}
