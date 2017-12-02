package cm.mindef.sed.sicre.mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import cm.mindef.sed.sicre.mobile.domain.User;
import cm.mindef.sed.sicre.mobile.utils.Constant;

public class NotificationActivity extends AppCompatActivity {

    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //TextView tv = findViewById(R.id.tv);
        RemoteMessage remoteMessage = (RemoteMessage) getIntent().getExtras().get(Constant.REMOTE_MESSAGE);
        //User user = (User) getIntent().getExtras().get(Constant.USER);
        //tv.setText(remoteMessage.toString() + " ||||||| " + user.toString());

        type = ""; //individu: type = 0; Vehicule: type = 1;  objet: type = 2
        LinearLayout individu = findViewById(R.id.individu);

        LinearLayout objet = findViewById(R.id.objet);

        LinearLayout newalert = findViewById(R.id.newalert);

        String body = remoteMessage.getNotification().getBody();

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(body);
            type = jsonObject.getString("type");

            TextView message_signaler = findViewById(R.id.message_signaler);
            String message = jsonObject.getString("message");
            message_signaler.setText(message);
        } catch (JSONException e) {
            Log.e("ALTROOOOOOOOO", "" + e.getMessage());
            type = "new";
        }

        if (type.equals("vehicule") || type.equals("objet")){

             individu.setVisibility(View.GONE);
            objet.setVisibility(View.VISIBLE);
            newalert.setVisibility(View.GONE);

            try {
                JSONObject corps = jsonObject.getJSONObject("body");

                String key = type.equals("objet")? "libelle":"libele";
                TextView libele = findViewById(R.id.libele);
                libele.setText("  " + corps.getString(key));

                TextView description = findViewById(R.id.description);
                description.setText("  " + corps.getString("description"));

                String key1 = type.equals("objet")? "statut":"status";
                TextView status_objet = findViewById(R.id.status_objet);
                status_objet.setText("  " + corps.getString(key1));


                String key2 = type.equals("objet")? "num_avis":"wanted_num";
                TextView num_avis = findViewById(R.id.num_avis);
                num_avis.setText("  " + corps.getString(key2));

                if (type.equals("vehicule")){
                    JSONObject identification = jsonObject.getJSONObject("identification");
                    TextView carte_grise = findViewById(R.id.carte_grise);
                    carte_grise.setText("  " + identification.getString("carte_grise"));

                    TextView matricule = findViewById(R.id.matricule);
                    matricule.setText("  " + identification.getString("matricule"));

                    TextView puissance = findViewById(R.id.puissance);
                    puissance.setText("  " + identification.getString("puissance"));

                    TextView kilometrage = findViewById(R.id.kilometrage);
                    kilometrage.setText("  " + identification.getString("kilometrage"));

                    TextView num_assurance = findViewById(R.id.num_assurance);
                    num_assurance.setText("  " + identification.getString("num_assurance"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (type.equals("individu")){

            individu.setVisibility(View.VISIBLE);
            objet.setVisibility(View.GONE);
            newalert.setVisibility(View.GONE);

            try {

                JSONObject corps = jsonObject.getJSONObject("body");


                TextView nom = findViewById(R.id.nom);
                String nom_ = corps.getString("nom");
                String prenom = " " + corps.getString("prenom");
                nom.setText("  " + nom_ + "  " + prenom);

                TextView alias = findViewById(R.id.alias);
                alias.setText("  " + corps.getString("surnom"));

                TextView civilite = findViewById(R.id.civilite);
                civilite.setText("  " + corps.getString("sexe"));

                TextView date_naissance = findViewById(R.id.date_naissance);
                date_naissance.setText("  " + corps.getString("date_naissance"));

                TextView lieu_naissance = findViewById(R.id.lieu_naissance);
                lieu_naissance.setText("  " + corps.getString("lieu_naissance"));

                TextView pays_dorigine = findViewById(R.id.pays_dorigine);
                pays_dorigine.setText("  " + corps.getString("pays"));

                TextView cni = findViewById(R.id.cni);
                cni.setText("  " + corps.getString("docnum"));

                TextView cni_expire = findViewById(R.id.cni_expire);
                cni_expire.setText("  " + corps.getString("date_expiration"));


                TextView cni_autorite = findViewById(R.id.cni_autorite);
                cni_autorite.setText("  " + corps.getString("autorite_doc"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (type.equals("new")){
            individu.setVisibility(View.GONE);
            objet.setVisibility(View.GONE);
            newalert.setVisibility(View.VISIBLE);
            TextView title = findViewById(R.id.title);
            title.setText(remoteMessage.getNotification().getTitle());
            TextView textView = findViewById(R.id.body);
            textView.setText(body);
        }
    }
}
