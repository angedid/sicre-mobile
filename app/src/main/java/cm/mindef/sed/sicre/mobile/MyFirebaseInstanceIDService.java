package cm.mindef.sed.sicre.mobile;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import dmax.dialog.SpotsDialog;

/**
 * Created by nkalla on 06/11/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFirebaseIIDService";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Constant.firebaseToken = refreshedToken;
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        //sendRegistrationToServer(refreshedToken);

        //fxklr_brs6k:APA91bFOXhFUcJjgr0EaDNq6yh1LXQuhAduN7Jyfjxz8FORZuTTc3A-gARRMZ-GktG04vaQAPraC6YXThf8RXuVnGYHnyNNs3ecD_GXh8xELkARBNMS3hJibYHHRlqJB2rWI08jVyURg

        SharedPreferences.Editor editor = getSharedPreferences(Constant.TOKEN, MODE_PRIVATE).edit();
        editor.putString(Constant.TOKEN, refreshedToken);
        editor.commit();


        /*Intent intentForBroadCast = new Intent();
        intentForBroadCast.setAction(Constant.TOKEN);
        intentForBroadCast.putExtra(Constant.TOKEN,refreshedToken);
        sendBroadcast(intentForBroadCast);*/

        Intent iintent = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
        iintent.putExtra(Constant.TOKEN,refreshedToken);
        sendBroadcast(iintent);

/*

        HttpURLConnection conn = null;

        String returnVal = "";
        try {

            @SuppressLint("WifiManagerLeak") WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            String address = info.getMacAddress();
            Credentials credentials = Credentials.getInstance(getApplicationContext());

            String upLoadServerUri =  "http://198.50.199.116:8090/scriptcase/app/SICRE_2/m_alert/" ;
            String query = "" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()
                    + "&mac_address=" + address + "&" + Constant.LATITUDE + "=" + Constant.latitudeNetwork + "&" +
                    Constant.LONGITUDE + "=" + Constant.longitudeNetwork + "&token=" + refreshedToken;
            Log.e("QUERYYYYYY", query);

            URL url = new URL(upLoadServerUri);

            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setRequestMethod("POST");

            Log.e("3333333333333333333333","333333333333333333333333333333333333333333333333");

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            // Responses from the server (code and message)
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            InputStream in = conn.getInputStream();

            if (serverResponseCode >= 200 && serverResponseCode < 300) {

                // messageText.setText(msg);
                //Toast.makeText(ctx, "File Upload Complete.",
                //      Toast.LENGTH_SHORT).show();

                // recursiveDelete(mDirectory1);

                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();
                String line;
                try {
                    br = new BufferedReader(new InputStreamReader(in));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                } catch (IOException e) {
                    returnVal = "1";
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            returnVal = "2";
                        }
                    }
                }
                in.close();
                Log.e("55555555555555555","55555555555555555555555555555555555555555555555");
                //os.close();
                returnVal = sb.toString();
                Log.e("5555returnVal","55555555555555555555555555555555555555555555555" + returnVal);

            }else {
                Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                returnVal =  "3" ;
            }


        } catch (MalformedURLException e) {

            // dialog.dismiss();
            e.printStackTrace();
            returnVal = "4";

        } catch (ProtocolException e) {
            e.printStackTrace();
            returnVal = "5";
        } catch (IOException e) {
            e.printStackTrace();
            returnVal = "6";
        }

//        Toast.makeText(getApplicationContext(), returnVal, Toast.LENGTH_LONG).show();

        Log.e("returnVal", returnVal);*/


    }

}
