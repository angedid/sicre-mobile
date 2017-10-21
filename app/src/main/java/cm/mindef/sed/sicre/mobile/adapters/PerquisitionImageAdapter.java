package cm.mindef.sed.sicre.mobile.adapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        String str_urlCourent = links.get(position);
        ProgressBar pb = convertView.findViewById(R.id.progress_photo);

        if(pb.getVisibility() == View.GONE){
            pb.setVisibility(View.VISIBLE);
        }

        final Uri uri = Uri.parse(str_urlCourent);

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
        draweeView.setController(controller);

        TextView numero = (TextView)rootView.findViewById(R.id.numero);
        numero.setText(" " + (position+1) + " ");

        return convertView;
    }
}
