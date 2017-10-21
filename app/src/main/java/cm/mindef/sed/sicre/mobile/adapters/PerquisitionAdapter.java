package cm.mindef.sed.sicre.mobile.adapters;

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

public class PerquisitionAdapter extends BaseAdapter{
    private Context context;
    private List<Perquisition> perquisitions;

    public PerquisitionAdapter(Context context, List<Perquisition> perquisitions) {
        this.context = context;
        this.perquisitions = perquisitions;
    }

    @Override
    public int getCount() {
        return perquisitions.size();
    }

    @Override
    public Object getItem(int position) {
        return perquisitions.get(position);
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
            convertView = li.inflate(R.layout.perquisition_layout, null);
        }

        Perquisition perquisitionCourente = perquisitions.get(position);

        TextView affaire = convertView.findViewById(R.id.affaire);
        affaire.setText(perquisitionCourente.getAffaire());

        TextView description = convertView.findViewById(R.id.description);
        description.setText(perquisitionCourente.getDescription());

        return convertView;
    }
}
