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
 * Created by root on 15/10/17.
 */

public class PerquisitionTextAdapter extends BaseAdapter {
    private Context context;
    private List<String> texts;

    public PerquisitionTextAdapter(Context context, List<String> texts) {
        this.context = context;
        this.texts = texts;
    }

    @Override
    public int getCount() {
        return texts.size();
    }

    @Override
    public Object getItem(int position) {
        return texts.get(position);
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
            convertView = li.inflate(R.layout.perquisition_text_layout, null);
        }

        TextView txt = convertView.findViewById(R.id.text);
        txt.setText(texts.get(position));

        return convertView;
    }
}
