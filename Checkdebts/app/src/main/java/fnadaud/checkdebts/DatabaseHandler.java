package fnadaud.checkdebts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by florian on 12/03/17.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DEBTS_NAME = "name";
    public static final String DEBTS_DEBT = "debt";

    public static final String DEBTS_TABLE_NAME = "debts";
    public static final String DEBTS_TABLE_CREATE =
            "CREATE TABLE " + DEBTS_TABLE_NAME + " (" +
                    DEBTS_NAME + " TEXT, " +
                    DEBTS_DEBT + " TEXT);";
    public static final String DEBTS_TABLE_DROP = "DROP TABLE IF EXISTS " + DEBTS_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DEBTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DEBTS_TABLE_DROP);
        onCreate(db);
    }

}
