package com.android.tokentravel.objetos;

public class Passageiro extends Pessoa {
    int numero_passageiro;

    public Passageiro(String pessoa_nome, String pessoa_cpf, String pessoa_email, String pessoa_senha, String pessoa_tipo, int numero_passageiro) {
        super(pessoa_nome, pessoa_cpf, pessoa_email, pessoa_senha, pessoa_tipo);
        this.numero_passageiro = numero_passageiro;
    }

    public int getNumero_passageiro() {
        return numero_passageiro;
    }

    public void setNumero_passageiro(int numero_passageiro) {
        this.numero_passageiro = numero_passageiro;
    }
}
