package in.aspiretechnovations.aml.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHandler extends SQLiteOpenHelper {

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        getReadableDatabase();
        getWritableDatabase();


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(GeneralTables.CREATE_VEHICLE_MAKE);
        db.execSQL(GeneralTables.CREATE_MODEL_TABLE);
        db.execSQL(GeneralTables.CREATE_VARIANT_TABLE);
        db.execSQL(GeneralTables.CREATE_CART_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //  AppLog.localDbLog("DB Update Upgrading database from version " + oldVersion + " to " + newVersion);
    }
}
