package cm.mindef.sed.sicre.mobile.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 14/10/17.
 */

public class Objet {

    private String id;
    private String signeParticulier;
    private String titre;
    private String sujet;
    private String description;
    private String marque;
    private String fabricant;
    private String dateDeCreation;
    private String raisonDescription;
    private String raisonId;
    private String photoLink;

    private String matricule;
    private String numeroDeChassie;

    private Objet(String id, String signeParticulier, String titre, String sujet, String description,
                 String marque, String fabricant, String dateDeCreation, String raisonDescription,
                 String raisonId, String photoLink, String matricule, String numeroDeChassie) {
        this.id = id;
        this.signeParticulier = signeParticulier;
        this.titre = titre;
        this.sujet = sujet;
        this.description = description;
        this.marque = marque;
        this.fabricant = fabricant;
        this.dateDeCreation = dateDeCreation;
        this.raisonDescription = raisonDescription;
        this.raisonId = raisonId;
        this.photoLink = photoLink;
        this.matricule = matricule;
        this.numeroDeChassie = numeroDeChassie;
    }

    public static Objet getInstance(JSONObject jsonObject){
        try {
            String id = jsonObject.getString("id");
            String signe_particulier = jsonObject.getString("signe_particulier");
            String titre = jsonObject.getString("titre");
            String sujet = jsonObject.getString("sujet");
            String description = jsonObject.getString("description");
            String marque = jsonObject.getString("marque");
            String fabricant = jsonObject.getString("fabricant");
            String dateDeCreation = jsonObject.getString("date_creation");

            JSONObject raison = jsonObject.getJSONObject("raison");
            String raisonDescription = raison.getString("description");
            String raisonId = raison.getString("affaire");
            String photoLink = jsonObject.getString("photo");

            String matricule = jsonObject.getString("matricule");
            String numero_de_chassie = jsonObject.getString("numero_de_chassie");


            return new Objet(id, signe_particulier, titre, sujet, description, marque, fabricant, dateDeCreation, raisonDescription, raisonId, photoLink, matricule, numero_de_chassie);
        } catch (JSONException e) {
            return null;
        }
    }

    public final String getId() {
        return id;
    }

    public final String getSigneParticulier() {
        return signeParticulier;
    }

    public final String getTitre() {
        return titre;
    }

    public final String getSujet() {
        return sujet;
    }

    public final String getDescription() {
        return description;
    }

    public final String getMarque() {
        return marque;
    }

    public final String getFabricant() {
        return fabricant;
    }

    public final String getDateDeCreation() {
        return dateDeCreation;
    }

    public final String getRaisonDescription() {
        return raisonDescription;
    }

    public final String getRaisonId() {
        return raisonId;
    }

    public final String getPhotoLink() {
        return photoLink;
    }

    public final String getMatricule() {
        return matricule;
    }

    public final String getNumeroDeChassie() {
        return numeroDeChassie;
    }
}
