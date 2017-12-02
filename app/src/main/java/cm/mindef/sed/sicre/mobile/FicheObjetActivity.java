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

import cm.mindef.sed.sicre.mobile.adapters.ObjetAdapter;
import cm.mindef.sed.sicre.mobile.domain.Objet;
import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;
import dmax.dialog.SpotsDialog;

public class FicheObjetActivity extends AppCompatActivity {
    private Objet objetCourent;
    public static  AppCompatActivity thisActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_objet);
        thisActivity = this;

        objetCourent = ResultSearchLiteActivity.selectedObject;//(Objet) getIntent().getExtras().get(Constant.OBJECT);
        RequestQueue requestQueue = MySingleton.getRequestQueue(getApplicationContext());
        Credentials credentials = Credentials.getInstance(getApplicationContext());


        final ProgressBar pb = findViewById(R.id.progress_photo);
        final ImageView photo = findViewById(R.id.photo);

        if (pb.getVisibility() == View.GONE) {
            pb.setVisibility(View.VISIBLE);
        }

        pb.setVisibility(View.GONE);

        String photoGUrl = objetCourent.getPhoto() + "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword() + "&" + Constant.ID + "=" + objetCourent.getId();// + "&" + Constant.PHOTO + "=" + Constant.PHOTO; // Image URL
        Log.e("PHOTOGURLLLLLLLLLLL", photoGUrl);
        ImageRequest imageRequestG = new ImageRequest(
                photoGUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if (pb.getVisibility() == View.VISIBLE) {
                            pb.setVisibility(View.GONE);
                        }
                        photo.setImageBitmap(response);
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
                        if (pb.getVisibility() == View.VISIBLE) {
                            pb.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(imageRequestG);


        TextView libele = (TextView) findViewById(R.id.libele);
        libele.setText("  " + objetCourent.getLibele());

        TextView description = (TextView) findViewById(R.id.description);
        description.setText("  " + objetCourent.getDescription());

        TextView status = (TextView) findViewById(R.id.status);
        status.setText("  " + objetCourent.getStatus());

        TextView wanted_num = (TextView) findViewById(R.id.wanted_num);
        wanted_num.setText("  " + objetCourent.getWanted_num());

        TextView raison = (TextView) findViewById(R.id.raison);
        raison.setText("  " + objetCourent.getWanted_desc());

        TextView carte_grise = (TextView) findViewById(R.id.carte_grise);
        carte_grise.setText("  " + objetCourent.getCarte_grise());


        TextView matricule = (TextView) findViewById(R.id.matricule);
        matricule.setText("  " + objetCourent.getMatricule());

        TextView puissance = (TextView) findViewById(R.id.puissance);
        puissance.setText("  " + objetCourent.getPuissance());

        TextView kilometrage = (TextView) findViewById(R.id.kilometrage);
        kilometrage.setText("  " + objetCourent.getKilometrage());

        TextView num_assurance = (TextView) findViewById(R.id.num_assurance);
        num_assurance.setText("  " + objetCourent.getNum_assurance());


        FloatingActionButton btn_signal = findViewById(R.id.btn_signal);
        btn_signal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signaler signaler = new Signaler();
                Credentials credentials = Credentials.getInstance(getApplicationContext());
                String query = null;
                try {
                    User user = ((HomeActivity)HomeActivity.thisActivity).getUser();
                    String artcle =  (!objetCourent.getMatricule().equals("")) ? "Le vehicule" : "L objet";
                    query = Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                            Constant.PASSWORD + "=" + credentials.getPassword() +
                            "&id=" + URLEncoder.encode(objetCourent.getId(), "UTF-8") +
                            "&objet=" + URLEncoder.encode(!objetCourent.getMatricule().equals("") ? "vehicule" : "objet", "UTF-8") +
                            "&titre=" + URLEncoder.encode("Attention Objet trouvé", "UTF-8") +
                            "&message=" + URLEncoder.encode(artcle + " " + objetCourent.getLibele() +
                            " a été retrouvé au coordonnées (" + Constant.latitudeNetwork + ", " + Constant.longitudeNetwork + ") par ("
                            + user.getName() + ")" , "UTF-8") +
                            "&porte=" + URLEncoder.encode("1", "UTF-8") +
                            "&" + Constant.LATITUDE + "=" + Constant.latitudeNetwork
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

        public Signaler() {
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

                Log.e("3333333333333333333333", "333333333333333333333333333333333333333333333333");
                Log.e("3333333333333333333333", "query " + query);

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
                    Log.e("55555555555555555", "55555555555555555555555555555555555555555555555");
                    //os.close();
                    returnVal = sb.toString();

                } else {
                    Log.e(Constant.KO + " 3     ", Constant.KO + " 3     ");
                    returnVal = Constant.KO + " 3     ";
                }


            } catch (MalformedURLException e) {

                // dialog.dismiss();
                e.printStackTrace();
                return Constant.KO + " 4    " + e.getMessage();

            } catch (ProtocolException e) {
                e.printStackTrace();
                return Constant.KO + " 5   " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return Constant.KO + " 6    " + e.toString();
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

            if (serverResponseCode >= 200 && serverResponseCode < 300) {
                Toast.makeText(getApplicationContext(), getString(R.string.successful_signal), Toast.LENGTH_SHORT).show();
            } else {
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
