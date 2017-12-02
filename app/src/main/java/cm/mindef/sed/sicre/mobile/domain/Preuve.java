package cm.mindef.sed.sicre.mobile.domain;

/**
 * Created by nkalla on 23/11/17.
 */

public class Preuve {
    private long id;
    private String path;
    private double latitude, longitude;
    private String others;
    private String type;

    public Preuve() {
    }

    public Preuve(long id, String path, double latitude, double longitude, String others, String type) {
        this.id = id;
        this.path = path;
        this.latitude = latitude;
        this.longitude = longitude;
        this.others = others;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        return "Preuve{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", others='" + others + '\'' +
                '}';
    }
}
