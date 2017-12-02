package cm.mindef.sed.sicre.mobile.oservable.event;

import android.content.Context;

import java.io.Serializable;

import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.oservable.MyDomainEventSubscriber;

/**
 * Created by nkalla on 03/11/17.
 */

public class RessourceCreated implements Serializable, MyDomainEventSubscriber{

    private String  uri;
    private String  type;
    private Context context;
    private Perquisition perquisition;

    public RessourceCreated(String uri, String type, Context context, Perquisition perquisition){
        this.setUri(uri);
        this.setType(type);
        this.setContext(context);
        this.setPerquisition(perquisition);
    }

    public String getUri() {
        return uri;
    }

    private void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public Perquisition getPerquisition() {
        return perquisition;
    }

    private void setPerquisition(Perquisition perquisition) {
        this.perquisition = perquisition;
    }

    @Override
    synchronized public void handleEvent() {

        //Credentials credentials = Credentials.getInstance(context);
        //UploadFileAsync uploadFileAsync = new UploadFileAsync(credentials.getUsername(), credentials.getPassword());
        //uploadFileAsync.execute(Constant.URL_LINK+ Constant.PERQUISITION_SAVE, uri);
    }


    @Override
    public String toString() {
        return "\nRessourceCreated{" +
                "uri='" + uri + '\'' +
                ", type='" + type + '\'' +
                '}' + "\n\n";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RessourceCreated)) return false;

        RessourceCreated that = (RessourceCreated) o;

        if (!uri.equals(that.uri)) return false;
        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }


}
