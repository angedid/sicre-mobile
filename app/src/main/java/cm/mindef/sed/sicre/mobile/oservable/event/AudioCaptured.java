package cm.mindef.sed.sicre.mobile.oservable.event;

import android.content.Context;

import cm.mindef.sed.sicre.mobile.domain.Perquisition;

/**
 * Created by nkalla on 03/11/17.
 */

public class AudioCaptured  extends RessourceCreated {

    public AudioCaptured(String uri, String type, Context context, Perquisition perquisition) {
        super(uri, type, context, perquisition);
    }
}
