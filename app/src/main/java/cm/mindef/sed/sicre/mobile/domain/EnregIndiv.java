package cm.mindef.sed.sicre.mobile.domain;

/**
 * Created by nkalla on 28/11/17.
 */

public class EnregIndiv {

    private String id, name, surname, doctype, docnum, raison, path, type;
    private double latitude, longitude;

    public EnregIndiv(String id, String name, String surname, String doctype, String docnum, String raison, String path, String type, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.doctype = doctype;
        this.docnum = docnum;
        this.raison = raison;
        this.path = path;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getDocnum() {
        return docnum;
    }

    public void setDocnum(String docnum) {
        this.docnum = docnum;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "EnregIndiv{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", doctype='" + doctype + '\'' +
                ", docnum='" + docnum + '\'' +
                ", raison='" + raison + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
