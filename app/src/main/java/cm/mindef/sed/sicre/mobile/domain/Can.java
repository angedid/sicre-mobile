package cm.mindef.sed.sicre.mobile.domain;

import java.io.Serializable;

/**
 * Created by nkalla on 02/11/17.
 */

public class Can implements Serializable{
    private boolean can;
    private String urlResource;

    public Can(boolean can, String urlResource) {
        this.can = can;
        this.urlResource = urlResource;
    }

    public boolean isCan() {
        return can;
    }

    public void setCan(boolean can) {
        this.can = can;
    }

    public String getUrlResource() {
        return urlResource;
    }

    public void setUrlResource(String urlResource) {
        this.urlResource = urlResource;
    }
}
