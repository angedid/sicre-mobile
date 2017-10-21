package cm.mindef.sed.sicre.mobile;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.*;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.utils.Constant;

/**
 * Created by root on 18/10/17.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener{

    //media player
    private MediaPlayer player;
    //song list
    private List<String> songs;
    //current position
    private int songPosn;

    private final IBinder musicBind = new MusicBinder();

    private String songTitle="";
    private static final int NOTIFY_ID=1;

    private boolean shuffle=false;
    private Random rand;

    private AudioAttributes audioAttributes;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(){
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new MediaPlayer();

        initMusicPlayer();

        rand=new Random();


        AudioAttributes mPlaybackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        /*AudioFocusRequest.Builder builder =  new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN);
        AudioFocusRequest mFocusRequest = builder.setAudioAttributes(mPlaybackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(this, new Handler())
                .build();

        audioManager.requestAudioFocus(mFocusRequest);*/

    }

    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
         audioAttributes =  new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        player.setAudioAttributes(audioAttributes);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);


    }

    public void setList(List<String> theSongs){
        songs=theSongs;
    }

    public void playSong(){
        //play a song
        player.reset();

        String url = songs.get(songPosn);
        String vet [] = url.split("\\/");
        songTitle = vet[vet.length - 1];

        Uri trackUri = Uri.parse(url);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
        Log.e("AFTER PREPARE ASYNC", "SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED ");



        //songTitle=playSong.getTitle();
       /* //get song
        Song playSong = songs.get(songPosn);
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);*/
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() > 0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //return false;

        mp.reset();
        Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
        this.sendBroadcast(i);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();

        Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
        this.sendBroadcast(i);

        /*Log.e("IN ONPREPARED", "SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED SONG IS PREPARED ");

        Intent notIntent = new Intent(this, PerquisitionActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constant.NOTIFICATION_CHANEL_ID);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing").setContentText(songTitle);
        Notification not = builder.build();


        startForeground(NOTIFY_ID, not);

        Log.e("NOTIFICATION IS LAUNCH", "NOTIFICATION IS LAUNCHED NOTIFICATION IS LAUNCHED NOTIFICATION IS LAUNCHED");*/


/*
        // The id of the channel.
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, Constant.NOTIFICATION_CHANEL_ID)
                        .setSmallIcon(R.drawable.play)
                        .setContentTitle(songTitle)
                        .setContentText(songTitle)
                        .setChannelId(Constant.NOTIFICATION_CHANEL_ID);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, PerquisitionActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(PerquisitionActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mNotificationId is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());*/


    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }


    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }


    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){

        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;
        }
        else{
            songPosn++;
            if(songPosn>=songs.size()) songPosn=0;
        }
        playSong();

    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN){
            playSong();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS){
            player.stop();
            player.release();
        }
    }


    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}