package com.android.tokentravel.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.tokentravel.objetos.Pessoa;

public class Dao extends SQLiteOpenHelper {
    public Dao(Context context) {
        super(context, "banco", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_pessoas = "CREATE TABLE pessoas (pessoas_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pessoas_nome TEXT, " +
                "pessoas_email TEXT UNIQUE, " +
                "pessoas_senha TEXT, " +
                "pessoas_tipo TEXT);";

        db.execSQL(sql_pessoas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_upgrade_pessoas = "DROP TABLE IF EXISTS pessoas;";

        db.execSQL(sql_upgrade_pessoas);
        onCreate(db);
    }

    public String inserirPessoa(Pessoa pessoa){
        SQLiteDatabase db = getWritableDatabase();

        // Dados a serem gravados no banco
        ContentValues dados_pessoa = new ContentValues();
        dados_pessoa.put("nome", pessoa.getPessoa_nome());
        dados_pessoa.put("email", pessoa.getPessoa_email());
        dados_pessoa.put("senha", pessoa.getPessoa_senha());
        dados_pessoa.put("tipo", pessoa.getPessoa_tipo());
        // ===========================


        db.insert("pessoas", null, dados_pessoa);
        db.close();

        return "Sucesso";
    }
}
