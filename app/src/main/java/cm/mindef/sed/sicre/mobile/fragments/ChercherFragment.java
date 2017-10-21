package cm.mindef.sed.sicre.mobile.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.SearchActivity;
import cm.mindef.sed.sicre.mobile.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChercherFragment extends Fragment {


    private View fragmentView;

    private Button btn_chercher_individu, btn_chercher_vehicule, btn_chercher_objet;



    public ChercherFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_chercher, container, false);
        fragmentView = view;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        btn_chercher_individu = (Button) fragmentView.findViewById(R.id.btn_chercher_individu);
        btn_chercher_vehicule = (Button) fragmentView.findViewById(R.id.btn_chercher_vehicule);
        btn_chercher_objet = (Button) fragmentView.findViewById(R.id.btn_chercher_objet);

        btn_chercher_individu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSearchInterface(view);
            }
        });


        btn_chercher_vehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSearchInterface(view);
            }
        });

        btn_chercher_objet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSearchInterface(view);
            }
        });
    }

    public void gotoSearchInterface(View view){
        int id = view.getId();
        String type = "";
        switch (id){
            case R.id.btn_chercher_individu:
                type = "" + Constant.SEARCH_INDIVIDU;
                break;
            case R.id.btn_chercher_vehicule:
                type = "" + Constant.SEARCH_VEHICULE;
                break;
            case R.id.btn_chercher_objet:
                type = "" + Constant.SEARCH_OBJECT;
                break;
            default:
                break;
        }
        if (type.equals("")){

        }else {
            Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }
}
