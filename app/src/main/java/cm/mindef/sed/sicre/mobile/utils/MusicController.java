package cm.mindef.sed.sicre.mobile.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;

import cm.mindef.sed.sicre.mobile.MusicService;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.fragments.SoundFragment;

/**
 * Created by root on 18/10/17.
 */

public class MusicController extends MediaController {
    private Context context;
    private MusicService musicService;
    public MusicController(Context c, MusicService musicService){
        super(c);
        context = c;
        this.musicService = musicService;
    }

    @Override
    public boolean dispatchKeyEvent (KeyEvent event){
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
            super.hide();
            if (SoundFragment.hideController){
                SoundFragment.hideController = false;
            }else{
                ((AppCompatActivity)getContext()).finish();
            }
        }
        return false;
    }

    /*@Override
    public void hide() {
        setVisibility(GONE);
    }*/


    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        ImageView closeButton = new ImageView(context);
        closeButton.setImageResource(R.drawable.icons8_delete_24);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.setMargins(0, 0, 10, 10);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (this != null && isShowing()){
                    hide();
                    musicService.finisPlay();
                    Log.e("Close success", "musicService.finisPlay();musicService.finisPlay();musicService.finisPlay();musicService.finisPlay();");
                    //player.finish();
                }
            }
        });
        addView(closeButton, params);
    }

}
