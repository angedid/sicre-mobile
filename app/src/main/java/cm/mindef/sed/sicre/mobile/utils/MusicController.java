package cm.mindef.sed.sicre.mobile.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.MediaController;

import cm.mindef.sed.sicre.mobile.fragments.SoundFragment;

/**
 * Created by root on 18/10/17.
 */

public class MusicController extends MediaController {
    public MusicController(Context c){
        super(c);
    }

    @Override
    public void hide(){

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



}
