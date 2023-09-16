package com.android.tokentravel.dao;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Passageiro;
import com.android.tokentravel.objetos.Pessoa;

import java.security.AccessControlContext;

public class Dao extends SQLiteOpenHelper {
    public Dao(Context context) {
        super(context, "banco", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_pessoas = "CREATE TABLE pessoas (pessoas_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pessoas_nome TEXT NOT NULL, " +
                "pessoas_cpf TEXT UNIQUE NOT NULL, " +
                "pessoas_email TEXT UNIQUE NOT NULL, " +
                "pessoas_senha TEXT NOT NULL, " +
                "pessoas_telefone TEXT NOT NULL, " +
                "pessoas_tipo TEXT);";
        db.execSQL(sql_pessoas);

        String sql_passageiros = "CREATE TABLE passageiros (passageiros_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_pessoas INTEGER, " +
                "FOREIGN KEY (id_pessoas) REFERENCES pessoas(pessoas_id));";
        db.execSQL(sql_passageiros);

        String sql_motoristas = "CREATE TABLE motoristas (motoristas_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_pessoas INTEGER, " +
                "motoristas_cnh TEXT , " +
                "modelo_carro TEXT, " +
                "placa_veiculo TEXT, " +
                "FOREIGN KEY (id_pessoas) REFERENCES pessoas(pessoas_id));";
        db.execSQL(sql_motoristas);

        String sql_dias_semanas = "CREATE TABLE dias_semanas (dias_semana_id INTEGER PRIMARY KEY, " +
                "domingo INTEGER, " +
                "segunda INTEGER, " +
                "terca INTEGER, " +
                "quarta INTEGER, " +
                "quinta INTEGER, " +
                "sexta INTEGER, " +
                "sabado INTEGER);";
        db.execSQL(sql_dias_semanas);

        String sql_criar_rotas = "CREATE TABLE criar_rotas (criar_rotas_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "origem_rota TEXT, " +
                "destino_rota TEXT, " +
                "valor_rota REAL, " +
                "horario_rota, " +
                "id_motoristas INTEGER, " +
                "id_dias_semanas INTEGER, " +
                "FOREIGN KEY (id_motoristas) REFERENCES motoristas(motoristas_id), " +
                "FOREIGN KEY (id_dias_semanas) REFERENCES dias_semanas(dias_semana_id));";
        db.execSQL(sql_criar_rotas);

        String sql_favoritos = "CREATE TABLE favoritos (favoritos_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_passageiros INTEGER, " +
                "id_motoristas INTEGER, " +
                "FOREIGN KEY (id_passageiros) REFERENCES passageiros(passageiros_id), " +
                "FOREIGN KEY (id_motoristas) REFERENCES motoristas(motoristas_id));";
        db.execSQL(sql_favoritos);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_upgrade_pessoas = "DROP TABLE IF EXISTS pessoas;";
        db.execSQL(sql_upgrade_pessoas);

        String sql_upgrade_passageiros = "DROP TABLE IF EXISTS passageiros;";
        db.execSQL(sql_upgrade_passageiros);

        String sql_upgrade_motoristas = "DROP TABLE IF EXISTS motoristas;";
        db.execSQL(sql_upgrade_motoristas);

        String sql_upgrade_criar_rotas = "DROP TABLE IF EXISTS criar_rotas;";
        db.execSQL(sql_upgrade_criar_rotas);

        String sql_upgrade_favoritos = "DROP TABLE IF EXISTS favoritos;";
        db.execSQL(sql_upgrade_favoritos);
        onCreate(db);
    }

    public String inserirPassageiro(Passageiro passageiro){
        SQLiteDatabase db = getWritableDatabase();



        // ===========================
        try {
            // Dados a serem gravados no banco
            // Inserir dados na tabela pessoas
            ContentValues dados_pessoas = new ContentValues();
            dados_pessoas.put("pessoas_nome", passageiro.getPessoa_nome());
            dados_pessoas.put("pessoas_cpf", passageiro.getPessoa_cpf());
            dados_pessoas.put("pessoas_email", passageiro.getPessoa_email());
            dados_pessoas.put("pessoas_senha", passageiro.getPessoa_senha());
            dados_pessoas.put("pessoas_tipo", passageiro.getPessoa_tipo());
            dados_pessoas.put("pessoas_telefone", passageiro.getPessoa_telefone());
            long idPessoa = db.insert("pessoas", null, dados_pessoas);

            ContentValues dados_passageiro = new ContentValues();
            dados_passageiro.put("id_pessoas", idPessoa);
            db.insert("passageiros", null, dados_passageiro);
            db.close();

            return "Passageiro cadastrado com sucesso!";
        } catch (SQLiteException e) {
            Log.e("DAO", e.getMessage());
            return "Erro ao cadastrar passageiro!";
        }
    }

    public String inserirMotorista(Motorista motorista){
        SQLiteDatabase db = getWritableDatabase();

        try {
            // Dados a serem gravados no banco
            // Inserir dados na tabela pessoas
            ContentValues dados_pessoas = new ContentValues();
            dados_pessoas.put("pessoas_nome", motorista.getPessoa_nome());
            dados_pessoas.put("pessoas_cpf", motorista.getPessoa_cpf());
            dados_pessoas.put("pessoas_email", motorista.getPessoa_email());
            dados_pessoas.put("pessoas_senha", motorista.getPessoa_senha());
            dados_pessoas.put("pessoas_tipo", motorista.getPessoa_tipo());
            dados_pessoas.put("pessoas_telefone", motorista.getPessoa_telefone());

            long idPessoa = db.insert("pessoas", null, dados_pessoas);

            ContentValues dados_motorista = new ContentValues();
            dados_motorista.put("id_pessoas", idPessoa);
            dados_motorista.put("motoristas_cnh", motorista.getCnh());
            dados_motorista.put("modelo_carro", motorista.getModelo_carro());
            dados_motorista.put("placa_veiculo", motorista.getPlaca_veiculo());

            db.insert("motoristas", null, dados_motorista);
            db.close();

            return "Motorista cadastrado com sucesso!";
        } catch (SQLiteException e) {
            Log.e("DAO", e.getMessage());
            return "Erro ao cadastrar motorista!";
        }
    }

    public String buscaPessoaEmail(String email){

        String sql_busca_pessoa =  "SELECT * FROM pessoas WHERE pessoas_email = " + "'" + email + "';";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql_busca_pessoa, null);

        if(c.moveToNext()){
            return "resultado";
        }else{
            return null;
        }
    }

