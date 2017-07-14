package fnadaud.checkdebts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by florian on 12/03/17.
 */
public class DebtsDAO extends DAOBase {

    public static final String TABLE_NAME = "debts";
    public static final String NAME = "name";
    public static final String DEBT = "debt";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + NAME + " TEXT, " + DEBT + " TEXT);";
    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public DebtsDAO(Context pContext) {
        super(pContext);
    }

    /**
     * @param d la dette à ajouter à la base
     */
    public void insert(Debts d) {
        ContentValues value = new ContentValues();
        value.put(DebtsDAO.NAME, d.getName());
        value.put(DebtsDAO.DEBT, d.getDebt());
        mDb.insert(DebtsDAO.TABLE_NAME, null, value);
    }

    /**
     * @param name le nom de la personne à supprimer
     */
    public void delete(String name) {
        mDb.delete(TABLE_NAME, NAME + " = ?", new String[] {name});
    }

    /**
     * @param d la dette modifiée
     */
    public void update(Debts d) {
        ContentValues value = new ContentValues();
        value.put(DEBT, d.getDebt());
        mDb.update(TABLE_NAME, value, NAME  + " = ?", new String[] {d.getName()});
    }

    /**
     * @param name le nom de la personne à récupérer
     */
    public Debts select(String name) {
        Cursor c = mDb.rawQuery("select " + NAME + " ," + DEBT + " from " + TABLE_NAME +
                " where name > ?", new String[]{name});
        c.moveToNext();
        Debts newDebt = new Debts(c.getString(0), c.getString(1));
        c.close();
        return newDebt;
    }

    public ArrayList<Debts> getAllDebts() {
        ArrayList<Debts> debts = new ArrayList<Debts>();
        String[] allColumns = { NAME, DEBT};
        Cursor cursor = mDb.query(TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Debts debt = new Debts(cursor.getString(0), cursor.getString(1));
            debts.add(debt);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return debts;
    }
}
