package cm.mindef.sed.sicre.mobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
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

public class ObjetAdapter extends BaseAdapter {
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

        final Objet objetCourent = listeDesObjets.get(position);
        RequestQueue requestQueue = MySingleton.getRequestQueue(context);
        final Credentials credentials = Credentials.getInstance(context);

        //final Uri uri = Uri.parse(objetCourent.getPhotoLink());

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
                .build();


        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.photo);
        draweeView.setMinimumHeight(draweeView.getWidth());
        draweeView.setController(controller);*/

        final ProgressBar pb = convertView.findViewById(R.id.progress_photo);
        final ImageView photo = convertView.findViewById(R.id.photo);

        if(pb.getVisibility() == View.GONE){
            pb.setVisibility(View.VISIBLE);
        }

        pb.setVisibility(View.GONE);

        /*String photoGUrl = objetCourent.getPhoto() + "?" + Constant.USERNAME + "=" + credentials.getUsername() + "&" + Constant.PASSWORD + "=" + credentials.getPassword()+ "&" + Constant.ID + "=" + objetCourent.getId() + "&" + Constant.PHOTO + "=" + Constant.PHOTO; // Image URL
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
        );*/

        //requestQueue.add(imageRequestG);


        TextView libele = (TextView)convertView.findViewById(R.id.libele);
        libele.setText("  " + objetCourent.getLibele());

        TextView description = (TextView)convertView.findViewById(R.id.description);
        description.setText("  " + objetCourent.getDescription());

        TextView status = (TextView)convertView.findViewById(R.id.status);
        status.setText("  " + objetCourent.getStatus());

        TextView wanted_num = (TextView)convertView.findViewById(R.id.wanted_num);
        wanted_num.setText("  " + objetCourent.getWanted_num());

        TextView raison = (TextView)convertView.findViewById(R.id.raison);
        raison.setText("  " + objetCourent.getWanted_desc());

        TextView carte_grise = (TextView)convertView.findViewById(R.id.carte_grise);
        carte_grise.setText("  " + objetCourent.getCarte_grise());


        TextView matricule = (TextView)convertView.findViewById(R.id.matricule);
        matricule.setText("  " + objetCourent.getMatricule());

        TextView puissance = (TextView)convertView.findViewById(R.id.puissance);
        puissance.setText("  " + objetCourent.getPuissance());

        TextView kilometrage = (TextView)convertView.findViewById(R.id.kilometrage);
        kilometrage.setText("  " + objetCourent.getKilometrage());

        TextView num_assurance = (TextView)convertView.findViewById(R.id.num_assurance);
        num_assurance.setText("  " + objetCourent.getNum_assurance());


        Button btn_signal = convertView.findViewById(R.id.btn_signal);
        btn_signal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFileAsync uploadFileAsync = new UploadFileAsync();
                Credentials credentials = Credentials.getInstance(context);
                String query = null;
                try {
                 query =  Constant.USERNAME + "=" + credentials.getUsername() + "&" + // URLEncoder.encode(EditText_mot_cle.getText().toString(), "UTF-8")
                        Constant.PASSWORD + "=" + credentials.getPassword() + "&id=" +  URLEncoder.encode(objetCourent.getId(), "UTF-8") + "&objet=" +
                        URLEncoder.encode(!objetCourent.getMatricule().equals("")?"vehicule":"objet" , "UTF-8")
                        + "&" + Constant.LATITUDE + "=" + Constant.latitudeNetwork
                        + "&" + Constant.LONGITUDE + "=" + Constant.longitudeNetwork;
                    uploadFileAsync.execute(Constant.URL_LINK + Constant.SIGNALER_LINK, query);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        return convertView;
    }


    private class UploadFileAsync extends AsyncTask<String, Void, String> {
        private AlertDialog dialog;
        private int serverResponseCode;

        public UploadFileAsync(){
            dialog = new SpotsDialog(context);
        }
        @Override
        protected String doInBackground(String... params) {

            String url_string = params[0];

            Log.e("urlllllllllllll", url_string);
            String returnVal = "";
            String query = params[1];
            // Log.e("query", query);

            //String sourceFileUri = uir_string;

            HttpURLConnection conn = null;


            try {

                String upLoadServerUri = url_string;/*"http://idea-cm.club/magasino/enregistrement.php";*/

                URL url = new URL(upLoadServerUri);

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setRequestMethod("POST");

                Log.e("3333333333333333333333","333333333333333333333333333333333333333333333333");
                Log.e("3333333333333333333333","query " + query);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                InputStream in = conn.getInputStream();

                if (serverResponseCode >= 200 && serverResponseCode < 300) {

                    // messageText.setText(msg);
                    //Toast.makeText(ctx, "File Upload Complete.",
                    //      Toast.LENGTH_SHORT).show();

                    // recursiveDelete(mDirectory1);

                    BufferedReader br = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    try {
                        br = new BufferedReader(new InputStreamReader(in));
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                    } catch (IOException e) {
                        return Constant.KO + " 1           " + e.getMessage();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                return Constant.KO + " 2         " + e.getMessage();
                            }
                        }
                    }
                    in.close();
                    Log.e("55555555555555555","55555555555555555555555555555555555555555555555");
                    //os.close();
                    returnVal = sb.toString();

                }else {
                    Log.e(Constant.KO + " 3     " ,Constant.KO + " 3     " );
                    returnVal =  Constant.KO + " 3     " ;
                }


            } catch (MalformedURLException e) {

                // dialog.dismiss();
                e.printStackTrace();
                return Constant.KO +  " 4    " + e.getMessage();

            } catch (ProtocolException e) {
                e.printStackTrace();
                return Constant.KO +  " 5   " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return Constant.KO +  " 6    " + e.toString();
            }
            // dialog.dismiss();

            // End else block



            return returnVal;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (serverResponseCode >= 200 && serverResponseCode < 300){
                Toast.makeText(context, context.getString(R.string.successful_signal), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }

            Log.e("returnValllll", result);




        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(context.getString(R.string.chargement));
            dialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
