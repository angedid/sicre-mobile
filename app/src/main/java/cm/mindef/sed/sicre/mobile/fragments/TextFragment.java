package cm.mindef.sed.sicre.mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import cm.mindef.sed.sicre.mobile.AddTextActivity;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.adapters.PerquisitionTextAdapter;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;
import cm.mindef.sed.sicre.mobile.utils.Constant;

import static android.app.Activity.RESULT_OK;

/**
 * Created by root on 15/10/17.
 */

public class TextFragment extends Fragment {

    private View rootView;

    private ListView text_list_view;
    private Perquisition perquisition;
    private PerquisitionTextAdapter perquisitionTextAdapter;
    private Button add_preuve_text;

    public TextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        rootView = view;
        TextView affaire = rootView.findViewById(R.id.affaire);

        Bundle bundle = getActivity().getIntent().getExtras();
        perquisition = (Perquisition) bundle.get(Constant.PERQUISITION);
        //Log.e("PERQUISITION", perquisition.toString());
        affaire.setText(perquisition.getAffaire());

        text_list_view = rootView.findViewById(R.id.text_list_view);
        //Log.e("SIZEEEEEEE", "" + perquisition.getTextLinks().size());
        perquisitionTextAdapter = new PerquisitionTextAdapter(getActivity().getApplicationContext(), perquisition.getTextLinks());
        text_list_view.setAdapter(perquisitionTextAdapter);

        add_preuve_text = rootView.findViewById(R.id.add_preuve_text);
        add_preuve_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddTextActivity.class);
                intent.putExtra(Constant.PERQUISITION, perquisition);
                startActivityForResult(intent, Constant.REQUEST_CODE_FOR_ADD_PERQUISITION);

            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CODE_FOR_ADD_PERQUISITION) {
            if (resultCode == RESULT_OK) {
                perquisition = (Perquisition) data.getExtras().get(Constant.PERQUISITION);
                perquisitionTextAdapter = new PerquisitionTextAdapter(getActivity().getApplicationContext(), perquisition.getTextLinks());
                text_list_view.setAdapter(perquisitionTextAdapter);
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