    public String buscaPessoaSenha(String senha){

        String sql_busca_senha =  "SELECT * FROM pessoas WHERE pessoas_senha = " + "'" + senha + "';";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(sql_busca_senha, null);

        if(c.moveToNext()){
            return "resultado";
        }else{
            return null;
        }
    }
    public String buscarTipoPessoa(String email, String senha) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT pessoas_tipo FROM pessoas WHERE pessoas_email = ? AND pessoas_senha = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, senha});

        if (cursor.moveToFirst()) {
            String tipo = cursor.getString(0);
            cursor.close();
            return tipo;
        }

        cursor.close();
        return null;
    }


    public String buscaPessoaCPF(String cpf) {
        String buscaPessoaPorCPF = "SELECT * FROM pessoas WHERE pessoas_cpf = '" + cpf + "';";
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(buscaPessoaPorCPF, null);

        if(c.moveToNext()){
            return "resultado1";
        }else{
            return null;
        }
    }
    public String autenticarUsuario(String email, String senha) {
        SQLiteDatabase db = getReadableDatabase();

        // Verifique se o usuário é um passageiro
        String queryPassageiro = "SELECT pessoas_tipo FROM pessoas WHERE pessoas_email = ? AND pessoas_senha = ?";
        Cursor cursorPassageiro = db.rawQuery(queryPassageiro, new String[]{email, senha});

        if (cursorPassageiro.moveToFirst()) {
            String tipo = cursorPassageiro.getString(0);
            cursorPassageiro.close();
            return tipo;
        }

        cursorPassageiro.close();

        // Verifique se o usuário é um motorista
        String queryMotorista = "SELECT pessoas_tipo FROM motoristas INNER JOIN pessoas ON motoristas.id_pessoas = pessoas.pessoas_id WHERE pessoas.pessoas_email = ? AND pessoas.pessoas_senha = ?";
        Cursor cursorMotorista = db.rawQuery(queryMotorista, new String[]{email, senha});

        if (cursorMotorista.moveToFirst()) {
            String tipo = cursorMotorista.getString(0);
            cursorMotorista.close();
            return tipo;
        }

        cursorMotorista.close();

        // Se nenhum tipo de usuário for concentration, retorne null
        return null;
    }

}
