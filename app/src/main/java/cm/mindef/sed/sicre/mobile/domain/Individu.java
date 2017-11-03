package cm.mindef.sed.sicre.mobile.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cm.mindef.sed.sicre.mobile.utils.Constant;

/**
 * Created by root on 13/10/17.
 */

public class Individu implements Serializable{
    private String id;
    private Person person;
    private Identification identification;
    private String antecedant;



    public Individu(String id, Person person, Identification identification, String antecedant) {
        this.id = id;
        this.person = person;
        this.identification = identification;
        this.antecedant = antecedant;

    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    private void setPerson(Person person) {
        this.person = person;
    }

    public String getAntecedant() {
        return antecedant;
    }

    private void setAntecedant(String antecedant) {
        this.antecedant = antecedant;
    }

    public Identification getIdentification() {
        return identification;
    }

    private void setIdentification(Identification identification) {
        this.identification = identification;
    }


    public static Person makePersonFromJson(JSONObject jsonObject){
        try {

            String name = (!jsonObject.isNull("name"))?jsonObject.getString("name"):"";
            String surname = (!jsonObject.isNull("firstname"))?jsonObject.getString("firstname"):"";
            String alias = (!jsonObject.isNull("alias"))?jsonObject.getString("alias"):"";
            String dateOfBirth = (!jsonObject.isNull("birth"))?jsonObject.getString("birth"):"";
            String age = (!jsonObject.isNull("age"))?jsonObject.getString("age"):"";
            String placeOfBirth = (!jsonObject.isNull("Lieu"))?jsonObject.getString("Lieu"):"";
            String civility = (!jsonObject.isNull("sex"))?jsonObject.getString("sex"):"";
            String status = (!jsonObject.isNull("status"))?jsonObject.getString("status"):"";

            return new Person(name, surname, alias, civility, dateOfBirth, age, placeOfBirth, status);

        } catch (JSONException e) {
            Log.e("PERSONNE", e.toString());
        }
        return null;

    }

    public static Identification makeIdentificationFromJson(JSONObject jsonObject){
        try {

            String birthCountry = (!jsonObject.isNull("birth_country"))?jsonObject.getString("birth_country"):"";
            String profession = (!jsonObject.isNull("profession"))?jsonObject.getString("profession"):"";
            String cni = (!jsonObject.isNull("cni"))?jsonObject.getString("cni"):"";
            String cniDate = (!jsonObject.isNull("cni_date"))?jsonObject.getString("cni_date"):"";
            String cniExpire = (!jsonObject.isNull("cni_expire"))?jsonObject.getString("cni_expire"):"";
            String cniAutorite = (!jsonObject.isNull("cni_autorite"))?jsonObject.getString("cni_autorite"):"";

            JSONObject jsonObjectPhoto = jsonObject.getJSONObject("photo");

            String photoG = (!jsonObjectPhoto.isNull("profil_g"))?jsonObjectPhoto.getString("profil_g"):"";
            String photoD = (!jsonObjectPhoto.isNull("profil_d"))?jsonObjectPhoto.getString("profil_d"):"";
            String photoF = (!jsonObjectPhoto.isNull("face"))?jsonObjectPhoto.getString("face"):"";
            String photoP = (!jsonObjectPhoto.isNull("portrait"))?jsonObjectPhoto.getString("portrait"):"";

            Log.e("PHOTOOOOOOG", photoG);

            return new Identification(birthCountry, profession, cni, cniDate, cniExpire, cniAutorite, photoG, photoD, photoF, photoP);

        } catch (JSONException e) {
            Log.e("IDENTIFICATION", e.toString());
        }
        return null;
    }

    public static Individu getInstance(JSONObject jsonObject){
        try {

            String id = jsonObject.getString("id");

            JSONObject jsonObject1 = jsonObject.getJSONObject(Constant.INDIVIDU);



            JSONObject jsonObject2 = jsonObject.getJSONObject(Constant.IDENTIFICATION);


            String antecedant1 =  (!jsonObject.isNull("antecedant"))?jsonObject.getString("antecedant"):Constant.RAS;

            return new Individu(id, makePersonFromJson(jsonObject1), makeIdentificationFromJson(jsonObject2), antecedant1);

        } catch (JSONException e) {
            Log.e("INDIVIDUUUUUUUUUUUU", e.toString());
            //e.printStackTrace();
        }
        return null;
    }


    public static class Person{
        private String name;
        private String surname;
        private String alias;
        private String civility;
        private String dateOfBirth;
        private String age;
        private String placeOfBirth;
        private String status;



        private Person(String name, String surname, String alias, String civility, String dateOfBirth, String age, String placeOfBirth, String status) {
            this.setName(name);
            this.setSurname(surname);
            this.setAlias(alias);
            this.setCivility(civility);
            this.setDateOfBirth(dateOfBirth);
            this.setAge(age);
            this.setPlaceOfBirth(placeOfBirth);
            this.setStatus(status);
        }

        public String getName() {
            return name;
        }

        private void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        private void setSurname(String surname) {
            this.surname = surname;
        }

        public String getAlias() {
            return alias;
        }

        private void setAlias(String alias) {
            this.alias = alias;
        }

        public String getCivility() {
            return civility;
        }

        private void setCivility(String civility) {
            this.civility = civility;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        private void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getPlaceOfBirth() {
            return placeOfBirth;
        }

        private void setPlaceOfBirth(String placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
        }

        public String getStatus() {
            return status;
        }

        private void setStatus(String statu) {
            this.status = statu;
        }

        public String getAge() {
            return age;
        }

        private void setAge(String age) {
            this.age = age;
        }
    }

    public static class Identification{
        private String birthCountry;
        private String profession;
        private String cni;
        private String cniDate;
        private String cniExpire;
        private String cniAutorite;
        private String profileG, profileD, portrait, face;

        public Identification(String birthCountry, String profession, String cni, String cniDate,
                              String cniExpire, String cniAutorite, String profileG,  String profileD, String face, String portrait) {
            this.setBirthCountry(birthCountry);
            this.setProfession(profession);
            this.setCni(cni);
            this.setCniDate(cniDate);
            this.setCniExpire(cniExpire);
            this.setCniAutorite(cniAutorite);
            this.profileG = profileG;
            this.profileD = profileD;
            this.face = face;
            this.portrait = portrait;
        }

        public String getBirthCountry() {
            return birthCountry;
        }

        public void setBirthCountry(String birthCountry) {
            this.birthCountry = birthCountry;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getCni() {
            return cni;
        }

        public void setCni(String cni) {
            this.cni = cni;
        }

        public String getCniDate() {
            return cniDate;
        }

        public void setCniDate(String cniDate) {
            this.cniDate = cniDate;
        }

        public String getCniExpire() {
            return cniExpire;
        }

        public void setCniExpire(String cniExpire) {
            this.cniExpire = cniExpire;
        }

        public String getCniAutorite() {
            return cniAutorite;
        }

        public void setCniAutorite(String cniAutorite) {
            this.cniAutorite = cniAutorite;
        }

        public String getProfileG() {
            return profileG;
        }

        public void setProfileG(String profileG) {
            this.profileG = profileG;
        }

        public String getProfileD() {
            return profileD;
        }

        public void setProfileD(String profileD) {
            this.profileD = profileD;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }
    }

}


