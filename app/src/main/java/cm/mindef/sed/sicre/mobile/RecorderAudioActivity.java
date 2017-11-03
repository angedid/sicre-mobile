package cm.mindef.sed.sicre.mobile;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.views.VisualizerView;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecorderAudioActivity extends AppCompatActivity {

    private VisualizerView visualizer;

    private TextView enregistrement_en_cours;

    private ImageButton record_or_stop, play_record, stop_play_record;
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder ;
    //private Random random ;
    //private String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    private MediaPlayer mediaPlayer ;
    private Perquisition perquisition;

    private boolean isRecording = false;

    private boolean isStopped;

    private Handler handler;

    //private Toolbar toolbar;
    public static final int REPEAT_INTERVAL = 40;

    private Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isRecording) // if we are already recording
            {
                // get the current amplitude
                int x = mediaRecorder.getMaxAmplitude();
                visualizer.addAmplitude(x); // update the VisualizeView
                visualizer.invalidate(); // refresh the VisualizerView

                // update in 40 milliseconds
                handler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_audio);

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/

        perquisition = (Perquisition) getIntent().getExtras().get(Constant.PERQUISITION);

        record_or_stop =  findViewById(R.id.record_or_stop);
        play_record =  findViewById(R.id.play_record);
        stop_play_record =  findViewById(R.id.stop_play_record);
        visualizer = findViewById(R.id.visualizer);
        enregistrement_en_cours = findViewById(R.id.enregistrement_en_cours);

        //buttonStopPlayingRecording = (Button)findViewById(R.id.button4);

        record_or_stop.setImageResource(R.drawable.icons8_microphone_60);
        play_record.setVisibility(View.INVISIBLE);
        stop_play_record.setVisibility(View.INVISIBLE);
        //play_record.setEnabled(false);
        //stop_play_record.setEnabled(false);

        //random = new Random();
        if(checkPermission()) {
        } else {
            requestPermission();
        }

        record_or_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isRecording == false){
                    isRecording = true;
                    enregistrement_en_cours.setVisibility(View.VISIBLE);
                    play_record.setVisibility(View.INVISIBLE);
                    stop_play_record.setVisibility(View.INVISIBLE);
                    record_or_stop.setImageResource(R.drawable.icons8_stop_60_i);



                    /*AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";*/
                        AudioSavePathInDevice = getNextAudioFileName(perquisition.getId());
                        Toast.makeText(RecorderAudioActivity.this, "fileName: " + AudioSavePathInDevice , Toast.LENGTH_SHORT).show();
                        Log.e("fileName" , AudioSavePathInDevice);
                        MediaRecorderReady();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error SICRE", "Error during recording audio");
                            e.printStackTrace();

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error SICRE", "Error during recording audio");
                            e.printStackTrace();

                        }

                        //play_record.setEnabled(false);
                        //stop_play_record.setEnabled(true);

                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

                }else {
                    isRecording = false;
                    enregistrement_en_cours.setVisibility(View.GONE);
                    play_record.setVisibility(View.VISIBLE);
                    stop_play_record.setVisibility(View.VISIBLE);
                    record_or_stop.setImageResource(R.drawable.icons8_microphone_60);

                    try{
                        mediaRecorder.stop();
                        handler.removeCallbacks(updateVisualizer);
                        visualizer.clear();
                    }catch(RuntimeException stopException){
                        Log.e("RuntimeException", stopException.getMessage() + " | "  + " | " + stopException.getStackTrace());
                    }

                    //buttonStop.setEnabled(false);
                    //buttonPlayLastRecordAudio.setEnabled(true);
                    //buttonStart.setEnabled(true);
                    //buttonStopPlayingRecording.setEnabled(false);

                    Toast.makeText(getApplicationContext(), "Recording Completed", Toast.LENGTH_LONG).show();

                }



            }
        });


        /*buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    mediaRecorder.stop();
                }catch(RuntimeException stopException){
                    Log.e("RuntimeException", stopException.getMessage() + " | "  + " | " + stopException.getStackTrace());
                }

                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Toast.makeText(getApplicationContext(), "Recording Completed", Toast.LENGTH_LONG).show();
            }
        });*/


        play_record.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {

                //buttonStop.setEnabled(false);
                //buttonStart.setEnabled(false);
                //buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {

                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
                    mediaPlayer.setAudioAttributes(audioAttributes);

                    mediaPlayer.setDataSource(AudioSavePathInDevice);

                    mediaPlayer.prepare();

                   // mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                isStopped = false;
                mediaPlayer.start();
                Toast.makeText(getApplicationContext(), "Recording Playing", Toast.LENGTH_LONG).show();
            }
        });


        stop_play_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buttonStop.setEnabled(false);
                //buttonStart.setEnabled(true);
                //buttonStopPlayingRecording.setEnabled(false);
                //buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null && !isStopped){
                    isStopped = true;
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

        handler = new Handler();

    }


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
        handler.post(updateVisualizer);
    }

    /*public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }*/

    private void requestPermission() {
        ActivityCompat.requestPermissions(RecorderAudioActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getApplicationContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }


    public String getNextAudioFileName(String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Constant.PERQUISITION_SOUND_ROOT + perquisitionId, Context.MODE_PRIVATE);
        // Create imageDir

        File[] files = directory.listFiles();

        Arrays.sort(files);

        int surfix = (int) (System.currentTimeMillis()/1000);
        String fileName = "audio_";

        if (files.length == 0){
            fileName += surfix;
        }else {
            String lastFileName = files[files.length-1].getName();
            int lastNum = Integer.parseInt(lastFileName.split("_")[1].split("\\.")[0]) + 1;
            fileName += "" + lastNum;
            Toast.makeText(getApplicationContext(), "lastNum: " + lastNum, Toast.LENGTH_LONG).show();
        }

        return directory.getAbsolutePath() + "/" + fileName + ".3gp";


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*  Back arrow*/
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        releaseRecorder();
    }
    private void releaseRecorder() {
        if (mediaRecorder != null) {
            isRecording = false; // stop recording
            handler.removeCallbacks(updateVisualizer);
            visualizer.clear();
            //mediaRecorder.stop();
            //mediaRecorder.reset();
            //mediaRecorder.release();
            //mediaRecorder = null;
        }
    }
}
