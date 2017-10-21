package cm.mindef.sed.sicre.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 13/10/17.
 */

public class Credentials {

    private String username;
    private String password;

    private Credentials(String username, String password) {
        setUsername(username);
        setPassword(password);
    }


    public static Credentials getInstance(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
        String username = prefs.getString(Constant.USERNAME, null);
        String password = prefs.getString(Constant.PASSWORD, null);
        return new Credentials(username, password);
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
