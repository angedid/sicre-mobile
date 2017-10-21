package cm.mindef.sed.sicre.mobile.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 13/10/17.
 */

public class Individu {
    private String Id;
    private String name;
    private String surname;
    private String alias;
    private String civility;
    private String dateOfBirth;
    private String placeOfBirth;
    private String father;
    private String mother;
    private String raisonDescription;
    private String raisonId;
    private String photoLink;


    private Individu(String id, String name, String surname, String alias, String civility,
                    String dateOfBirth, String placeOfBirth, String father, String mother,
                    String raisonDescription, String raisonId, String photoLink) {
        Id = id;
        this.name = name;
        this.surname = surname;
        this.alias = alias;
        this.civility = civility;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.father = father;
        this.mother = mother;
        this.raisonDescription = raisonDescription;
        this.raisonId = raisonId;
        this.photoLink = photoLink;
    }

    public static Individu getInstance(JSONObject jsonObject){
        try {
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("nom");
            String surname = jsonObject.getString("prenom");
            String alias = jsonObject.getString("alias");
            String civility = jsonObject.getString("civilite");
            String dateOfBirth = jsonObject.getString("date_naissance");
            String placeOfBirth = jsonObject.getString("lieu_naissance");
            String father = jsonObject.getString("pere");
            String mother = jsonObject.getString("mere");

            JSONObject raison = jsonObject.getJSONObject("raison");
            String raisonDescription = raison.getString("description");
            String raisonId = raison.getString("affaire");
            String photoLink = jsonObject.getString("photo");

            return new Individu(id, name, surname, alias, civility, dateOfBirth, placeOfBirth, father, mother, raisonDescription, raisonId, photoLink);
        } catch (JSONException e) {
            return null;
        }
    }


    public final String getId() {
        return Id;
    }

    public final String getName() {
        return name;
    }

    public final String getSurname() {
        return surname;
    }

    public final String getAlias() {
        return alias;
    }

    public final String getCivility() {
        return civility;
    }

    public final String getDateOfBirth() {
        return dateOfBirth;
    }

    public final String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public final String getFather() {
        return father;
    }

    public final String getMother() {
        return mother;
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
}
