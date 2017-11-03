package cm.mindef.sed.sicre.mobile.domain;

import java.io.Serializable;

/**
 * Created by nkalla on 02/11/17.
 */

public class Access implements Serializable{

    private Can create, read, update, delete;

    public Access(Can create, Can read, Can update, Can delete) {
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }


    public Can getCreate() {
        return create;
    }

    public void setCreate(Can create) {
        this.create = create;
    }

    public Can getRead() {
        return read;
    }

    public void setRead(Can read) {
        this.read = read;
    }

    public Can getUpdate() {
        return update;
    }

    public void setUpdate(Can update) {
        this.update = update;
    }

    public Can getDelete() {
        return delete;
    }

    public void setDelete(Can delete) {
        this.delete = delete;
    }
}
