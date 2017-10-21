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
import cm.mindef.sed.sicre.mobile.ResultSearchActivity;
import cm.mindef.sed.sicre.mobile.domain.Individu;
import cm.mindef.sed.sicre.mobile.domain.Objet;
import cm.mindef.sed.sicre.mobile.utils.Constant;

/**
 * Created by root on 14/10/17.
 */

public class ObjetAdapter extends BaseAdapter{
    private Context context;
    private List<Objet> listeDesObjets;

    public ObjetAdapter(Context context, List<Objet> listeDesObjets) {
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
            convertView = li.inflate(R.layout.objet_layout, null);
        }

        Objet objetCourent = listeDesObjets.get(position);

        ProgressBar pb = convertView.findViewById(R.id.progress_photo);

        if(pb.getVisibility() == View.GONE){
            pb.setVisibility(View.VISIBLE);
        }

        final Uri uri = Uri.parse(objetCourent.getPhotoLink());

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


        TextView titre = (TextView)rootView.findViewById(R.id.titre);
        titre.setText("  " + objetCourent.getTitre());

        TextView sujet = (TextView)rootView.findViewById(R.id.sujet);
        sujet.setText("  " + objetCourent.getSujet());

        TextView description = (TextView)rootView.findViewById(R.id.description);
        description.setText("  " + objetCourent.getDescription());

        TextView marque = (TextView)rootView.findViewById(R.id.marque);
        marque.setText("  " + objetCourent.getMarque());

        TextView frabricant = (TextView)rootView.findViewById(R.id.frabricant);
        frabricant.setText("  " + objetCourent.getFabricant());

        TextView date_creation = (TextView)rootView.findViewById(R.id.date_creation);
        date_creation.setText("  " + objetCourent.getDateDeCreation());


        TextView raison = (TextView)rootView.findViewById(R.id.raison);
        raison.setText("  " + objetCourent.getRaisonDescription());

        TextView matricule = (TextView)rootView.findViewById(R.id.matricule);
        matricule.setText("  " + objetCourent.getMatricule());

        TextView chassie = (TextView)rootView.findViewById(R.id.chassie);
        chassie.setText("  " + objetCourent.getNumeroDeChassie());

        return convertView;
    }
}
