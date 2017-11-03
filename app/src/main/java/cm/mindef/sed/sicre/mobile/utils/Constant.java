package cm.mindef.sed.sicre.mobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 11/10/17.
 */

public class Constant {
    public final static String OK = "OK";
    public final static String KO = "KO";

    public final static String PERQUISITION_ACCESS = "URL VIEW ALL";

    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String STAY_CONNECTED = "stay_connected";

    public static final String PreferenceCredential = "PreferenceCredential";

    public static final String HOSTNAME = "cm.mindef.sed.sicre.mobile";
    public static final int MIN_LENGTH_PASSWORD = 6;

    public static final String URL_LINK = "http://198.50.199.116:8090/scriptcase/app/SICRE_2/";

    public static final String LOGIN_PATH = "m_login/";

    public static final int SEARCH_INDIVIDU = 1;
    public static final int SEARCH_VEHICULE = 2;
    public static final int SEARCH_OBJECT   = 3;

    public static final int SEARCH_INDIVIDU_RESULT = 4;
    public static final int SEARCH_VEHICULE_RESULT = 5;
    public static final int SEARCH_OBJECT_RESULT   = 6;


    private static final String SEARCH_INDIVIDU_VALUE = "Recherche D'Individu";
    private static final String SEARCH_VEHICULE_VALUE = "Recherche De Objet";
    private static final String SEARCH_OBJECT_VALUE  = "Recherche D'Objet";

    private static final String SEARCH_INDIVIDU_VALUE_RESULT = "Individus Trouves";
    private static final String SEARCH_VEHICULE_VALUE_RESULT = "Vehicules Trouves";
    private static final String SEARCH_OBJECT_VALUE_RESULT  = "Objets Trouves";

    public static final String CRITERIA = "criteria";
    public static final String KEY_WORD = "keyword";
    public static final String DOMAINE = "domaine";
    public static final String RESULT = "result";
    public static final String SEARCH_REULT_DISPLAY_VALUE = "search_result_display_value";
    public static final String M = "M";
    public static final String F = "F";
    public static final String PERQUISITION_LIST = "PERQUISITION_LIST";

    public static final String PERQUISITION_LIST_REQUEST_TAG = "PERQUISITION_LIST_REQUEST_TAG";
    public static final String PERQUISITION = "perquisition";
    public static final int REQUEST_CODE_FOR_ADD_PERQUISITION = 1;
    public static final int REQUEST_CODE_FOR_ADD_IMG_PERQUISITION = 2;
    public static final java.lang.String VIDEO_LINK = "video_link";
    public static final String NOTIFICATION_CHANEL_ID = "NOTIFICATION_CHANEL_ID";
    public static final int REQUEST_VIDEO_CAPTURE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final Object AFFAIRE_LIST_REQUEST_TAG = "affaire_list_request_tag";
    public static final String AFFAIRE = "affaire";
    public static final long TIMEOUT = 5000;
    public static final String TYPE = "type";
    public static final String OBJECT = "object";
    public static final String OPTION = "option";
    public static final String INDIVIDU = "individu";
    public static final String IDENTIFICATION = "identification";
    public static final String STATUS = "status";
    public static final String FAILED = "failed";
    public static final String USER = "user";
    public static final String RESEARCH_PATH = "m_research/";
    public static final String ID = "id";
    public static final String PHOTO = "photo";
    public static final String PROFILE_GAUCHE = "profil_gauche";
    public static final String PROFILE_DROIT = "profil_droit";
    public static final String FACE = "face";
    public static final String PORTRAIT = "portrait";
    public static final String RAS = "Rien a Signaler";

    public static Map<String, Object> data = new HashMap<String, Object>();
    public static final String PERQUISITION_IMAGE_ROOT = "perquisitions_images_";
    public static final String PERQUISITION_SOUND_ROOT = "perquisitions_sounds_";
    public static final String PERQUISITION_VIDEO_ROOT = "perquisitions_videos_";
    public static String who_is_showing = "";
    public static final String CONNECTED = "CONNECTED";

    public static final int RequestPermissionCode = 1;

    //public static String AUCUN_INDIVIDU_TROUVE;

    public static Map<String, String> getSearchTypeValue(){
        Map<String, String> map = new HashMap<>();

        map.put("" + SEARCH_INDIVIDU, SEARCH_INDIVIDU_VALUE);
        map.put("" + SEARCH_VEHICULE, SEARCH_VEHICULE_VALUE);
        map.put("" + SEARCH_OBJECT, SEARCH_OBJECT_VALUE);

        map.put("" + SEARCH_INDIVIDU_RESULT, SEARCH_INDIVIDU_VALUE_RESULT);
        map.put("" + SEARCH_VEHICULE_RESULT, SEARCH_VEHICULE_VALUE_RESULT);
        map.put("" + SEARCH_OBJECT_RESULT, SEARCH_OBJECT_VALUE_RESULT);

        return map;
    }


    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
