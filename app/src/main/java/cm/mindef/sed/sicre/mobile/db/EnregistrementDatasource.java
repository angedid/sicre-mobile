package cm.mindef.sed.sicre.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cm.mindef.sed.sicre.mobile.domain.EnregIndiv;
import cm.mindef.sed.sicre.mobile.domain.EnregObjet;
import cm.mindef.sed.sicre.mobile.domain.EnregVehicule;
import cm.mindef.sed.sicre.mobile.utils.Constant;

/**
 * Created by nkalla on 28/11/17.i9
 */

public class EnregistrementDatasource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context mContext;

    private String[] allColumnsI = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.NAME,
            MySQLiteHelper.SURNAME,
            MySQLiteHelper.DOCTYPE,
            MySQLiteHelper.DOCNUM,
            MySQLiteHelper.RAISON,
            MySQLiteHelper.COLUMN_PATH,
            MySQLiteHelper.COLUMN_LATITUDE,
            MySQLiteHelper.COLUMN_LONGITUDE,
            MySQLiteHelper.COLUMN_TYPE
    };



    private String[] allColumnsV = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.OWNER,
            MySQLiteHelper.MATRICULE,
            MySQLiteHelper.CHASSIS,
            MySQLiteHelper.MARQUE,
            MySQLiteHelper.TITRE,
            MySQLiteHelper.CARTEGRISE,
            MySQLiteHelper.COLUMN_PATH,
            MySQLiteHelper.COLUMN_LATITUDE,
            MySQLiteHelper.COLUMN_LONGITUDE,
            MySQLiteHelper.COLUMN_TYPE
    };

    private String[] allColumnsO = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.OWNER,
            MySQLiteHelper.TITRE,
            MySQLiteHelper.DESCRIPTION,
            MySQLiteHelper.COLUMN_PATH,
            MySQLiteHelper.COLUMN_LATITUDE,
            MySQLiteHelper.COLUMN_LONGITUDE,
            MySQLiteHelper.COLUMN_TYPE
    };

    public EnregistrementDatasource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public long createObjet(String owner, String titre, String description,  String path, double latitude, double longitude, String type) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PATH, path);
        values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
        values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
        values.put(MySQLiteHelper.OWNER, owner);
        values.put(MySQLiteHelper.TITRE, titre);
        values.put(MySQLiteHelper.DESCRIPTION, description);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        Log.e("PATH PATH PATH", path);
        return database.insert(MySQLiteHelper.TABLE_OBJET, null,values);
        /*Cursor cursor = database.query(MySQLiteHelper.TABLE_INDIVIDU, allColumnsI, MySQLiteHelper.COLUMN_PATH + " = '" + path + "'", null, null, null, null);
        cursor.moveToFirst();
        Preuve preuve = cursorToPreuve(cursor);
        cursor.close();
        Log.e("End PATH PATH PATH", preuve.getPath());

        return preuve;*/
    }


    public long createVehicule(String owner, String matricule, String chassis, String marque, String titre,
                                 String cartegrise, String path, double latitude, double longitude, String type) {
        ContentValues values = new ContentValues();
        //values.put(MySQLiteHelper.COLUMN_ID, id);
        values.put(MySQLiteHelper.COLUMN_PATH, path);
        values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
        values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
        values.put(MySQLiteHelper.OWNER, owner);
        values.put(MySQLiteHelper.MATRICULE, matricule);
        values.put(MySQLiteHelper.CHASSIS, chassis);
        values.put(MySQLiteHelper.MARQUE, marque);
        values.put(MySQLiteHelper.TITRE, titre);
        values.put(MySQLiteHelper.CARTEGRISE, cartegrise);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        Log.e("PATH PATH PATH", path);
        return database.insert(MySQLiteHelper.TABLE_VEHICULE, null,values);
        /*Cursor cursor = database.query(MySQLiteHelper.TABLE_INDIVIDU, allColumnsI, MySQLiteHelper.COLUMN_PATH + " = '" + path + "'", null, null, null, null);
        cursor.moveToFirst();
        Preuve preuve = cursorToPreuve(cursor);
        cursor.close();
        Log.e("End PATH PATH PATH", preuve.getPath());

        return preuve;*/
    }


    public long createIndividu(String name, String surname, String doctype, String docnum, String raison,
                               String path, double latitude, double longitude, String type) {
        ContentValues values = new ContentValues();
        //values.put(MySQLiteHelper.COLUMN_ID, id);
        values.put(MySQLiteHelper.COLUMN_PATH, path);
        values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
        values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
        values.put(MySQLiteHelper.NAME, name);
        values.put(MySQLiteHelper.SURNAME, surname);
        values.put(MySQLiteHelper.DOCTYPE, doctype);
        values.put(MySQLiteHelper.DOCNUM, docnum);
        values.put(MySQLiteHelper.RAISON, raison);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        Log.e("PATH PATH PATH", path);
        return database.insert(MySQLiteHelper.TABLE_INDIVIDU, null,values);
        /*Cursor cursor = database.query(MySQLiteHelper.TABLE_INDIVIDU, allColumnsI, MySQLiteHelper.COLUMN_PATH + " = '" + path + "'", null, null, null, null);
        cursor.moveToFirst();
        Preuve preuve = cursorToPreuve(cursor);
        cursor.close();
        Log.e("End PATH PATH PATH", preuve.getPath());

        return preuve;*/
    }


    public int  deleteEnreg(String quoi, String path) {
        if(quoi != null){
            if (quoi.equals(Constant.INDIVIDU)){
                return database.delete(MySQLiteHelper.TABLE_INDIVIDU, MySQLiteHelper.COLUMN_PATH + " = '" + path  + "'", null);
            }

            else if (quoi.equals(Constant.VEHICULE)){
                return database.delete(MySQLiteHelper.TABLE_VEHICULE, MySQLiteHelper.COLUMN_PATH + " = '" + path  + "'", null);
            }

            else if (quoi.equals(Constant.OBJECT)){
                return database.delete(MySQLiteHelper.TABLE_OBJET, MySQLiteHelper.COLUMN_PATH + " = '" + path  + "'", null);
            }

        }

        return -1;

    }


    public List<EnregIndiv> getAllEnregIndividu() {
        List<EnregIndiv> indivs = new ArrayList<EnregIndiv>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_INDIVIDU, allColumnsI, "1" ,
                null, null, null, MySQLiteHelper.COLUMN_ID + "");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EnregIndiv enregIndiv =  new EnregIndiv(
                    "" + cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(9),
                    cursor.getDouble(7),
                    cursor.getDouble(8)

            );
            indivs.add(enregIndiv);
            cursor.moveToNext();
        }

        Log.e("lite individu", indivs.toString());
        return indivs;
    }


    public List<EnregVehicule> getAllEnregVehicule() {
        List<EnregVehicule> vehicules = new ArrayList<EnregVehicule>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_VEHICULE, allColumnsV, "1" ,
                null, null, null, MySQLiteHelper.COLUMN_ID + "");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EnregVehicule enregVehicule =  new EnregVehicule(
                    "" + cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(10),
                    cursor.getDouble(8),
                    cursor.getDouble(9)

            );
            vehicules.add(enregVehicule);
            cursor.moveToNext();
        }

        return vehicules;
    }


    public List<EnregObjet> getAllEnregObject() {
        List<EnregObjet> objets = new ArrayList<EnregObjet>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJET, allColumnsO, "1" ,
                null, null, null, MySQLiteHelper.COLUMN_ID + "");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EnregObjet enregObjet =  new EnregObjet(
                    "" + cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(7),
                    cursor.getDouble(5),
                    cursor.getDouble(6)
            );

            objets.add(enregObjet);
            cursor.moveToNext();
        }

        return objets;
    }


}
