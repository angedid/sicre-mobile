package cm.mindef.sed.sicre.mobile.oservable.event;

import android.content.Context;

import cm.mindef.sed.sicre.mobile.domain.Perquisition;

/**
 * Created by nkalla on 03/11/17.
 */

public class VideoCaptured extends RessourceCreated{
    public VideoCaptured(String uri, String type, Context context, Perquisition perquisition) {
        super(uri, type, context, perquisition);
    }
}
