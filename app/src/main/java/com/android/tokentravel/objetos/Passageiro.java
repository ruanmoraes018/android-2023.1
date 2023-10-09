package com.android.tokentravel.objetos;

import java.util.UUID;

public class Passageiro extends Pessoa {
    Integer id_pessoas;
    UUID codigoPassageiro; // Adicione o campo UUID

    public Passageiro(String pessoa_nome, String pessoa_cpf, String pessoa_email, String pessoa_senha, String pessoa_telefone, String pessoa_tipo, String foto) {
        super(pessoa_nome, pessoa_cpf, pessoa_email, pessoa_senha, pessoa_telefone, pessoa_tipo, foto);
        this.codigoPassageiro = UUID.randomUUID(); // Gere um UUID único para o passageiro
    }
    public Integer getId_pessoas() {
        return id_pessoas;
    }
}
