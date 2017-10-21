package cm.mindef.sed.sicre.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.domain.Affaire;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.domain.Perquisition;

/**
 * Created by root on 15/10/17.
 */

public class AffaireAdapter extends BaseAdapter {
    private Context context;
    private List<Affaire> affaires;

    public AffaireAdapter(Context context, List<Affaire> affaires) {
        this.context = context;
        this.affaires = affaires;
    }

    @Override
    public int getCount() {
        return affaires.size();
    }

    @Override
    public Object getItem(int position) {
        return affaires.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater li=(LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.affaire_layout, null);
        }

        Affaire affaireCurrente = affaires.get(position);

        TextView affaire = convertView.findViewById(R.id.affaire);
        affaire.setText(affaireCurrente.getName());

        TextView description = convertView.findViewById(R.id.description);
        description.setText(affaireCurrente.getDescription());

        return convertView;
    }
}

