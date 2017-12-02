package cm.mindef.sed.sicre.mobile.domain;

/**
 * Created by nkalla on 28/11/17.
 */

public class EnregVehicule {

    private String id, owner, matricule, chassis, marque, titre, cartegrise, path, type;
    private  double latitude, longitude;

    public EnregVehicule(String id, String owner, String matricule, String chassis, String marque, String titre, String cartegrise, String path, String type, double latitude, double longitude) {
        this.id = id;
        this.owner = owner;
        this.matricule = matricule;
        this.chassis = chassis;
        this.marque = marque;
        this.titre = titre;
        this.cartegrise = cartegrise;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCartegrise() {
        return cartegrise;
    }

    public void setCartegrise(String cartegrise) {
        this.cartegrise = cartegrise;
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
}
