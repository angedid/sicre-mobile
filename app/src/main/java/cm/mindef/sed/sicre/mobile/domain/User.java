package cm.mindef.sed.sicre.mobile.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by root on 31/10/17.
 */


public class User implements Serializable {

    private String name;
    private String username;
    private String email;
    private String photoUrl;
    private String langue;
    private String unite;

    private Access perquisition, individu, vehicule, objet, alert;

    public User(String name, String username, String email, String photoUrl, String langue, String unite,
                Access perquisition, Access individu, Access vehicule, Access objet, Access alert) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
        this.langue = langue;
        this.unite = unite;
        this.perquisition = perquisition;
        this.individu = individu;
        this.vehicule = vehicule;
        this.objet = objet;
        this.alert = alert;
    }

    public static User getInstance(JSONObject jsonObject){
        try {
            String name = (!jsonObject.isNull("name"))?jsonObject.getString("name"):"";
            String username = (!jsonObject.isNull("username"))?jsonObject.getString("username"):"";
            String email = (!jsonObject.isNull("email"))?jsonObject.getString("email"):"";
            String photo = (!jsonObject.isNull("photo"))?jsonObject.getString("photo"):"";
            String langue = (!jsonObject.isNull("langue"))?jsonObject.getString("langue"):"";
            String unite = (!jsonObject.isNull("unite"))?jsonObject.getString("unite"):"";

            JSONObject access = jsonObject.getJSONObject("access");

            JSONObject perquisitionJsonObj = access.getJSONObject("perquisition");
            JSONObject individuJsonObj = access.getJSONObject("individu");
            JSONObject vehiculeJsonObj = access.getJSONObject("vehicule");
            JSONObject objetJsonObj = access.getJSONObject("objet");
            JSONObject alertJsonObj = access.getJSONObject("alert");


            JSONObject perquisitionCreate = perquisitionJsonObj.getJSONObject("create");
            JSONObject perquisitionRead = perquisitionJsonObj.getJSONObject("access");
            JSONObject perquisitionUpdate = perquisitionJsonObj.getJSONObject("update");
            JSONObject perquisitionDelete = perquisitionJsonObj.getJSONObject("delete");

            Access perquisition = new Access(
                    new Can(perquisitionCreate.getString("can").equals("Y"), perquisitionCreate.getString("ressource")),
                    new Can(perquisitionRead.getString("can").equals("Y"), perquisitionRead.getString("ressource")),
                    new Can(perquisitionUpdate.getString("can").equals("Y"), perquisitionUpdate.getString("ressource")),
                    new Can(perquisitionDelete.getString("can").equals("Y"), perquisitionDelete.getString("ressource"))

            );


            JSONObject individuCreate = individuJsonObj.getJSONObject("create");
            JSONObject individuRead = individuJsonObj.getJSONObject("access");
            JSONObject individuUpdate = individuJsonObj.getJSONObject("update");
            JSONObject individuDelete = individuJsonObj.getJSONObject("delete");

            Access individu = new Access(
                    new Can(individuCreate.getString("can").equals("Y"), individuCreate.getString("ressource")),
                    new Can(individuRead.getString("can").equals("Y"), individuRead.getString("ressource")),
                    new Can(individuUpdate.getString("can").equals("Y"), individuUpdate.getString("ressource")),
                    new Can(individuDelete.getString("can").equals("Y"), individuDelete.getString("ressource"))

            );


            JSONObject vehiculeCreate = vehiculeJsonObj.getJSONObject("create");
            JSONObject vehiculeRead = vehiculeJsonObj.getJSONObject("access");
            JSONObject vehiculeUpdate = vehiculeJsonObj.getJSONObject("update");
            JSONObject vehiculeDelete = vehiculeJsonObj.getJSONObject("delete");

            Access vehicule = new Access(
                    new Can(vehiculeCreate.getString("can").equals("Y"), vehiculeCreate.getString("ressource")),
                    new Can(vehiculeRead.getString("can").equals("Y"), vehiculeRead.getString("ressource")),
                    new Can(vehiculeUpdate.getString("can").equals("Y"), vehiculeUpdate.getString("ressource")),
                    new Can(vehiculeDelete.getString("can").equals("Y"), vehiculeDelete.getString("ressource"))

            );

            JSONObject objetCreate = objetJsonObj.getJSONObject("create");
            JSONObject objetRead = objetJsonObj.getJSONObject("access");
            JSONObject objetUpdate = objetJsonObj.getJSONObject("update");
            JSONObject objetDelete = objetJsonObj.getJSONObject("delete");

            Access objet = new Access(
                    new Can(objetCreate.getString("can").equals("Y"), objetCreate.getString("ressource")),
                    new Can(objetRead.getString("can").equals("Y"), objetRead.getString("ressource")),
                    new Can(objetUpdate.getString("can").equals("Y"), objetUpdate.getString("ressource")),
                    new Can(objetDelete.getString("can").equals("Y"), objetDelete.getString("ressource"))

            );


            JSONObject alertCreate = alertJsonObj.getJSONObject("create");
            JSONObject alertRead = alertJsonObj.getJSONObject("access");
            JSONObject alertUpdate = alertJsonObj.getJSONObject("update");
            JSONObject alertDelete = alertJsonObj.getJSONObject("delete");

            Access alert = new Access(
                    new Can(alertCreate.getString("can").equals("Y"), alertCreate.getString("ressource")),
                    new Can(alertRead.getString("can").equals("Y"), alertRead.getString("ressource")),
                    new Can(alertUpdate.getString("can").equals("Y"), alertUpdate.getString("ressource")),
                    new Can(alertDelete.getString("can").equals("Y"), alertDelete.getString("ressource"))

            );

            return new User(name, username, email, photo, langue, unite, perquisition,individu, vehicule, objet, alert);


        } catch (JSONException e) {
            Log.e("ERRORRRRRRRR" , e.getMessage());
            return null;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Access getPerquisition() {
        return perquisition;
    }

    public void setPerquisition(Access perquisition) {
        this.perquisition = perquisition;
    }

    public Access getIndividu() {
        return individu;
    }

    public void setIndividu(Access individu) {
        this.individu = individu;
    }

    public Access getVehicule() {
        return vehicule;
    }

    public void setVehicule(Access vehicule) {
        this.vehicule = vehicule;
    }

    public Access getObjet() {
        return objet;
    }

    public void setObjet(Access objet) {
        this.objet = objet;
    }

    public Access getAlert() {
        return alert;
    }

    public void setAlert(Access alert) {
        this.alert = alert;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", langue='" + langue + '\'' +
                ", unite='" + unite + '\'' +
                ", perquisition=" + perquisition +
                ", individu=" + individu +
                ", vehicule=" + vehicule +
                ", objet=" + objet +
                ", alert=" + alert +
                '}';
    }
}
