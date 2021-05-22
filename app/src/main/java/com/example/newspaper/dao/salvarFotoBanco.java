package com.example.newspaper.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.newspaper.Foto;

import java.util.ArrayList;

public class salvarFotoBanco extends SQLiteOpenHelper {

    private static final String Nome_Banco="foto.sqlite";
    private static final int Banco_VERSION = 1;
    private static final String Banco_TABLE = "Foto";
    private static final String COL_ID = "id";
    private static final String COL_NOME = "nome";
    private static final String COL_DATA = "data";
    private static final String COL_FOTO = "foto";
    private Context context;

    public salvarFotoBanco( Context context) {
        super(context, Nome_Banco,null,Banco_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s(" +
                " %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " %s TEXT, " +
                " %s TEXT, " +
                " %s TEXT " +
                ")", Banco_TABLE, COL_ID, COL_NOME, COL_DATA, COL_FOTO);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long criandoFotoNoBanco(Foto foto) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME, foto.getNome());
        values.put(COL_DATA, foto.getData());
        values.put(COL_FOTO, foto.getFoto());

        long id = database.insert(Banco_TABLE, null, values);

        database.close();
        return id;
    }


    public ArrayList<Foto> recuperarFotos() {
        ArrayList<Foto> listaFotos = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(Banco_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                String nome  = cursor.getString(cursor.getColumnIndex(COL_NOME));
                String data  = cursor.getString(cursor.getColumnIndex(COL_DATA));
                String foto = cursor.getString(cursor.getColumnIndex(COL_FOTO));
                Foto fotosGravada = new Foto(id, nome, data, foto);

                listaFotos.add(fotosGravada);
            }
            while(cursor.moveToNext());
        }

        database.close();
        return listaFotos;
    }


    public long AtualizarFoto(Foto foto) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_DATA, foto.getData());
        values.put(COL_NOME, foto.getData());
        values.put(COL_FOTO, foto.getFoto());
        String query = String.format(" %s = %s ", COL_ID, foto.getId());
        long id = database.update(Banco_TABLE, values, query, null);

        database.close();

        return id;
    }


    public long deletarFoto(Foto foto) {
        SQLiteDatabase database = getWritableDatabase();
        String query = String.format(" %s = %s ", COL_ID, foto.getId());

        long id = database.delete(Banco_TABLE, query, null);

        return id;
    }




}
