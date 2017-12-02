package cm.mindef.sed.sicre.mobile.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cm.mindef.sed.sicre.mobile.HomeActivity;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;

/**
 * Created by root on 14/10/17.
 */

public class Perquisition implements Serializable{

    private String id;
    private String description;
    private String domaine;
    private String objet_saisis;
    private String descriptionLieu;
    private String descriptionObjet;
    private String declarationReceptionniste;
    private List<String> preuves;
    private List<String> imageLinks;
    private List<String> audioLinks;
    private List<String> videoLinks;

    public Perquisition(String id, String description, String domaine, String objet_saisis,
                        String descriptionLieu, String descriptionObjet, String declarationReceptionniste,
                        List<String> preuves,List<String> imageLinks, List<String> audioLinks, List<String> videoLinks) {
        this.id = id;
        this.description = description;
        this.domaine = domaine;
        this.objet_saisis = objet_saisis;
        this.descriptionLieu = descriptionLieu;
        this.descriptionObjet = descriptionObjet;
        this.declarationReceptionniste = declarationReceptionniste;
        this.preuves = preuves;
        this.imageLinks = imageLinks;
        this.audioLinks = audioLinks;
        this.videoLinks = videoLinks;
    }

    public static Perquisition getInstance(JSONObject jsonObject){
        try {

            Credentials ceCredentials = Credentials.getInstance(HomeActivity.thisActivity);
            String url = "http://198.50.199.116:8090/scriptcase/app/SICRE_2/m_get_file/?" + Constant.USERNAME + "=" + ceCredentials.getUsername()+
                    "&" + Constant.PASSWORD + "=" + ceCredentials.getPassword() + "&path=";
            //(!jsonObjectPhoto.isNull("profil_g"))?jsonObjectPhoto.getString("profil_g"):"";
            String id = (!jsonObject.isNull("id"))?jsonObject.getString("id"):"";
            String description = (!jsonObject.isNull("description"))?jsonObject.getString("description"):"";
            String domaine = (!jsonObject.isNull("domaine"))?jsonObject.getString("domaine"):"";
            String objet_saisis = (!jsonObject.isNull("objet_saisis"))?jsonObject.getString("objet_saisis"):"";
            String description_lieu = (!jsonObject.isNull("description_lieu"))?jsonObject.getString("description_lieu"):"";
            String description_objet = (!jsonObject.isNull("description_objet"))?jsonObject.getString("description_objet"):"";
            String declaration_receptionniste = (!jsonObject.isNull("declaration_receptionniste"))?jsonObject.getString("declaration_receptionniste"):"";

            JSONArray preuvesarray = (!jsonObject.isNull("preuve"))?jsonObject.getJSONArray("preuve"):new JSONArray("[]");
            List<String> preuves = new ArrayList<>();
            for (int i=0; i<preuvesarray.length(); i++){
                preuves.add(preuvesarray.getString(i));
            }

            JSONArray imagearray = (!jsonObject.isNull("images"))?jsonObject.getJSONArray("images"):new JSONArray("[]");
            List<String> images = new ArrayList<>();
            for (int i=0; i<imagearray.length(); i++){
                images.add(url + imagearray.getString(i));
            }

            JSONArray audioArray = (!jsonObject.isNull("audios"))?jsonObject.getJSONArray("audios"):new JSONArray("[]");
            List<String> audios = new ArrayList<>();
            for (int i=0; i<audioArray.length(); i++){
                audios.add(url+ audioArray.getString(i));
            }


            JSONArray videoArray = (!jsonObject.isNull("videos"))?jsonObject.getJSONArray("videos"):new JSONArray("[]");
            List<String> videos = new ArrayList<>();
            for (int i=0; i<videoArray.length(); i++){
                videos.add(url + videoArray.getString(i));
            }



            return new Perquisition(id, description, domaine, objet_saisis, description_lieu, description_objet, declaration_receptionniste,
                    preuves, images, audios, videos);
        } catch (JSONException e) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public String getObjet_saisis() {
        return objet_saisis;
    }

    public void setObjet_saisis(String objet_saisis) {
        this.objet_saisis = objet_saisis;
    }

    public String getDescriptionLieu() {
        return descriptionLieu;
    }

    public void setDescriptionLieu(String descriptionLieu) {
        this.descriptionLieu = descriptionLieu;
    }

    public String getDescriptionObjet() {
        return descriptionObjet;
    }

    public void setDescriptionObjet(String descriptionObjet) {
        this.descriptionObjet = descriptionObjet;
    }

    public String getDeclarationReceptionniste() {
        return declarationReceptionniste;
    }

    public void setDeclarationReceptionniste(String declarationReceptionniste) {
        this.declarationReceptionniste = declarationReceptionniste;
    }

    public List<String> getPreuves() {
        return preuves;
    }

    public void setPreuves(List<String> preuves) {
        this.preuves = preuves;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public List<String> getAudioLinks() {
        return audioLinks;
    }

    public void setAudioLinks(List<String> audioLinks) {
        this.audioLinks = audioLinks;
    }

    public List<String> getVideoLinks() {
        return videoLinks;
    }

    public void setVideoLinks(List<String> videoLinks) {
        this.videoLinks = videoLinks;
    }

    public void addPreuve(String preuve) {
        preuves.add(preuve);
    }

    public void removePreuve(String link){
        List<String> retVal = new ArrayList<>();

        for (String string:audioLinks){
            if (!string.contains(link)){
                retVal.add(string);
            }
        }

        audioLinks = retVal;

        retVal = new ArrayList<>();

        for (String string:videoLinks){
            if (!string.contains(link)){
                retVal.add(string);
            }
        }

        videoLinks = retVal;

        retVal = new ArrayList<>();

        for (String string:imageLinks){
            if (!string.contains(link)){
                retVal.add(string);
            }
        }

        imageLinks = retVal;
    }


    @Override
    public String toString() {
        return "Perquisition{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", domaine='" + domaine + '\'' +
                ", objet_saisis='" + objet_saisis + '\'' +
                ", descriptionLieu='" + descriptionLieu + '\'' +
                ", descriptionObjet='" + descriptionObjet + '\'' +
                ", declarationReceptionniste='" + declarationReceptionniste + '\'' +
                ", preuves=" + preuves +
                ", imageLinks=" + imageLinks +
                ", audioLinks=" + audioLinks +
                ", videoLinks=" + videoLinks +
                '}';
    }
}
