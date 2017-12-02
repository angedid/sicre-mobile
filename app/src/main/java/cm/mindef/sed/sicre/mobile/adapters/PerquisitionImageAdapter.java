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
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import java.util.List;

import cm.mindef.sed.sicre.mobile.R;
import cm.mindef.sed.sicre.mobile.utils.MySingleton;

/**
 * Created by root on 16/10/17.
 */

public class PerquisitionImageAdapter extends BaseAdapter{
    private Context context;
    private List<String> links;

    public PerquisitionImageAdapter(Context context, List<String> links) {
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
            convertView = li.inflate(R.layout.perquisition_image_layout, null);
        }

        RequestQueue requestQueue = MySingleton.getRequestQueue(context);
        String str_urlCourent = links.get(position);
        final ProgressBar pb = convertView.findViewById(R.id.progress_photo);

        final ImageView photo =  convertView.findViewById(R.id.photo);

        if(pb.getVisibility() == View.GONE){
            pb.setVisibility(View.VISIBLE);
        }

        Uri uri = Uri.parse(str_urlCourent);

        ImageRequest imageRequestG = new ImageRequest(
                str_urlCourent,
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

        /*final View rootView = convertView;

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
                .build();*/





        /*draweeView.setMinimumHeight(draweeView.getWidth());
        draweeView.setController(controller);*/

        TextView numero = (TextView)convertView.findViewById(R.id.numero);
        numero.setText(" " + (position+1) + " ");

        return convertView;
    }
}
