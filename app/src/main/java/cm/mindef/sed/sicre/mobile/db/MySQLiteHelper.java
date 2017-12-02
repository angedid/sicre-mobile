package cm.mindef.sed.sicre.mobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_PREUVES = "preuves";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_PERQUISITION = "perquisition";
    public static final String COLUMN_TYPE = "type";
    private static final String DATABASE_NAME = "sicre_mobile.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PREUVES + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_PATH
            + " text not null, " + COLUMN_LATITUDE + " REAL not null, " +
            COLUMN_LONGITUDE + " REAL not null,"
            + COLUMN_PERQUISITION+" text not null," + COLUMN_TYPE + " text not null" +");";


    public static final String TABLE_INDIVIDU = "individus";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String DOCTYPE = "doctype";
    public static final String DOCNUM = "docnum";
    public static final String RAISON = "raison";


    private static final String DATABASE_CREATE_INDIVIDU = "create table "
            + TABLE_INDIVIDU + "(" +
            COLUMN_ID + " integer primary key, " +
            NAME + " text not null, " +
            SURNAME + " text not null, " +
            DOCTYPE + " text not null, " +
            DOCNUM + " text not null, " +
            RAISON + " text not null, " +
            COLUMN_PATH + " text not null, " +
            COLUMN_LATITUDE + " REAL not null, " +
            COLUMN_LONGITUDE + " REAL not null," +
            COLUMN_TYPE + " text not null" +
            ");";

    public static final String TABLE_VEHICULE = "vehicules";
    public static final String OWNER = "owner";
    public static final String MATRICULE = "matricule";
    public static final String CHASSIS = "chassie";
    public static final String MARQUE = "marque";
    public static final String TITRE = "titre";
    public static final String CARTEGRISE = "cartegrise";


    private static final String DATABASE_CREATE_VEHICULE= "create table "
            + TABLE_VEHICULE + "(" +
            COLUMN_ID + " integer primary key, " +
            OWNER + " text not null, " +
            MATRICULE + " text not null, " +
            CHASSIS + " text not null, " +
            MARQUE + " text not null, " +
            TITRE + " text not null, " +
            CARTEGRISE + " text not null, " +
            COLUMN_PATH + " text not null, " +
            COLUMN_LATITUDE + " REAL not null, " +
            COLUMN_LONGITUDE + " REAL not null," +
            COLUMN_TYPE + " text not null" +
            ");";


    public static final String TABLE_OBJET = "objets";
    public static final String DESCRIPTION = "description";

    private static final String DATABASE_CREATE_OBJET= "create table "
            + TABLE_OBJET + "(" +
            COLUMN_ID + " integer primary key, " +
            OWNER + " text not null, " +
            TITRE + " text not null, " +
            DESCRIPTION + " text not null, " +
            COLUMN_PATH + " text not null, " +
            COLUMN_LATITUDE + " REAL not null, " +
            COLUMN_LONGITUDE + " REAL not null," +
            COLUMN_TYPE + " text not null" +
            ");";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE_INDIVIDU);
        database.execSQL(DATABASE_CREATE_VEHICULE);
        database.execSQL(DATABASE_CREATE_OBJET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREUVES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDIVIDU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJET);
        onCreate(db);
    }

}
