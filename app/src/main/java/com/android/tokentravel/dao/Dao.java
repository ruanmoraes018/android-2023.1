package com.android.tokentravel.dao;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.tokentravel.objetos.Pessoa;

import java.security.AccessControlContext;

public class Dao extends SQLiteOpenHelper {
    public Dao(Context context) {
        super(context, "banco", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_pessoas = "CREATE TABLE pessoas (pessoas_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pessoas_nome TEXT, " +
                "pessoas_cpf TEXT UNIQUE NOT NULL, " +
                "pessoas_email TEXT UNIQUE NOT NULL, " +
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
        dados_pessoa.put("pessoas_nome", pessoa.getPessoa_nome());
        dados_pessoa.put("pessoas_cpf", pessoa.getPessoa_cpf());
        dados_pessoa.put("pessoas_email", pessoa.getPessoa_email());
        dados_pessoa.put("pessoas_senha", pessoa.getPessoa_senha());
        dados_pessoa.put("pessoas_tipo", pessoa.getPessoa_tipo());
        // ===========================


        db.insert("pessoas", null, dados_pessoa);
        db.close();

        return "Sucesso";
    }

    public String buscaPessoaemail(String email){

        String sql_busca_pessoa =  "SELECT * FROM pessoas WHERE pessoas_email = " + "'" + email + "'";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql_busca_pessoa, null);

        if(c.moveToNext()){
            return "resultado";
        }else{
            return null;
        }
    }

    public String buscaPessoaCPF(String cpf) {
        String buscaPessoaPorCPF = "SELECT * FROM pessoas WHERE pessoas_cpf = '" + cpf + "'";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(buscaPessoaPorCPF, null);

        if(c.moveToNext()){
            return "resultado1";
        }else{
            return null;
        }
    }
}
