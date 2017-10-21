package cm.mindef.sed.sicre.mobile.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 14/10/17.
 */

public class Perquisition implements Serializable{

    private String id;
    private String affaireId;
    private String affaire;
    private String description;
    private List<String> textLinks;
    private List<String> imageLinks;
    private List<String> audioLinks;
    private List<String> videoLinks;


    private Perquisition(String id, String affaireId, String affaire, String description, List<String> textLinks,
                        List<String> imageLinks, List<String> audioLinks, List<String> videoLinks) {
        this.id = id;
        this.affaireId = affaireId;
        this.affaire = affaire;
        this.description = description;
        this.textLinks = textLinks;
        this.imageLinks = imageLinks;
        this.audioLinks = audioLinks;
        this.videoLinks = videoLinks;
    }

    public static Perquisition getInstance(JSONObject jsonObject){
        try {
            String id = jsonObject.getString("id");
            String affaireId = jsonObject.getString("affaire_id");
            String affaire = jsonObject.getString("affaire");
            String description = jsonObject.getString("description");

            JSONArray textArray = jsonObject.getJSONArray("preuves_texte");
            List<String> texts = new ArrayList<>();
            for (int i=0; i<textArray.length(); i++){
                texts.add(textArray.getString(i));
            }

            JSONArray imageArray = jsonObject.getJSONArray("preuves_image");
            List<String> images = new ArrayList<>();
            for (int i=0; i<imageArray.length(); i++){
                images.add(imageArray.getString(i));
            }


            JSONArray soundArray = jsonObject.getJSONArray("preuves_son");
            List<String> sons = new ArrayList<>();
            for (int i=0; i<soundArray.length(); i++){
                sons.add(soundArray.getString(i));
            }

            JSONArray videoArray = jsonObject.getJSONArray("preuves_video");
            List<String> videos = new ArrayList<>();
            for (int i=0; i<videoArray.length(); i++){
                videos.add(videoArray.getString(i));
            }

            return new Perquisition(id, affaireId, affaire, description, texts, images, sons, videos);
        } catch (JSONException e) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public String getAffaireId() {
        return affaireId;
    }

    public String getAffaire() {
        return affaire;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTextLinks() {
        return textLinks;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public List<String> getAudioLinks() {
        return audioLinks;
    }

    public List<String> getVideoLinks() {
        return videoLinks;
    }


    @Override
    public String toString() {
        return "\n\n\nPerquisition{" +
                "id='" + id + '\'' +
                ", affaireId='" + affaireId + '\'' +
                ", affaire='" + affaire + '\'' +
                ", description='" + description + '\'' +
                ", textLinks=" + textLinks +
                ", imageLinks=" + imageLinks +
                ", audioLinks=" + audioLinks +
                ", videoLinks=" + videoLinks +
                '}' + "\n\n\n";
    }

    public void addText(String s) {
        textLinks.add(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Perquisition that = (Perquisition) o;

        if (!id.equals(that.id)) return false;
        return affaireId.equals(that.affaireId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + affaireId.hashCode();
        return result;
    }
}
