package com.android.tokentravel.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;
import com.android.tokentravel.objetos.Motorista;
import com.android.tokentravel.objetos.Passageiro;
import com.android.tokentravel.objetos.Pessoa;
import com.android.tokentravel.objetos.Rotas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "motoristas_cnh TEXT NOT NULL, " +
                "modelo_carro TEXT NOT NULL, " +
                "placa_veiculo TEXT NOT NULL, " +
                "FOREIGN KEY (id_pessoas) REFERENCES pessoas(pessoas_id));";
        db.execSQL(sql_motoristas);

        String sql_dias_semanas = "CREATE TABLE dias_semanas (dias_semana_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "domingo TEXT, " +
                "segunda TEXT, " +
                "terca TEXT, " +
                "quarta TEXT, " +
                "quinta TEXT, " +
                "sexta TEXT, " +
                "sabado TEXT);";
        db.execSQL(sql_dias_semanas);

        String sql_rotas = "CREATE TABLE rotas (criar_rotas_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "origem_rota TEXT, " +
                "numeroRota INT UNIQUE, " +
                "destino_rota TEXT, " +
                "tipo_veiculo TEXT, " +
                "valor_rota REAL, " +
                "horario_rota TEXT, " +
                "id_motoristas INTEGER, " +
                "id_dias_semanas INTEGER, " +  // Adicione esta coluna
                "FOREIGN KEY (id_motoristas) REFERENCES motoristas(motoristas_id), " +
                "FOREIGN KEY (id_dias_semanas) REFERENCES dias_semanas(dias_semana_id));";
        db.execSQL(sql_rotas);

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

        String sql_upgrade_rotas = "DROP TABLE IF EXISTS rotas;";
        db.execSQL(sql_upgrade_rotas);

        String sql_upgrade_favoritos = "DROP TABLE IF EXISTS favoritos;";
        db.execSQL(sql_upgrade_favoritos);
        onCreate(db);
    }

    public String inserirPassageiro(Passageiro passageiro){
        SQLiteDatabase db = getWritableDatabase();
        try {
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
    public Pessoa buscaInfoPassageiro(String email){
        SQLiteDatabase db = getReadableDatabase();
        String sql_busca_passageiro =  "SELECT pessoas_nome, pessoas_email FROM pessoas WHERE pessoas_tipo = 'Passageiro' AND pessoas_email = ?;";
        Cursor c = db.rawQuery(sql_busca_passageiro, new String[]{email});

        if (c.moveToFirst()) {
            String nomeDoPassageiro = c.getString(0);
            String emailDoPassageiro = c.getString(1);
            c.close();
            return new Pessoa(nomeDoPassageiro, null, emailDoPassageiro, null, null, null);
        }
        c.close();
        return null;
    }

    public String autenticarUsuario(String email, String senha) {
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

    public Pessoa buscaInfoMotorista(String email){
        SQLiteDatabase db = getReadableDatabase();
        String sql_busca_motorista =  "SELECT pessoas_nome, pessoas_email FROM pessoas WHERE pessoas_tipo = 'Motorista' AND pessoas_email = ?;";
        Cursor c = db.rawQuery(sql_busca_motorista, new String[]{email});

        if (c.moveToFirst()) {
            String nomeDoMotorista = c.getString(0);
            String emailDoMotorista = c.getString(1);
            c.close();
            return new Pessoa(nomeDoMotorista, null, emailDoMotorista, null, null, null);
        }
        c.close();
        return null;
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
    public Motorista buscaDadosMotorista(String email){
        SQLiteDatabase db = getReadableDatabase();
        String sql_busca_motorista =  "SELECT id_pessoas FROM motoristas WHERE pessoas_tipo = 'Motorista' AND pessoas_email = ?;";
        Cursor c = db.rawQuery(sql_busca_motorista, new String[]{email});

        if (c.moveToFirst()) {
            String nomeDoMotorista = c.getString(0);
            String emailDoMotorista = c.getString(1);
            c.close();
            return new Motorista(nomeDoMotorista, null, emailDoMotorista, null, null, null, null, null, null);
        }
        c.close();
        return null;
    }
    public long inserirRota(Rotas rota) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String query = "SELECT MAX(numeroRota) FROM rotas";
            Cursor cursor = db.rawQuery(query, null);

            int numeroRotaMaximo = 0;

            if (cursor.moveToFirst()) {
                numeroRotaMaximo = cursor.getInt(0);
            }

            cursor.close();

            int proximoNumeroRota = numeroRotaMaximo + 1;

            // Inserir os dias da semana na tabela dias_semanas
            ContentValues diasSemanas = new ContentValues();
            diasSemanas.put("domingo", rota.isDomingo() ? "true" : "false");
            diasSemanas.put("segunda", rota.isSegunda() ? "true" : "false");
            diasSemanas.put("terca", rota.isTerca() ? "true" : "false");
            diasSemanas.put("quarta", rota.isQuarta() ? "true" : "false");
            diasSemanas.put("quinta", rota.isQuinta() ? "true" : "false");
            diasSemanas.put("sexta", rota.isSexta() ? "true" : "false");
            diasSemanas.put("sabado", rota.isSabado() ? "true" : "false");

            long diasSemanasId = db.insert("dias_semanas", null, diasSemanas);

            // Inserir a rota na tabela rotas
            ContentValues rotas = new ContentValues();
            rotas.put("numeroRota", proximoNumeroRota); // Usar o próximo número de rota único
            rotas.put("origem_rota", rota.getOrigem());
            rotas.put("destino_rota", rota.getDestino());
            rotas.put("tipo_veiculo", rota.getTipo());
            rotas.put("valor_rota", rota.getValor());
            rotas.put("horario_rota", rota.getHorario());
            rotas.put("id_motoristas", rota.getId_motorista());
            rotas.put("id_dias_semanas", diasSemanasId);

            long rotaId = db.insert("rotas", null, rotas);

            db.close();

            return rotaId;
        } catch (SQLiteException e) {
            Log.e("DAO", e.getMessage());
            return -1; // Retorne um valor de erro adequado
        }
    }

    public ArrayList<Rotas> buscaRotasMotorista(int motoristaId) {
        ArrayList<Rotas> rotasList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_busca_motorista = "SELECT r.*, ds.* FROM rotas r " +
                "INNER JOIN dias_semanas ds ON r.id_dias_semanas = ds.dias_semana_id " +
                "WHERE r.id_motoristas = ?";

        Cursor c = db.rawQuery(sql_busca_motorista, new String[]{String.valueOf(motoristaId)});

        while (c.moveToNext()) {
            @SuppressLint("Range") int numeroRota = c.getInt(c.getColumnIndex("numeroRota"));
            @SuppressLint("Range") String origemRota = c.getString(c.getColumnIndex("origem_rota"));
            @SuppressLint("Range") String destinoRota = c.getString(c.getColumnIndex("destino_rota"));
            @SuppressLint("Range") String tipoRota = c.getString(c.getColumnIndex("tipo_veiculo"));
            @SuppressLint("Range") float valorRota = c.getFloat(c.getColumnIndex("valor_rota"));
            @SuppressLint("Range") String horarioRota = c.getString(c.getColumnIndex("horario_rota"));
            @SuppressLint("Range") int idMotorista = c.getInt(c.getColumnIndex("id_motoristas"));

            @SuppressLint("Range")
            boolean domingo = c.getString(c.getColumnIndex("domingo")).equals("true");
            @SuppressLint("Range")
            boolean segunda = c.getString(c.getColumnIndex("segunda")).equals("true");
            @SuppressLint("Range")
            boolean terca = c.getString(c.getColumnIndex("terca")).equals("true");
            @SuppressLint("Range")
            boolean quarta = c.getString(c.getColumnIndex("quarta")).equals("true");
            @SuppressLint("Range")
            boolean quinta = c.getString(c.getColumnIndex("quinta")).equals("true");
            @SuppressLint("Range")
            boolean sexta = c.getString(c.getColumnIndex("sexta")).equals("true");
            @SuppressLint("Range")
            boolean sabado = c.getString(c.getColumnIndex("sabado")).equals("true");

// Mapeia os nomes dos dias da semana às variáveis booleanas
            Map<String, Boolean> diasSemana = new HashMap<>();
            diasSemana.put("domingo", domingo);
            diasSemana.put("segunda", segunda);
            diasSemana.put("terca", terca);
            diasSemana.put("quarta", quarta);
            diasSemana.put("quinta", quinta);
            diasSemana.put("sexta", sexta);
            diasSemana.put("sabado", sabado);

// Crie uma lista para armazenar os nomes dos dias da semana que são verdadeiros
            List<String> diasAtivos = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : diasSemana.entrySet()) {
                if (entry.getValue()) {
                    diasAtivos.add(entry.getKey());
                }
            }

// Resto do código permanece inalterado


            Rotas rota = new Rotas(
                    numeroRota,
                    origemRota,
                    destinoRota,
                    tipoRota,
                    valorRota,
                    horarioRota,
                    idMotorista,
                    diasAtivos // Aqui, passamos a lista de nomes dos dias ativos
            );

            rotasList.add(rota);

        }

        c.close();
        return rotasList;
    }

    public int buscaIdMotorista(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String sql_busca_motorista = "SELECT id_pessoas FROM motoristas " +
                "INNER JOIN pessoas ON motoristas.id_pessoas = pessoas.pessoas_id " +
                "WHERE pessoas_email = ?;";
        Cursor c = db.rawQuery(sql_busca_motorista, new String[]{email});

        if (c.moveToFirst()) {
            int idMotorista = c.getInt(0);
            c.close();
            return idMotorista;
        }
        c.close();
        return -1; // Retorna -1 se não encontrar o motorista
    }
    public int atualizarRota(Integer idDoMotoristaLogado, int idRota, Rotas rota) {
        if (rota == null) {
            Log.e("DAO", "Tentativa de atualizar uma rota nula.");
            return -1; // Retorne um valor de erro apropriado
        }

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Atualize os campos da rota
            String origem = rota.getOrigem();
            String destino = rota.getDestino();
            String tipo = rota.getTipo();
            Float valor = rota.getValor();
            String horario = rota.getHorario();

            if (origem == null || destino == null || tipo == null || valor == null || horario == null) {
                Log.e("DAO", "Um ou mais campos da rota são nulos.");
                return -1; // Retorne um valor de erro apropriado
            }

            ContentValues values = new ContentValues();
            values.put("origem_rota", origem);
            values.put("destino_rota", destino);
            values.put("tipo_veiculo", tipo);
            values.put("valor_rota", valor);
            values.put("horario_rota", horario);

            Log.d("DAO", "Valores antes da atualização:");
            Log.d("DAO", "Origem: " + origem);
            Log.d("DAO", "Destino: " + destino);
            Log.d("DAO", "Tipo de veículo: " + tipo);
            Log.d("DAO", "Valor: " + valor);
            Log.d("DAO", "Horário: " + horario);

            // Atualize a tabela "rotas" com base no ID do motorista e no ID da rota
            int rowsUpdated = db.update("rotas", values, "id_motoristas = ? AND criar_rotas_id = ?", new String[]{String.valueOf(idDoMotoristaLogado), String.valueOf(idRota)});

            Log.d("DAO", "Linhas atualizadas na tabela 'rotas': " + rowsUpdated);

            // Agora, atualize os dias da semana na tabela "dias_semanas" com base no ID da rota
            ContentValues diasSemanaValues = new ContentValues();
            diasSemanaValues.put("domingo", rota.isDomingo() ? "true" : "false");
            diasSemanaValues.put("segunda", rota.isSegunda() ? "true" : "false");
            diasSemanaValues.put("terca", rota.isTerca() ? "true" : "false");
            diasSemanaValues.put("quarta", rota.isQuarta() ? "true" : "false");
            diasSemanaValues.put("quinta", rota.isQuinta() ? "true" : "false");
            diasSemanaValues.put("sexta", rota.isSexta() ? "true" : "false");
            diasSemanaValues.put("sabado", rota.isSabado() ? "true" : "false");
            int diasSemanaRowsUpdated = db.update("dias_semanas", diasSemanaValues, "dias_semana_id = ?", new String[]{String.valueOf(idRota)});

            Log.d("DAO", "Linhas atualizadas na tabela 'dias_semanas': " + diasSemanaRowsUpdated);

            db.setTransactionSuccessful();
            return rowsUpdated;
        } catch (SQLiteException e) {
            Log.e("DAO", e.getMessage());
            return -1; // Retorne um valor de erro apropriado
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    public int buscarIdRotaPorNumeroRota(int numeroRota) {
        SQLiteDatabase db = getReadableDatabase();
        String sql_busca_id_rota = "SELECT criar_rotas_id FROM rotas WHERE numeroRota = ?";
        Cursor c = db.rawQuery(sql_busca_id_rota, new String[]{String.valueOf(numeroRota)});

        if (c.moveToFirst()) {
            int idRota = c.getInt(0);
            c.close();
            Log.d("Dao", "ID da rota encontrado: " + idRota); // Adicione esta linha para verificar o ID encontrado
            return idRota;
        } else {
            c.close();
            Log.d("Dao", "Rota não encontrada para o número de rota: " + numeroRota); // Adicione esta linha para verificar quando a rota não é encontrada
            return -1; // Retorna -1 para indicar que a rota não foi encontrada
        }
    }


    public int deletarRota(int idDoMotoristaLogado, int idRota) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            // Excluir a rota da tabela "rotas" com base no ID do motorista e no ID da rota
            int rowsDeleted = db.delete("rotas", "id_motoristas = ? AND criar_rotas_id = ?", new String[]{String.valueOf(idDoMotoristaLogado), String.valueOf(idRota)});

            // Excluir os dias da semana correspondentes na tabela "dias_semanas"
            int diasSemanaRowsDeleted = db.delete("dias_semanas", "dias_semana_id = ?", new String[]{String.valueOf(idRota)});

            // Verifique se alguma linha foi excluída
            if (rowsDeleted > 0 && diasSemanaRowsDeleted > 0) {
                db.setTransactionSuccessful();
                return rowsDeleted; // Retorne o número de linhas excluídas (pode ser usado para verificação)
            } else {
                return -1; // Nenhuma linha foi excluída
            }
        } catch (SQLiteException e) {
            Log.e("DAO", e.getMessage());
            return -1; // Retorne um valor de erro apropriado
        } finally {
            db.endTransaction();
            db.close();
        }
    }





}
