package cm.mindef.sed.sicre.mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cm.mindef.sed.sicre.mobile.adapters.IndividuAdapter;
import cm.mindef.sed.sicre.mobile.adapters.LiteIndividuAdapter;
import cm.mindef.sed.sicre.mobile.adapters.LiteObjectAdapter;
import cm.mindef.sed.sicre.mobile.adapters.ObjetAdapter;
import cm.mindef.sed.sicre.mobile.domain.Individu;
import cm.mindef.sed.sicre.mobile.domain.Objet;
import cm.mindef.sed.sicre.mobile.utils.Constant;

public class ResultSearchLiteActivity extends AppCompatActivity {

    private List<Individu> listeDindividus;
    private List<Objet> listeDesObjets;

    private ListView listView;
    private LiteIndividuAdapter individuAdapter;
    private LiteObjectAdapter objetAdapter;

    private TextView searchTitleTextView;
    private String searchTitle;

    private RelativeLayout erroTextView, result_num_view;
    private Toolbar toolbar;
    private int domaine;
    public static Individu selectedIndividu;
    public static Objet selectedObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search_lite);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Bundle bundle = getIntent().getExtras();
        searchTitle = bundle.getString(Constant.SEARCH_REULT_DISPLAY_VALUE);
        domaine = bundle.getInt(Constant.DOMAINE);

        erroTextView = (RelativeLayout) findViewById(R.id.error_view);
        erroTextView.setVisibility(View.GONE);

        result_num_view = (RelativeLayout) findViewById(R.id.result_num_view);
        result_num_view.setVisibility(View.VISIBLE);


        String result = SearchActivity.searchData;// bundle.getString(Constant.RESULT);

        if (domaine == Constant.SEARCH_INDIVIDU){
            listeDindividus = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++){
                    Log.e("getJSONObject" + i + ":  ", jsonArray.getJSONObject(i).toString());
                    Individu individu = Individu.getInstance(jsonArray.getJSONObject(i));
                    if (individu != null)
                        listeDindividus.add(individu);
                }
            } catch (JSONException e) {
                Log.e("RESULT RESEARCH", e.toString());
            }

            if (listeDindividus.size() == 0){
                erroTextView.setVisibility(View.VISIBLE);
                result_num_view.setVisibility(View.GONE);
                TextView error_text_view = erroTextView.findViewById(R.id.error_text_view);
                error_text_view.setText(getString(R.string.aucun_individu_trouve));
            }else{
                erroTextView.setVisibility(View.GONE);
                result_num_view.setVisibility(View.VISIBLE);
                TextView result_num_text_view = result_num_view.findViewById(R.id.result_num_text_view);
                result_num_text_view.setText(getString(R.string.nombre_trouve) + ": " + listeDindividus.size());

                listView = findViewById(R.id.result_list);
                individuAdapter = new LiteIndividuAdapter(this, listeDindividus);
                listView.setAdapter(individuAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), FicheIndividuActivity.class);
                        selectedIndividu = listeDindividus.get(position);
                        //intent.putExtra(Constant.INDIVIDU, listeDindividus.get(position));
                        startActivity(intent);
                    }
                });
            }
        } else if (domaine == Constant.SEARCH_VEHICULE || domaine == Constant.SEARCH_OBJECT){

            erroTextView.setVisibility(View.GONE);
            result_num_view.setVisibility(View.VISIBLE);

            listeDesObjets = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Objet objet = Objet.getInstance(jsonObject.getJSONObject("Objet"));

                    if (objet != null)
                        listeDesObjets.add(objet);
                }
            } catch (JSONException e) {

            }

            if (listeDesObjets.size() == 0){
                erroTextView.setVisibility(View.VISIBLE);
                result_num_view.setVisibility(View.GONE);
                TextView error_text_view = erroTextView.findViewById(R.id.error_text_view);
                error_text_view.setText(getString(R.string.aucun_individu_trouve));
            }else{

                TextView result_num_text_view = result_num_view.findViewById(R.id.result_num_text_view);
                result_num_text_view.setText(getString(R.string.nombre_trouve) + ": " + listeDesObjets.size());
                listView = findViewById(R.id.result_list);
                objetAdapter = new LiteObjectAdapter(this, listeDesObjets);
                listView.setAdapter(objetAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), FicheObjetActivity.class);
                        selectedObject = listeDesObjets.get(position);
                        //intent.putExtra(Constant.OBJECT, listeDesObjets.get(position));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*  Back arrow*/
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

