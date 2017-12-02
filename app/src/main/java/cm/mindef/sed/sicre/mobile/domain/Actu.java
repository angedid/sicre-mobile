package cm.mindef.sed.sicre.mobile.domain;

import java.io.Serializable;

/**
 * Created by root on 14/03/17.
 */

public class Actu implements Serializable{
    int id;
    String title, message, publisher, date;
    double latitude, longitude;

    public Actu(int id, String title, String message, String publisher, String date, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.publisher = publisher;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
