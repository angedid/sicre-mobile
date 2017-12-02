package cm.mindef.sed.sicre.mobile.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by root on 14/10/17.
 */


public class Objet implements Serializable{

    private String id;
    private String libele;
    private String description;
    private String status;
    private String wanted_num;
    private String wanted_desc;
    private String carte_grise;
    private String matricule;
    private String puissance;
    private String kilometrage;
    private String annee_circ;
    private String num_assurance;
    private String num_enregistrement;
    private String photo;

    public Objet(String id, String libele, String description, String status, String wanted_num,
                 String wanted_desc, String carte_grise, String matricule, String puissance, String kilometrage,
                 String annee_circ, String num_assurance, String num_enregistrement, String photo) {
        this.id = id;
        this.libele = libele;
        this.description = description;
        this.status = status;
        this.wanted_num = wanted_num;
        this.wanted_desc = wanted_desc;
        this.carte_grise = carte_grise;
        this.matricule = matricule;
        this.puissance = puissance;
        this.kilometrage = kilometrage;
        this.annee_circ = annee_circ;
        this.num_assurance = num_assurance;
        this.num_enregistrement = num_enregistrement;
        this.photo = photo;
    }

    public static Objet getInstance(JSONObject jsonObject){
        try {
            String id = (!jsonObject.isNull("id"))?jsonObject.getString("id"):"";
            String libele = (!jsonObject.isNull("libele"))?jsonObject.getString("libele"):"";
            String description = (!jsonObject.isNull("description"))?jsonObject.getString("description"):"";
            String status = (!jsonObject.isNull("status"))?jsonObject.getString("status"):"";
            String wanted_num = (!jsonObject.isNull("wanted_num"))?jsonObject.getString("wanted_num"):"";
            String wanted_desc = (!jsonObject.isNull("wanted_desc"))?jsonObject.getString("wanted_desc"):"";
            String carte_grise = (!jsonObject.isNull("carte_grise"))?jsonObject.getString("carte_grise"):"";
            String matricule = (!jsonObject.isNull("matricule"))?jsonObject.getString("matricule"):"";
            String puissance = (!jsonObject.isNull("puissance"))?jsonObject.getString("puissance"):"";
            String kilometrage = (!jsonObject.isNull("kilometrage"))?jsonObject.getString("kilometrage"):"";
            String annee_circ = (!jsonObject.isNull("annee_circ"))?jsonObject.getString("annee_circ"):"";
            String num_assurance = (!jsonObject.isNull("num_assurance"))?jsonObject.getString("num_assurance"):"";
            String num_enregistrement = (!jsonObject.isNull("num_enregistrement"))?jsonObject.getString("num_enregistrement"):"";
            String photo = (!jsonObject.isNull("photo"))?jsonObject.getString("photo"):"";



            return new Objet(id, libele, description, status, wanted_num, wanted_desc, carte_grise,
                    matricule, puissance, kilometrage, annee_circ, num_assurance, num_enregistrement, photo);
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

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWanted_num() {
        return wanted_num;
    }

    public void setWanted_num(String wanted_num) {
        this.wanted_num = wanted_num;
    }

    public String getWanted_desc() {
        return wanted_desc;
    }

    public void setWanted_desc(String wanted_desc) {
        this.wanted_desc = wanted_desc;
    }

    public String getCarte_grise() {
        return carte_grise;
    }

    public void setCarte_grise(String carte_grise) {
        this.carte_grise = carte_grise;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPuissance() {
        return puissance;
    }

    public void setPuissance(String puissance) {
        this.puissance = puissance;
    }

    public String getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(String kilometrage) {
        this.kilometrage = kilometrage;
    }

    public String getAnnee_circ() {
        return annee_circ;
    }

    public void setAnnee_circ(String annee_circ) {
        this.annee_circ = annee_circ;
    }

    public String getNum_assurance() {
        return num_assurance;
    }

    public void setNum_assurance(String num_assurance) {
        this.num_assurance = num_assurance;
    }

    public String getNum_enregistrement() {
        return num_enregistrement;
    }

    public void setNum_enregistrement(String num_enregistrement) {
        this.num_enregistrement = num_enregistrement;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
