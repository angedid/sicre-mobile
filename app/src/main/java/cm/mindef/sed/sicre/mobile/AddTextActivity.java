package cm.mindef.sed.sicre.mobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;

public class AddTextActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView title, result;
    private EditText text;
    private ProgressBar loader;
    private Button btn_enregistrer;

    private Perquisition perquisition;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        url ="https://jsonplaceholder.typicode.com/posts/1";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Bundle bundle = getIntent().getExtras();
        perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
        title = findViewById(R.id.title);
        title.setText(getString(R.string.enregistrement_preuve_txt) + " dans " + perquisition.getDescription());

        text = findViewById(R.id.text);

        loader = findViewById(R.id.loader);
        result = findViewById(R.id.result);
        btn_enregistrer = findViewById(R.id.btn_enregistrer);

        btn_enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), getString(R.string.invalide_data), Toast.LENGTH_LONG).show();
                    return;
                }

                if (loader.getVisibility() == View.GONE){
                    loader.setVisibility(View.VISIBLE);
                    result.setVisibility(View.GONE);
                }
                RequestQueue requestQueue = MySingleton.getRequestQueue(getApplicationContext());
                final String texte = text.getText().toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (loader.getVisibility() == View.VISIBLE){
                            loader.setVisibility(View.GONE);
                            result.setVisibility(View.VISIBLE);
                            result.setText(response.toString());
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplication(), "Error..." + error.getCause() + " ", Toast.LENGTH_LONG).show();
                        if (loader.getVisibility() == View.VISIBLE){
                            loader.setVisibility(View.GONE);
                            result.setVisibility(View.VISIBLE);
                            result.setTextColor(Color.RED);
                            result.setText(texte + "  " + error.getMessage()  + " Ce ci est un message d'erreur");
                            perquisition.addPreuve(texte + "  " + error.getMessage()  + " Ce ci est un message d'erreur");
                            Intent data = new Intent();
                            data.putExtra(Constant.PERQUISITION, perquisition);
                            setResult(Activity.RESULT_OK, data);
                            finish();

                        }
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}
