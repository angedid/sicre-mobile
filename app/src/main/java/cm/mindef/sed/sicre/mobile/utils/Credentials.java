package cm.mindef.sed.sicre.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by root on 13/10/17.
 */

public class Credentials {

    private String username; //admin
    private String password; //admin
    private String stayConnected;

    private Credentials(String username, String password, String stayConnected) {
        setUsername(username);
        setPassword(password);
        setStayConnected(stayConnected);
    }


    public static Credentials getInstance(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE);
        String username = prefs.getString(Constant.USERNAME, null);
        String password = prefs.getString(Constant.PASSWORD, null);
        String stayConnected = prefs.getString(Constant.STAY_CONNECTED, null);
        return new Credentials(username, password, stayConnected);
    }

    public static void saveCredential(Context context, String username, String password, String stayConnected){

        SharedPreferences.Editor editor = context.getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE).edit();
        editor.putString(Constant.USERNAME, username);
        editor.putString(Constant.PASSWORD, password);
        editor.putString(Constant.STAY_CONNECTED, stayConnected);
        editor.commit();

    }

    public static void disconnect(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constant.PreferenceCredential, MODE_PRIVATE).edit();
        editor.remove(Constant.USERNAME);
        editor.remove(Constant.PASSWORD);
        editor.remove(Constant.STAY_CONNECTED);
        editor.commit();
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

    public String getStayConnected() {
        return stayConnected;
    }

    private void setStayConnected(String stayConnected) {
        this.stayConnected = stayConnected;
    }
}
