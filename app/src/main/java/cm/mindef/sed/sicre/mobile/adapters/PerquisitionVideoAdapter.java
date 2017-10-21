package cm.mindef.sed.sicre.mobile.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import cm.mindef.sed.sicre.mobile.R;

/**
 * Created by root on 17/10/17.
 */

public class PerquisitionVideoAdapter extends BaseAdapter{
    private Context context;
    private List<String> links;

    public PerquisitionVideoAdapter(Context context, List<String> links) {
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
            convertView = li.inflate(R.layout.perquisition_video_layout, null);
        }

        String fileNames [] = links.get(position).split("\\/");
        String fileName = fileNames[fileNames.length-1];
        TextView link = convertView.findViewById(R.id.link);
        link.setText(fileName);

        return convertView;
    }
}
