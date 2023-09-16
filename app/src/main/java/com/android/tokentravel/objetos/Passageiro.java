package com.android.tokentravel.objetos;

public class Passageiro extends Pessoa {
    Integer id_pessoas;
    public Passageiro(String pessoa_nome, String pessoa_cpf, String pessoa_email, String pessoa_senha, String pessoa_telefone, String pessoa_tipo) {
        super(pessoa_nome, pessoa_cpf, pessoa_email, pessoa_senha, pessoa_telefone, pessoa_tipo);
    }

    public Integer getId_pessoas() {
        return id_pessoas;
    }
}
