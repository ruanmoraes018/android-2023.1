package com.android.tokentravel.objetos;

public class Motorista extends Pessoa {
    private String cnh;

    public Motorista(String pessoa_nome, String pessoa_cpf, String pessoa_email, String pessoa_senha, String pessoa_tipo, String cnh) {
        super(pessoa_nome, pessoa_cpf, pessoa_email, pessoa_senha, pessoa_tipo);
        this.cnh = cnh;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }
}