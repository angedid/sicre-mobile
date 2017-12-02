package cm.mindef.sed.sicre.mobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
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
 * Created by root on 13/10/17.
 */

public class IndividuAdapter extends BaseAdapter {
    private Context context;
    private List<Individu> individus;

    public IndividuAdapter(Context context, List<Individu> individus) {
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
            convertView = li.inflate(R.layout.individu_layout, null);
        }

        Individu individuCourent = individus.get(position);

        RequestQueue requestQueue = MySingleton.getRequestQueue(context);
        Credentials credentials = Credentials.getInstance(context);



        /*final Uri uri = Uri.parse(individuCourent.getPhotoLink());

        final View rootView = convertView;

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable){
                ProgressBar progressBar = rootView.findViewById(R.id.progress_photo);
                if (progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }
                if (imageInfo == null){
                    return;
                }

                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                boolean imageMemoryCached = imagePipeline.isInBitmapMemoryCache(uri);
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.e("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                FLog.e("Intermediate image received", "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                FLog.e(getClass(), throwable, "Error loading %s", id);
            }
        };



        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(uri)
                // other setters
                .build();


        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.photo);
        draweeView.setMinimumHeight(draweeView.getWidth());
        draweeView.setController(controller);*/

        final ProgressBar pbg = convertView.findViewById(R.id.progress_photoG);

        if(pbg.getVisibility() == View.GONE){
            pbg.setVisibility(View.VISIBLE);
        }

        final ImageView photoG = convertView.findViewById(R.id.photoG);
        // Initialize a new ImageRequest
        String photoGUrl = individuCourent.getIdentification().getProfileG() + "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId() + "&" + Constant.PHOTO + "=" + Constant.PROFILE_GAUCHE; // Image URL
       Log.e("PHOTOGURLLLLLLLLLLL", photoGUrl);
        ImageRequest imageRequestG = new ImageRequest(
                photoGUrl,
                 new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbg.getVisibility() == View.VISIBLE){
                            pbg.setVisibility(View.GONE);
                        }
                        photoG.setImageBitmap(response);
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
                        if(pbg.getVisibility() == View.VISIBLE){
                            pbg.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        final ProgressBar pbd = convertView.findViewById(R.id.progress_photoD);

        if(pbd.getVisibility() == View.GONE){
            pbd.setVisibility(View.VISIBLE);
        }

        final ImageView photoD = convertView.findViewById(R.id.photoD);

        String photoDUrl = individuCourent.getIdentification().getProfileD() + "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId() + "&" + Constant.PHOTO + "=" + Constant.PROFILE_DROIT; // Image URL
        Log.e("PHOTODURLLLLLLLLLLL", photoDUrl);
        // Initialize a new ImageRequest
        ImageRequest imageRequestD = new ImageRequest(
                photoDUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbd.getVisibility() == View.VISIBLE){
                            pbd.setVisibility(View.GONE);
                        }
                        photoD.setImageBitmap(response);
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
                        if(pbd.getVisibility() == View.VISIBLE){
                            pbd.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        final ProgressBar pbf = convertView.findViewById(R.id.progress_photoFace);

        if(pbf.getVisibility() == View.GONE){
            pbf.setVisibility(View.VISIBLE);
        }

        final ImageView photoF = convertView.findViewById(R.id.photoFace);
        // Initialize a new ImageRequest

        String photoFUrl = individuCourent.getIdentification().getFace() + "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId() + "&" + Constant.PHOTO + "=" + Constant.FACE; // Image URL
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


        final ProgressBar pbp = convertView.findViewById(R.id.progress_photoPortrait);

        if(pbp.getVisibility() == View.GONE){
            pbp.setVisibility(View.VISIBLE);
        }

        final ImageView photoP = convertView.findViewById(R.id.photoPortrait);
        // Initialize a new ImageRequest

        String photoPUrl = individuCourent.getIdentification().getPortrait() + "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + individuCourent.getId() + "&" + Constant.PHOTO + "=" + Constant.PORTRAIT; // Image URL
        Log.e("PHOTOPURLLLLLLLLLLL", photoPUrl);

        ImageRequest imageRequestP = new ImageRequest(
                photoPUrl,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        if(pbp.getVisibility() == View.VISIBLE){
                            pbp.setVisibility(View.GONE);
                        }
                        photoP.setImageBitmap(response);
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
                        if(pbp.getVisibility() == View.VISIBLE){
                            pbp.setVisibility(View.GONE);
                        }
                        Log.e("ERROR GET USER PHOTO", error.toString());
                        // Do something with error response
                        // error.printStackTrace();
                        // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        //requestQueue.add(imageRequestG);
        //requestQueue.add(imageRequestD);
        //requestQueue.add(imageRequestF);
        //requestQueue.add(imageRequestP);





        TextView nom = (TextView)convertView.findViewById(R.id.nom);
        nom.setText("  " + individuCourent.getPerson().getName());

        TextView prenom = (TextView)convertView.findViewById(R.id.prenom);
        prenom.setText("  " + individuCourent.getPerson().getSurname());

        TextView alias = (TextView)convertView.findViewById(R.id.alias);
        alias.setText("  " + individuCourent.getPerson().getAlias());

        TextView civilite = (TextView)convertView.findViewById(R.id.civilite);
        String str_civilite = (individuCourent.getPerson().getCivility().equals(Constant.M))?context.getString(R.string.monsieur):context.getString(R.string.madame);
        civilite.setText("  " + str_civilite);

        TextView status = (TextView)convertView.findViewById(R.id.status);
        status.setText("  " + individuCourent.getPerson().getStatus());

        TextView date_naissance = (TextView)convertView.findViewById(R.id.date_naissance);
        date_naissance.setText("  " + individuCourent.getPerson().getDateOfBirth());

        TextView lieu_naissance = (TextView)convertView.findViewById(R.id.lieu_naissance);
        lieu_naissance.setText("  " + individuCourent.getPerson().getPlaceOfBirth());

        TextView pays_origine = (TextView)convertView.findViewById(R.id.pays_origine);
        pays_origine.setText("  " + individuCourent.getPerson().getPlaceOfBirth());

        TextView profession = (TextView)convertView.findViewById(R.id.profession);
        profession.setText("  " + individuCourent.getIdentification().getProfession());

        TextView cni = (TextView)convertView.findViewById(R.id.cni);
        cni.setText("  " + individuCourent.getIdentification().getCni());

        TextView cni_date = (TextView)convertView.findViewById(R.id.cni_date);
        cni_date.setText("  " + individuCourent.getIdentification().getCniDate());


        TextView cni_expire = (TextView)convertView.findViewById(R.id.cni_expire);
        cni_expire.setText("  " + individuCourent.getIdentification().getCniExpire());

        TextView cni_autorite = (TextView)convertView.findViewById(R.id.cni_autorite);
        cni_autorite.setText("  " + individuCourent.getIdentification().getCniAutorite());

        TextView antecedent = (TextView)convertView.findViewById(R.id.antecedent);
        antecedent.setText("  " + individuCourent.getAntecedant());

        return convertView;

    }
}
