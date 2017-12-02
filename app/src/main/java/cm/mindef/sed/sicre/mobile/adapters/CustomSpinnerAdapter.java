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
import cm.mindef.sed.sicre.mobile.domain.DocType;

/**
 * Created by nkalla on 07/11/17.
 */

public class CustomSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<DocType> list;

    public CustomSpinnerAdapter(Context context, List<DocType> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(list.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater li=(LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.doctype_item, null);
        }

        DocType currentDoctype = list.get(position);

        TextView description = convertView.findViewById(R.id.description);
        description.setText(currentDoctype.getDescription());

        return convertView;
    }
}
