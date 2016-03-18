package edu.ifpb.pdm.applistservice.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael on 17/03/2016.
 *
 * Classe responsável por criar um banco SQLite
 */
public class SqlStorage extends SQLiteOpenHelper {
    private static final String NOME_DATA = "banco.db";
    public static final String NOME_TABLE = "Data";
    public static final String ID = "id";
    public static final String NOME = "Nome";
    private static final Integer VERSION = 1;

    public SqlStorage(Context context) {
        super(context, NOME_DATA, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(NOME_TABLE).append("(")
                .append(ID).append(" integer primary key autoincrement,")
                .append(NOME).append(" text")
                .append(")");
        db.execSQL(sql.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOME_TABLE);
        onCreate(db);
    }

    public void save(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlStorage.NOME, nome);
        db.insert(SqlStorage.NOME_TABLE, null, contentValues);
        db.close();
    }

    public List<String> findAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] campos = {SqlStorage.NOME};
        Cursor cursor = db.query(SqlStorage.NOME_TABLE, campos, null, null, null, null, null);
        List<String> list = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            //
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return list;
        }
        db.close();
        return list;
    }

    public void saveList(List<String> list) {
        for (String value : list) {
            save(value);
        }
    }

    public void delete() {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = SqlStorage.ID + ">" + 0;
        db.delete(SqlStorage.NOME_TABLE, where, null);
        db.close();

    }
}
