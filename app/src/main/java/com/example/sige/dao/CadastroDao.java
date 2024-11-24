package com.example.sige.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.sige.helper.SQLiteDataHelper;
import com.example.sige.model.Cadastro;

public class CadastroDao {

    private SQLiteDatabase database;
    private SQLiteDataHelper dbHelper;

    public CadastroDao(Context context) {
        dbHelper = new SQLiteDataHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long inserirCadastro(Cadastro cadastro) {
        ContentValues values = new ContentValues();
        values.put("nome", cadastro.getNome());
        values.put("email", cadastro.getEmail());
        values.put("senha", cadastro.getSenha());

        return database.insert("Cadastro", null, values);
    }

    public Cadastro buscarCadastroPorEmail(String email) {
        Cursor cursor = database.query(
                "Cadastro",
                new String[]{"nome", "email", "senha"},
                "email = ?",
                new String[]{email},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Cadastro cadastro = new Cadastro();
            cadastro.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            cadastro.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            cadastro.setSenha(cursor.getString(cursor.getColumnIndexOrThrow("senha")));
            cursor.close();
            return cadastro;
        } else {
            return null;
        }
    }
}
