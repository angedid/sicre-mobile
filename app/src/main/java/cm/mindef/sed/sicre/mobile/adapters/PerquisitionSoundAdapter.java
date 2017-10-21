package cm.mindef.sed.sicre.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.List;

import cm.mindef.sed.sicre.mobile.R;

/**
 * Created by root on 16/10/17.
 */

public class PerquisitionSoundAdapter extends BaseAdapter{
    private Context context;
    private List<String> links;

    public PerquisitionSoundAdapter(Context context, List<String> links) {
        this.context = context;
        this.links = links;
    }

    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public Object getItem(int position) {
        return links.get(position);
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
            convertView = li.inflate(R.layout.perquisition_sound_layout, null);
        }

        TextView numero = convertView.findViewById(R.id.numero);
        numero.setText(" " + (position + 1) + " - ");

        String fileName = links.get(position).substring(links.get(position).lastIndexOf('/') + 1);

        TextView name = convertView.findViewById(R.id.name);
        name.setText(fileName);

        convertView.setTag(position);

        return convertView;
    }
}
