package cm.mindef.sed.sicre.mobile.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 20/10/17.
 */

public class Affaire implements Serializable{

    private String id;
    private String name;
    private String description;
    private Set<Perquisition> perquisitions;


    private Affaire(String id, String name, String description, Set<Perquisition> perquisitions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.perquisitions = perquisitions;
    }

    public static Affaire getInstance(JSONObject jsonObject){

        try {
            String anId = jsonObject.getString("id");
            String aName = jsonObject.getString("name");
            String adescription = jsonObject.getString("description");
            Set<Perquisition> aSetOfperquisition = new HashSet<>();
            JSONArray jsonArray = jsonObject.getJSONArray("perquisitions");

            for (int i = 0; i<jsonArray.length(); i++){
                aSetOfperquisition.add(Perquisition.getInstance(jsonArray.getJSONObject(i)));
            }
            return new Affaire(anId, aName, adescription, aSetOfperquisition);

        } catch (JSONException e) {
            return null;
        }

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Perquisition> getPerquisitions() {
        return perquisitions;
    }
}
