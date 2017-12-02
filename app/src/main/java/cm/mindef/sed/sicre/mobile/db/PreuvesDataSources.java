package cm.mindef.sed.sicre.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cm.mindef.sed.sicre.mobile.domain.Preuve;

public class PreuvesDataSources {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private Context mContext;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PATH,   MySQLiteHelper.COLUMN_LATITUDE,
            MySQLiteHelper.COLUMN_LONGITUDE, MySQLiteHelper.COLUMN_PERQUISITION,
            MySQLiteHelper.COLUMN_TYPE};

    public PreuvesDataSources(Context context) {
        dbHelper = new MySQLiteHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Preuve createPreuve(String path , double latitude, double longitude, String perquisition, String type) {
        ContentValues values = new ContentValues();
        //values.put(MySQLiteHelper.COLUMN_ID, id);
        values.put(MySQLiteHelper.COLUMN_PATH, path);
        values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
        values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
        values.put(MySQLiteHelper.COLUMN_PERQUISITION, perquisition);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        Log.e("PATH PATH PATH", path);
        database.insert(MySQLiteHelper.TABLE_PREUVES, null,values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PREUVES,
                allColumns, MySQLiteHelper.COLUMN_PATH + " = '" + path + "'", null, null, null, null);
        cursor.moveToFirst();
        Preuve preuve = cursorToPreuve(cursor);
        cursor.close();
        Log.e("End PATH PATH PATH", preuve.getPath());

        return preuve;
    }

    public void deletePreuve(Preuve preuve) {
        if(preuve != null){
            String path = preuve.getPath();
            //System.out.println("Comment deleted with id: " + id);
            int res = database.delete(MySQLiteHelper.TABLE_PREUVES, MySQLiteHelper.COLUMN_PATH + " = '" + path  + "'", null);
            //Toast.makeText(mContext, "" + res, Toast.LENGTH_LONG).show();
            Log.e("res res res res", "    " + res);
            //if(!(res == 1))Toast.makeText(mContext, "Ne peut etre supprime", Toast.LENGTH_LONG).show();
        }

    }



    public List<Preuve> getAllPreuves(String perquisition) {
        List<Preuve> preuves = new ArrayList<Preuve>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PREUVES, allColumns, MySQLiteHelper.COLUMN_PERQUISITION + "='" + perquisition + "'" ,
                null, null, null, MySQLiteHelper.COLUMN_ID + "");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Preuve preuve = cursorToPreuve(cursor);
            preuves.add(preuve);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        ArrayList<Preuve> reverse_smss = new ArrayList<Preuve>();
        for(int i=preuves.size()-1; i>= 0 ; i--) reverse_smss.add(preuves.get(i));
        cursor.close();
        Log.e("preuves size ", "" + preuves.size());
        return reverse_smss;
    }

    private Preuve cursorToPreuve(Cursor cursor) {
        Preuve preuve = new Preuve();
        preuve.setId(cursor.getLong(0));
        preuve.setPath(cursor.getString(1));
        preuve.setLatitude(cursor.getDouble(2));
        preuve.setLongitude(cursor.getDouble(3));
        preuve.setOthers(cursor.getString(4));
        preuve.setType(cursor.getString(5));

        return preuve;
    }
    public boolean updatePreuve(long id, ContentValues values){
        String [] params = {"" + id};
        int a  = database.update(MySQLiteHelper.TABLE_PREUVES, values, "" +MySQLiteHelper.COLUMN_ID
                + "=?"  , params);
        return (a==1);

    }

}

