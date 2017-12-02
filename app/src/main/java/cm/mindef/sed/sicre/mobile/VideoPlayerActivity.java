package cm.mindef.sed.sicre.mobile;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;



import cm.mindef.sed.sicre.mobile.utils.Constant;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SETTINGS;
import static cm.mindef.sed.sicre.mobile.RecorderAudioActivity.RequestPermissionCode;

public class VideoPlayerActivity extends AppCompatActivity implements /* SurfaceHolder.Callback,*/ MediaPlayer.OnPreparedListener
{

    private String url ;

    private VideoView myVideo;
    private MediaController vidControl;
    private LinearLayout loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_video_player);
        url = getIntent().getExtras().getString(Constant.VIDEO_LINK);
        Uri vidUri = Uri.parse(url);

        /*surfView = (SurfaceView) findViewById(R.id.surfView);
        vidHolder = surfView.getHolder();
        vidHolder.addCallback(this);*/

        loader = (LinearLayout) findViewById(R.id.loader);
        if (loader.getVisibility() == View.GONE){
            loader.setVisibility(View.VISIBLE);
        }

        myVideo = (VideoView) findViewById(R.id.myVideo);
        myVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       if (loader.getVisibility() == View.VISIBLE){
                           loader.setVisibility(View.GONE);
                       }
                   }
               });
                return false;
            }
        });
        vidControl = new MediaController(this);
        myVideo.setVideoURI(vidUri);
        vidControl.setAnchorView(myVideo);
        myVideo.setMediaController(vidControl);

        myVideo.setOnPreparedListener(this);

        myVideo.start();
    }

    @Override
    public void onPause(){
        if (loader.getVisibility() == View.VISIBLE){
            loader.setVisibility(View.GONE);
        }

        if (myVideo != null){
            myVideo.pause();
        }
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStop(){
        if (myVideo != null){
            myVideo.stopPlayback();
        }

        if (loader.getVisibility() == View.VISIBLE){
            loader.setVisibility(View.GONE);
        }
        super.onStop();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (loader.getVisibility() == View.VISIBLE){
            loader.setVisibility(View.GONE);
        }
        myVideo.start();
    }

    /*@Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(vidHolder);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .build();

            mediaPlayer.setAudioAttributes(audioAttributes);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }*/
}
