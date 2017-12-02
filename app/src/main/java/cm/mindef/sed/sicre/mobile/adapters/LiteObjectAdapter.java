package cm.mindef.sed.sicre.mobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import cm.mindef.sed.sicre.mobile.AlerterActivity;
import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.ResultSearchActivity;
import cm.mindef.sed.sicre.mobile.domain.Individu;
import cm.mindef.sed.sicre.mobile.domain.Objet;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;
import dmax.dialog.SpotsDialog;

/**
 * Created by root on 14/10/17.
 */

public class LiteObjectAdapter extends BaseAdapter {
    private Context context;
    private List<Objet> listeDesObjets;

    public LiteObjectAdapter(Context context, List<Objet> listeDesObjets) {
        this.context = context;
        this.listeDesObjets = listeDesObjets;
    }

    @Override
    public int getCount() {
        return listeDesObjets.size();
    }

    @Override
    public Object getItem(int position) {
        return listeDesObjets.get(position);
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
            convertView = li.inflate(R.layout.lite_object_layout, null);
        }

        final Objet objetCourent = listeDesObjets.get(position);
        RequestQueue requestQueue = MySingleton.getRequestQueue(context);
        final Credentials credentials = Credentials.getInstance(context);

        final ProgressBar pb = convertView.findViewById(R.id.progress_photo);
        final ImageView photo = convertView.findViewById(R.id.photo);

        if(pb.getVisibility() == View.GONE){
            pb.setVisibility(View.VISIBLE);
        }

        pb.setVisibility(View.GONE);

        String photoGUrl = objetCourent.getPhoto() + "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + objetCourent.getId() + "&" + Constant.PHOTO + "=" + Constant.PHOTO; // Image URL
        Log.e("PHOTOGURLLLLLLLLLLL", photoGUrl);
        ImageRequest imageRequestG = new ImageRequest(
                photoGUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pb.getVisibility() == View.VISIBLE){
                            pb.setVisibility(View.GONE);
                        }
                        photo.setImageBitmap(response);
                        //circularImageView.setImageURI(new );
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pb.getVisibility() == View.VISIBLE){
                            pb.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(imageRequestG);


        TextView libele = (TextView)convertView.findViewById(R.id.libele);
        libele.setText("  " + objetCourent.getLibele());

        TextView description = (TextView)convertView.findViewById(R.id.description);
        description.setText("" + objetCourent.getDescription() . trim());

        TextView status = (TextView)convertView.findViewById(R.id.status);
        status.setText("" + objetCourent.getStatus().trim());

        return convertView;
    }

}
