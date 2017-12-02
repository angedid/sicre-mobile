package cm.mindef.sed.sicre.mobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.domain.Individu;
import cm.mindef.sed.sicre.mobile.utils.Constant;
import cm.mindef.sed.sicre.mobile.utils.Credentials;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;

/**
 * Created by nkalla on 20/11/17.
 */

public class LiteIndividuAdapter extends BaseAdapter {


    private Context context;
    private List<Individu> individus;

    public LiteIndividuAdapter(Context context, List<Individu> individus) {
        this.context = context;
        this.individus = individus;
        Log.e("individus", this.individus.toString());
    }

    @Override
    public int getCount() {
        return individus.size();
    }

    @Override
    public Object getItem(int position) {
        return individus.get(position);
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
            convertView = li.inflate(R.layout.lite_individu_layout, null);
        }

        Individu individuCourent = individus.get(position);

        RequestQueue requestQueue = MySingleton.getRequestQueue(context);
        Credentials credentials = Credentials.getInstance(context);

        final ProgressBar pbf = convertView.findViewById(R.id.progress_photoFace);

        if(pbf.getVisibility() == View.GONE){
            pbf.setVisibility(View.VISIBLE);
        }

        final ImageView photoF = convertView.findViewById(R.id.photoFace);
        // Initialize a new ImageRequest

        String photoFUrl = individuCourent.getIdentification().getFace() + "&" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId(); // Image URL
        Log.e("PHOTOFURLLLLLLLLLLL", photoFUrl);

        ImageRequest imageRequestF = new ImageRequest(
                photoFUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbf.getVisibility() == View.VISIBLE){
                            pbf.setVisibility(View.GONE);
                        }
                        photoF.setImageBitmap(response);
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
                        if(pbf.getVisibility() == View.VISIBLE){
                            pbf.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(imageRequestF);

        TextView nom = (TextView)convertView.findViewById(R.id.nom);
        nom.setText("  " + individuCourent.getPerson().getName() + "  " + individuCourent.getPerson().getSurname());

        TextView prenom = (TextView)convertView.findViewById(R.id.cni);
        prenom.setText(" " + individuCourent.getIdentification().getCni());


        return convertView;
    }
}
