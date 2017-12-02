package cm.mindef.sed.sicre.mobile;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import cm.mindef.sed.sicre.mobile.domain.Individu;
import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;
import dmax.dialog.SpotsDialog;

public class FicheIndividuActivity extends AppCompatActivity {

    private Individu individuCourent;
    public static  AppCompatActivity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_individu);
        thisActivity = this;

         individuCourent = ResultSearchLiteActivity.selectedIndividu;//(Individu) getIntent().getExtras().get(Constant.INDIVIDU);


        RequestQueue requestQueue = MySingleton.getRequestQueue(getApplicationContext());
        Credentials credentials = Credentials.getInstance(getApplicationContext());

        final ProgressBar pbg = findViewById(R.id.progress_photoG);

        if(pbg.getVisibility() == View.GONE){
            pbg.setVisibility(View.VISIBLE);
        }

        final ImageView photoG = findViewById(R.id.photoG);
        // Initialize a new ImageRequest
        String photoGUrl = individuCourent.getIdentification().getProfileG() + "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId(); //+ "&" + Constant.PHOTO + "=" + Constant.PROFILE_GAUCHE; // Image URL
        Log.e("PHOTOGURLLLLLLLLLLL", photoGUrl);
        ImageRequest imageRequestG = new ImageRequest(
                photoGUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbg.getVisibility() == View.VISIBLE){
                            pbg.setVisibility(View.GONE);
                        }
                        photoG.setImageBitmap(response);
                        //circularImageView.setImageURI(new );
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pbg.getVisibility() == View.VISIBLE){
                            pbg.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        final ProgressBar pbd = findViewById(R.id.progress_photoD);

        if(pbd.getVisibility() == View.GONE){
            pbd.setVisibility(View.VISIBLE);
        }

        final ImageView photoD = findViewById(R.id.photoD);

        String photoDUrl = individuCourent.getIdentification().getProfileD() + "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId();// + "&" + Constant.PHOTO + "=" + Constant.PROFILE_DROIT; // Image URL
        Log.e("PHOTODURLLLLLLLLLLL", photoDUrl);
        // Initialize a new ImageRequest
        ImageRequest imageRequestD = new ImageRequest(
                photoDUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbd.getVisibility() == View.VISIBLE){
                            pbd.setVisibility(View.GONE);
                        }
                        photoD.setImageBitmap(response);
                        //circularImageView.setImageURI(new );
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pbd.getVisibility() == View.VISIBLE){
                            pbd.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        final ProgressBar pbf = findViewById(R.id.progress_photoFace);

        if(pbf.getVisibility() == View.GONE){
            pbf.setVisibility(View.VISIBLE);
        }

        final ImageView photoF = findViewById(R.id.photoFace);
        // Initialize a new ImageRequest

        String photoFUrl = individuCourent.getIdentification().getFace() + "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId();// + "&" + Constant.PHOTO + "=" + Constant.FACE; // Image URL
        Log.e("PHOTOFURLLLLLLLLLLL", photoFUrl);

        ImageRequest imageRequestF = new ImageRequest(
                photoFUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbf.getVisibility() == View.VISIBLE){
                            pbf.setVisibility(View.GONE);
                        }
                        photoF.setImageBitmap(response);
                        //circularImageView.setImageURI(new );
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pbf.getVisibility() == View.VISIBLE){
                            pbf.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );


        final ProgressBar pbp = findViewById(R.id.progress_photoPortrait);

        if(pbp.getVisibility() == View.GONE){
            pbp.setVisibility(View.VISIBLE);
        }

        final ImageView photoP = findViewById(R.id.photoPortrait);
        // Initialize a new ImageRequest

        String photoPUrl = individuCourent.getIdentification().getPortrait() + "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId();// + "&" + Constant.PHOTO + "=" + Constant.PORTRAIT; // Image URL
        Log.e("PHOTOPURLLLLLLLLLLL", photoPUrl);

        ImageRequest imageRequestP = new ImageRequest(
                photoPUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbp.getVisibility() == View.VISIBLE){
                            pbp.setVisibility(View.GONE);
                        }
                        photoP.setImageBitmap(response);
                        //circularImageView.setImageURI(new );
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pbp.getVisibility() == View.VISIBLE){
                            pbp.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequestG);
        requestQueue.add(imageRequestD);
        requestQueue.add(imageRequestF);
        requestQueue.add(imageRequestP);





        TextView nom = (TextView)findViewById(R.id.nom);
        nom.setText("  " + individuCourent.getPerson().getName());

        TextView prenom = (TextView)findViewById(R.id.prenom);
        prenom.setText("  " + individuCourent.getPerson().getSurname());

        TextView alias = (TextView)findViewById(R.id.alias);
        alias.setText("  " + individuCourent.getPerson().getAlias());

        TextView civilite = (TextView)findViewById(R.id.civilite);
        String str_civilite = (individuCourent.getPerson().getCivility().equals(Constant.M))?getString(R.string.monsieur):getString(R.string.madame);
        civilite.setText("  " + str_civilite);

        TextView status = (TextView)findViewById(R.id.status);
        status.setText("  " + individuCourent.getPerson().getStatus());

        TextView date_naissance = (TextView)findViewById(R.id.date_naissance);
        date_naissance.setText("  " + individuCourent.getPerson().getDateOfBirth());

        TextView lieu_naissance = (TextView)findViewById(R.id.lieu_naissance);
        lieu_naissance.setText("  " + individuCourent.getPerson().getPlaceOfBirth());

        TextView pays_dorigine = (TextView)findViewById(R.id.pays_dorigine);
        pays_dorigine.setText("  " + individuCourent.getPerson().getPlaceOfBirth());

        TextView profession = (TextView)findViewById(R.id.profession);
        profession.setText("  " + individuCourent.getIdentification().getProfession());

        TextView cni = (TextView)findViewById(R.id.cni);
        cni.setText("  " + individuCourent.getIdentification().getCni());

        TextView cni_date = (TextView)findViewById(R.id.cni_date);
        cni_date.setText("  " + individuCourent.getIdentification().getCniDate());


        TextView cni_expire = (TextView)findViewById(R.id.cni_expire);
        cni_expire.setText("  " + individuCourent.getIdentification().getCniExpire());

        TextView cni_autorite = (TextView)findViewById(R.id.cni_autorite);
        cni_autorite.setText("  " + individuCourent.getIdentification().getCniAutorite());

        TextView antecedent = (TextView)findViewById(R.id.antecedent);
        antecedent.setText("  " + individuCourent.getAntecedant());

        FloatingActionButton btn_signal = findViewById(R.id.btn_signal);
        btn_signal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signaler signaler = new Signaler();
                Credentials credentials = Credentials.getInstance(getApplicationContext());
                String query = null;
                try {
                    User user = ((HomeActivity)HomeActivity.thisActivity).getUser();
                    query =  Constant.USERNAME + "=" + credentials.getUsername() + "&" +
                            Constant.PASSWORD + "=" + credentials.getPassword() +
                            "&id=" +  URLEncoder.encode(individuCourent.getId(), "UTF-8") +
                            "&objet=" + URLEncoder.encode("individu" , "UTF-8") +
                            "&titre=" + URLEncoder.encode("Individu trouvé" , "UTF-8")
                            +"&message=" + URLEncoder.encode("L Individu "  + individuCourent.getPerson().getName() + " "
                            + individuCourent.getPerson().getSurname() + " a été retrouvé au coordonnées (" + Constant.latitudeNetwork + ", " + Constant.longitudeNetwork + ") par ("
                            + user.getName() + ")", "UTF-8")
                            +"&porte=" + URLEncoder.encode("1", "UTF-8")
                            + "&" + Constant.LATITUDE + "=" + Constant.latitudeNetwork
                            + "&" + Constant.LONGITUDE + "=" + Constant.longitudeNetwork;
                    signaler.execute(Constant.URL_LINK + Constant.SIGNALER_LINK, query);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private class Signaler extends AsyncTask<String, Void, String> {
        private AlertDialog dialog;
        private int serverResponseCode;

        public Signaler(){
            dialog = new SpotsDialog(thisActivity);
        }
        @Override
        protected String doInBackground(String... params) {

            String url_string = params[0];

            Log.e("urlllllllllllll", url_string);
            String returnVal = "";
            String query = params[1];
            // Log.e("query", query);

            //String sourceFileUri = uir_string;

            HttpURLConnection conn = null;


            try {

                String upLoadServerUri = url_string;/*"http://idea-cm.club/magasino/enregistrement.php";*/

                URL url = new URL(upLoadServerUri);

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setRequestMethod("POST");

                Log.e("3333333333333333333333","333333333333333333333333333333333333333333333333");
                Log.e("3333333333333333333333","query " + query);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
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
                        return Constant.KO + " 1           " + e.getMessage();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                return Constant.KO + " 2         " + e.getMessage();
                            }
                        }
                    }
                    in.close();
                    Log.e("55555555555555555","55555555555555555555555555555555555555555555555");
                    //os.close();
                    returnVal = sb.toString();

                }else {
                    Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                    returnVal =  Constant.KO + " 3     " ;
                }


            } catch (MalformedURLException e) {

                // dialog.dismiss();
                e.printStackTrace();
                return Constant.KO +  " 4    " + e.getMessage();

            } catch (ProtocolException e) {
                e.printStackTrace();
                return Constant.KO +  " 5   " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return Constant.KO +  " 6    " + e.toString();
            }
            // dialog.dismiss();

            // End else block



            return returnVal;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (serverResponseCode >= 200 && serverResponseCode < 300){
                Toast.makeText(getApplicationContext(), getString(R.string.successful_signal), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }

            Log.e("returnValllll", result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getString(R.string.chargement));
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
