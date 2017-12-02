package cm.mindef.sed.sicre.mobile.fragments;


import android.app.AlertDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cm.mindef.sed.sicre.mobile.MusicService;
import cm.mindef.sed.sicre.mobile.PerquisitionActivity;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.RecorderAudioActivity;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionSoundAdapter;
import cm.mindef.sed.sicre.mobile.db.PreuvesDataSources;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.domain.Preuve;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MusicController;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;
import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;
import static cm.mindef.sed.sicre.mobile.utils.Constant.REQUEST_VIDEO_CAPTURE;

/**
 * Created by root on 15/10/17.
 */

public class SoundFragment extends Fragment implements MediaController.MediaPlayerControl {

    public static final int PATH_REQUEST_CODE = 100;
    private View rootView;
    private Perquisition perquisition;
    private ListView sound_list_view;
    private PerquisitionSoundAdapter perquisitionSoundAdapter;
    private FloatingActionButton add_preuve_sound;
    private MusicController controller;
    private boolean isViewShown = false;

    private SwipeRefreshLayout swipeContainer;


    private FloatingActionButton save;
    private ProgressBar progress_save;
    //private ImageButton close_controller;


    public static boolean hideController = false;

    private PerquisitionActivity perquisitionActivity;

   // private MediaPlayer mediaPlayer;
    //private AudioAttributes  audioAttributes;


    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    private ProgressBar currentProgressBar;


    private boolean paused=false, playbackPaused=false;


    private RequestQueue queue;
    private Button btn_refresh;

    //private StringRequest stringRequest;
    private JsonObjectRequest jsonObjectRequest;
    private JSONObject jsonObject;
    private String url;



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

        perquisitionActivity = (PerquisitionActivity) getActivity();

        rootView = view;

        save = rootView.findViewById(R.id.save);
        save.setVisibility(View.GONE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAll(v);
            }
        });

        progress_save = rootView.findViewById(R.id.progress_save);
        progress_save.setVisibility(View.GONE);

        /*close_controller = rootView.findViewById(R.id.close_controller);
        close_controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controller != null && controller.isShowing()){
                    musicSrv.finisPlay();
                    controller.hide();
                    //player.finish();
                }
            }
        });*/


        if (!isViewShown) {
            queue = MySingleton.getRequestQueue(this.getActivity().getApplicationContext());
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
        //getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();

    }

    private void showSaveButton() {

        PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
        preuvesDataSources.open();

        List<Preuve> preuves = preuvesDataSources.getAllPreuves(perquisition.getId());
        if (preuves.size() > 0){
            if (save.getVisibility() == View.GONE){
                save.setVisibility(View.VISIBLE);
            }
        }

        preuvesDataSources.close();
    }

    public void songPicked(View view){
       /* musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();*/


       ProgressBar loader = (ProgressBar) view.findViewById(R.id.loader);
        if (loader.getVisibility() == View.GONE){
            loader.setVisibility(View.VISIBLE);
        }
        currentProgressBar = loader;
        Log.e("InparseInt(view.getTag)", "          " + Integer.parseInt(view.getTag().toString()));
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
        startActivityForResult(intent, PATH_REQUEST_CODE);
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

        fetchData();

        PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
        preuvesDataSources.open();
        List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
        preuvesDataSources.close();

        if (preuveList.size() > 0){
            perquisitionActivity.loadPerquisitionData();
        }


        if(paused){
            setController();
            paused=false;
        }

        fetchData();

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
        Log.e("perquisition.getAudioLi", perquisition.getAudioLinks().toString());
        perquisitionSoundAdapter = new PerquisitionSoundAdapter(getActivity().getApplicationContext(), perquisition.getAudioLinks());
        sound_list_view.setAdapter(perquisitionSoundAdapter);

        sound_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songPicked(view);
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
                retVal.add("" + files[i].getAbsolutePath());
        }

        return  retVal;

    }

    private void setController(){
        //set the controller up
        controller = new MusicController(getActivity(), musicSrv);
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

    public  void fetchData() {

        TextView affaire = rootView.findViewById(R.id.affaire);


        Bundle bundle = getActivity().getIntent().getExtras();
        //if (perquisition == null)
        perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
        //Log.e("PERQUISITION", perquisition.toString());
        affaire.setText(perquisition.getDescription());



        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {


                Credentials credentials =  Credentials.getInstance(getActivity().getApplicationContext());

                url = "http://198.50.199.116:8090/scriptcase/app/SICRE_2/m_perquisition_search_by_id/?" + Constant.USERNAME + "="+credentials.getUsername()+
                        "&" + Constant.PASSWORD + "=" + credentials.getPassword() + "&" + Constant.ID + "=" + perquisition.getId();

                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonObject = response;

                        Log.e("response perqui", response.toString());

                        perquisition = Perquisition.getInstance(response);

                        Log.e("Perquisition . tostring", perquisition.toString());

                        List<String> localSounds = getSavedSounds(perquisition.getId());

                        Log.e("localSounds", localSounds.toString());

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
                        musicSrv.setList(perquisition.getAudioLinks());
                        swipeContainer.setRefreshing(false);

                    }
                },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                //Toast.makeText(getActivity().getApplication(), "Error..." + error.toString(), Toast.LENGTH_LONG).show();
                                Log.e("ERRORER 22", error.getCause() + " | " + error.getStackTrace() + " | " + error.getMessage() + " | " + error.toString());

                                swipeContainer.setRefreshing(false);


                                //loadPerquisition();

                            }
                        }
                );

                jsonObjectRequest.setTag(Constant.PERQUISITION_LIST_REQUEST_TAG);

// Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }

        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

        sound_list_view = rootView.findViewById(R.id.sound_list_view);
        //local_image_list_view = rootView.findViewById(R.id.local_image_list_view);
        //Log.e("SIZEEEEEEE", "" + perquisition.getTextLinks().size());
        List<String> localSounds = getSavedSounds(perquisition.getId());

        Log.e("localSounds", localSounds.toString());

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

        showSaveButton();


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == PATH_REQUEST_CODE && resultCode == RESULT_OK) {
            perquisition = (Perquisition) intent.getExtras().get(Constant.PERQUISITION);

            musicSrv.setList(perquisition.getAudioLinks());
            /*swipeContainer.post(new Runnable() {
                @Override public void run() {
                    swipeContainer.setRefreshing(true);
                }
            });*/

        }
    }

    public void saveAll(View view){
        Credentials credentials = Credentials.getInstance(perquisitionActivity.getApplicationContext());
        UploadFileAsync uploadFileAsync = new UploadFileAsync(credentials.getUsername(), credentials.getPassword());
        uploadFileAsync.execute(perquisitionActivity.getIntent().getExtras().getString(Constant.RESOURCES) + "/");
    }


    private  class UploadFileAsync extends AsyncTask<String, Void, String> {

        private AlertDialog dialog;
        private String username, password;
        private List<Preuve> results;
        private int total;

        public UploadFileAsync(String username, String password){
            this.username = username;
            this.password = password;
            dialog = new SpotsDialog(PerquisitionActivity.thisActivity.getApplicationContext());
            results = new ArrayList<>();

            PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
            preuvesDataSources.open();
            List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
            preuvesDataSources.close();

            total = preuveList.size();

        }
        @Override
        protected String doInBackground(String... params) {

            String url_string = params[0];

            Log.e("urlllllllllllll", url_string);
            String returnVal = "";
            // String uir_string = params[1];
            // Log.e("uriiiiiiiiiiiiiiii", uir_string);
            try {
                //String sourceFileUri = uir_string;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                //OutputStream out = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                try {

                    PreuvesDataSources preuvesDataSources = new PreuvesDataSources(getActivity().getApplicationContext());
                    preuvesDataSources.open();
                    List<Preuve> preuveList = preuvesDataSources.getAllPreuves(perquisition.getId());
                    preuvesDataSources.close();

                    Log.e("result, total", " " + preuveList.size() + ", " + total);

                    for (Preuve preuve: preuveList){

                        //RessourceCreated ressourceCreated = (RessourceCreated) object;
                        File sourceFile = new File(preuve.getPath());
                        Log.e("ABSOLUTE PATH", sourceFile.getAbsolutePath());
                        if (sourceFile.isFile()) {



                            String upLoadServerUri =url_string; /*"http://idea-cm.club/magasino/image.php";*/
                            String urlParameters  = Constant.USERNAME + "=" + this.username + "&" + Constant.PASSWORD + "=" +
                                    this.password + "&" + Constant.KEY_WORD + "=" + preuve.getType() + "&" +
                                    Constant.ID + "=" + perquisition.getId() + "&" + Constant.LATITUDE + "=" + preuve.getLatitude()
                                    + "&" + Constant.LONGITUDE + "=" + preuve.getLongitude();


                            Log.e("11111111111111",upLoadServerUri + "(" + preuve.getLatitude() + ", " +preuve.getLongitude() + ")");
                            // open a URL connection to the Servlet
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL(upLoadServerUri + "?" + urlParameters);

                            Log.e("urlParameters", urlParameters);

                            // Open a HTTP connection to the URL
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            //conn.setChunkedStreamingMode(0);
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty(preuve.getType(), sourceFile.getAbsolutePath());
                            //conn.setRequestProperty();
                            //Log.e("2222222222222222",urlParameters);

                            dos = new DataOutputStream(conn.getOutputStream());
                            //out = new BufferedOutputStream(conn.getOutputStream());

                            //BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                            //writer.write(urlParameters);


                            //dos.write(postData);

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"" + preuve.getType() + "\";filename=\"" +preuve.getPath() + "\"" + lineEnd);

                            dos.writeBytes(lineEnd);

                            Log.e("3333333333333333333333","333333333333333333333333333333333333333333333333");

                            // create a buffer of maximum size
                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            // read file and write it into form...
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            Log.e("bytesAvailable","" + bytesAvailable);

                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                                Log.e("image size:::::::::::", "" + bufferSize);
                            }

                            // send multipart form data necesssary after file
                            // data...
                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                            Log.e("444444444444","44444444444444444444444444444444444444444444444444444444444");

                            // Responses from the server (code and message)
                            int serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn.getResponseMessage();

                            InputStream in = conn.getInputStream();

                            Log.e("serverResponseCode", " " + serverResponseCode);

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
                                Log.e("sb.toString()", sb.toString());
                                //os.close();
                                returnVal = sb.toString();

                                results.add(preuve);
                                Log.e("results.size", "" + results.size());

                                fileInputStream.close();
                                //delete
                                File file = new File(preuve.getPath());
                                boolean deleted = file.delete();
                                Log.e("FILE DELETED", " " + deleted);
                                PreuvesDataSources preuvesDataSources1 = new PreuvesDataSources(getActivity().getApplicationContext());
                                preuvesDataSources1.open();
                                preuvesDataSources1.deletePreuve(preuve);
                                preuvesDataSources1.close();
                               // perquisition.removePreuve(preuve.getPath());
                                //PerquisitionActivity.publisher.unsubscribe(ressourceCreated);
                                Log.e("deleted: ", "" + preuve.toString());

                            }else {
                                Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                                returnVal =  Constant.KO + " 3     " ;
                            }



                        }else {
                            Log.e("NOT FILE", sourceFile.getAbsolutePath());
                        }
                    }



                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();
                    return Constant.KO +  " 4    " + e.getMessage();

                }
                // dialog.dismiss();

                // End else block


                //conn.disconnect();
            } catch (Exception ex) {
                // dialog.dismiss();
                ex.printStackTrace();
                return Constant.KO + " 5           " + ex.getMessage();
            }
            return returnVal;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (progress_save.getVisibility() == View.VISIBLE){
                progress_save.setVisibility(View.GONE);
            }

            Log.e("returnValllll", result +  " " + " " + results.size() + ", " + total);

            //Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();

            Log.e("result, total", " " + " " + results.size() + ", " + total);

            if (results.size() == total){
                save.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.successful_saved), Toast.LENGTH_LONG).show();
            }

            fetchData();
            //swipeContainer.setRefreshing(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("onPreExecute", "onPreExecute");
            if (progress_save.getVisibility() == View.GONE){
                progress_save.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}
