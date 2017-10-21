package cm.mindef.sed.sicre.mobile.fragments;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cm.mindef.sed.sicre.mobile.MusicService;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.RecorderAudioActivity;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionSoundAdapter;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.MusicController;

/**
 * Created by root on 15/10/17.
 */

public class SoundFragment extends Fragment implements MediaController.MediaPlayerControl {

    private View rootView;
    private Perquisition perquisition;
    private ListView sound_list_view;
    private PerquisitionSoundAdapter perquisitionSoundAdapter;
    private Button add_preuve_sound;
    private MusicController controller;
    private boolean isViewShown = false;

    public static boolean hideController = false;

   // private MediaPlayer mediaPlayer;
    //private AudioAttributes  audioAttributes;


    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    private ProgressBar currentProgressBar;


    private boolean paused=false, playbackPaused=false;

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            if (perquisition == null){
                Bundle bundle = getActivity().getIntent().getExtras();
                perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
            }
            musicSrv.setList(perquisition.getAudioLinks());
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private BroadcastReceiver mReceiver ;
    //private
    private IntentFilter intentFilter;

    public SoundFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sound, container, false);

        rootView = view;

        if (!isViewShown) {
            fetchData();
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(playIntent);
        musicSrv=null;
        getActivity().unbindService(musicConnection);
        super.onDestroy();

    }

    public void songPicked(View view){
       /* musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();*/


       ProgressBar loader = (ProgressBar) view.findViewById(R.id.loader);
        if (loader.getVisibility() == View.GONE){
            loader.setVisibility(View.VISIBLE);
        }
        currentProgressBar = loader;
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        if (!controller.isShowing())
            controller.show(0);

    }

    @Override
    public void onPause(){
        //if (mediaPlayer.isPlaying())
        //    mediaPlayer.pause();
        super.onPause();
        paused=true;
    }



    @Override
    public void onStop() {
        controller.hide();

        getActivity().unregisterReceiver(mReceiver);
        super.onStop();
    }

    private void reccorAudio() {

        Intent intent = new Intent(getActivity().getApplicationContext(), RecorderAudioActivity.class);
        intent.putExtra(Constant.PERQUISITION, perquisition);
        startActivity(intent);
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
        if(paused){
            setController();
            paused=false;
        }

         intentFilter = new IntentFilter("android.intent.action.MAIN");
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                //String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                // Log.e("InchooTutorial", msg_for_me);

                if (currentProgressBar != null){
                    currentProgressBar.setVisibility(View.GONE);
                }

            }
        };

        this.getActivity().registerReceiver(mReceiver, intentFilter);

    }

    /**
     * Ecriture / sauvegarde dans le systeme de fichier local
     * @param mySound
     * @param perquisitionId
     * @return
     */
    private String saveToInternalStorage(byte[] mySound, String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Constant.PERQUISITION_SOUND_ROOT + perquisitionId, Context.MODE_PRIVATE);
        // Create imageDir

        File[] files = directory.listFiles();

        Arrays.sort(files);

        int surfix = (int) (System.currentTimeMillis()/1000);
        String fileName = "sound_";

        if (files.length == 0){
            fileName += surfix;
        }else {
            String lastFileName = files[files.length-1].getName();
            int lastNum = Integer.parseInt(lastFileName.split("_")[1].split("\\.")[0]) + 1;
            fileName += "" + lastNum;
            Toast.makeText(getActivity().getApplicationContext(), "lastNum: " + lastNum, Toast.LENGTH_LONG).show();
        }


        Toast.makeText(getActivity().getApplicationContext(), "fileName: " + fileName, Toast.LENGTH_LONG).show();

        File mypath=new File(directory,  fileName + ".sound");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            fos.write(mySound);
            // Use the compress method on the BitMap object to write image to the OutputStream
            //bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> locatSound = getSavedSounds(perquisition.getId());
        for (String s : locatSound) perquisition.getAudioLinks().add(s);
        perquisitionSoundAdapter = new PerquisitionSoundAdapter(getActivity().getApplicationContext(), perquisition.getAudioLinks());
        sound_list_view.setAdapter(perquisitionSoundAdapter);

        sound_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            }
        });

        return directory.getAbsolutePath();
    }


    /**
     * Lecture des fichiers enregistres
     * @param perquisitionId
     * @return
     */

    public List<String> getSavedSounds(String perquisitionId){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir(Constant.PERQUISITION_SOUND_ROOT + perquisitionId, Context.MODE_PRIVATE);
        // Create imageDir
        File[] files = directory.listFiles();

        Arrays.sort(files);

        List<String> retVal = new ArrayList<>();
        for (int i=0; i<files.length; i++){
            String dirs[] = files[i].getAbsolutePath().split("\\/");
            String lastDir = dirs[dirs.length-2];

            String vet [] = lastDir.split("_");
            String id = vet[vet.length-1];
            if (id.equals(perquisitionId))
                retVal.add("file://" + files[i].getAbsolutePath());
        }

        return  retVal;

    }

    private void setController(){
        //set the controller up
        controller = new MusicController(getActivity());
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(rootView.findViewById(R.id.audio_controller_hint));
        controller.setEnabled(true);
    }


    //play next
    private void playNext(){

        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    //play previous
    private void playPrev(){

        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }


    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {

        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public MusicController getMusicController(){
        return this.controller;
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

    private void fetchData() {

        TextView affaire = rootView.findViewById(R.id.affaire);

        Bundle bundle = getActivity().getIntent().getExtras();
        perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
        //Log.e("PERQUISITION", perquisition.toString());
        affaire.setText(perquisition.getAffaire());

        sound_list_view = rootView.findViewById(R.id.sound_list_view);
        //local_image_list_view = rootView.findViewById(R.id.local_image_list_view);
        //Log.e("SIZEEEEEEE", "" + perquisition.getTextLinks().size());
        List<String> localSounds = getSavedSounds(perquisition.getId());
        for (String s : localSounds) perquisition.getAudioLinks().add(s);
        perquisitionSoundAdapter = new PerquisitionSoundAdapter(getActivity().getApplicationContext(), perquisition.getAudioLinks());
        sound_list_view.setAdapter(perquisitionSoundAdapter);


        //mediaPlayer = new MediaPlayer();

        sound_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songPicked(view);
            }
        });

        setController();


        /* sound_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               try {
                        mediaPlayer.reset();

                    audioAttributes =  new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();


                    final View viewClicked = view;
                    mediaPlayer.setAudioAttributes(audioAttributes);
                    mediaPlayer.setDataSource(perquisition.getAudioLinks().get(position));


                    (new AsyncTask<String, Integer, String>(){
                        private ProgressBar progressBar;
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressBar = viewClicked.findViewById(R.id.loader);
                            if (progressBar.getVisibility() == View.GONE){
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute((String) result);
                            if (progressBar.getVisibility() == View.VISIBLE){
                                progressBar.setVisibility(View.GONE);
                            }


                        }
                    }).execute("");


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
            }
        });*/



        // perquisitionLocalImageAdapter = new PerquisitionImageAdapter(getActivity().getApplicationContext(), getSavedImages(perquisition.getId()));
        //local_image_list_view.setAdapter(perquisitionLocalImageAdapter);


        add_preuve_sound = rootView.findViewById(R.id.add_preuve_sound);
        add_preuve_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reccorAudio();
            }
        });


        /*String url = "http://idea-cm.club/magasino/Vivere.mp3"; // your URL here
        final MediaPlayer mediaPlayer = new MediaPlayer();

        AudioAttributes  audioAttributes =  new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setAudioAttributes(audioAttributes);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


}
