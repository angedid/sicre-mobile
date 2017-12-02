package cm.mindef.sed.sicre.mobile.domain;

/**
 * Created by nkalla on 07/11/17.
 */

public class DocType {

    private String id, description;

    public DocType(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
