package cm.mindef.sed.sicre.mobile.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by nkalla on 20/11/17.
 */

public class SearchCriteria implements Serializable{

    private String individuLinkCriteria, vehiculeLinkCriteria, objectLinkCriteria;

    public SearchCriteria(String individuLinkCriteria, String vehiculeLinkCriteria, String objectLinkCriteria) {
        this.individuLinkCriteria = individuLinkCriteria;
        this.vehiculeLinkCriteria = vehiculeLinkCriteria;
        this.objectLinkCriteria = objectLinkCriteria;
    }


    public static SearchCriteria getInstance(JSONObject jsonObject){
        try {
            String individu = (!jsonObject.isNull("individu"))?jsonObject.getString("individu"):"";
            String vehicule = (!jsonObject.isNull("vehicule"))?jsonObject.getString("vehicule"):"";
            String objet = (!jsonObject.isNull("objet"))?jsonObject.getString("objet"):"";

            return new SearchCriteria(individu, vehicule, objet);


        } catch (JSONException e) {
            Log.e("ERRORRRRRRRR" , e.getMessage());
            return null;
        }

    }

    public String getIndividuLinkCriteria() {
        return individuLinkCriteria;
    }

    public void setIndividuLinkCriteria(String individuLinkCriteria) {
        this.individuLinkCriteria = individuLinkCriteria;
    }

    public String getVehiculeLinkCriteria() {
        return vehiculeLinkCriteria;
    }

    public void setVehiculeLinkCriteria(String vehiculeLinkCriteria) {
        this.vehiculeLinkCriteria = vehiculeLinkCriteria;
    }

    public String getObjectLinkCriteria() {
        return objectLinkCriteria;
    }

    public void setObjectLinkCriteria(String objectLinkCriteria) {
        this.objectLinkCriteria = objectLinkCriteria;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "individuLinkCriteria='" + individuLinkCriteria + '\'' +
                ", vehiculeLinkCriteria='" + vehiculeLinkCriteria + '\'' +
                ", objectLinkCriteria='" + objectLinkCriteria + '\'' +
                '}';
    }
}
