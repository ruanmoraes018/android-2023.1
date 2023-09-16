package com.android.tokentravel.objetos;

public class Pessoa {
    Integer pessoa_id;
    String pessoa_nome;
    String pessoa_cpf;
    String pessoa_email;
    String pessoa_senha;
    String pessoa_tipo;
    String pessoa_telefone;

    public Pessoa(String pessoa_nome, String pessoa_cpf, String pessoa_email, String pessoa_senha, String pessoa_telefone , String pessoa_tipo) {
        this.pessoa_nome = pessoa_nome;
        this.pessoa_cpf = pessoa_cpf;
        this.pessoa_email = pessoa_email;
        this.pessoa_senha = pessoa_senha;
        this.pessoa_telefone = pessoa_telefone;
        this.pessoa_tipo = pessoa_tipo;
    }

    public Integer getPessoa_id() {
        return pessoa_id;
    }

    public String getPessoa_nome() {
        return pessoa_nome;
    }

    public void setPessoa_nome(String pessoa_nome) {
        this.pessoa_nome = pessoa_nome;
    }

    public String getPessoa_cpf() {
        return pessoa_cpf;
    }

    public void setPessoa_cpf(String pessoa_cpf) {
        this.pessoa_cpf = pessoa_cpf;
    }

    public String getPessoa_email() {
        return pessoa_email;
    }

    public void setPessoa_email(String pessoa_email) {
        this.pessoa_email = pessoa_email;
    }

    public String getPessoa_senha() {
        return pessoa_senha;
    }

    public void setPessoa_senha(String pessoa_senha) {
        this.pessoa_senha = pessoa_senha;
    }

    public String getPessoa_tipo() {
        return pessoa_tipo;
    }

    public void setPessoa_tipo(String pessoa_tipo) { this.pessoa_tipo = pessoa_tipo; }

    public String getPessoa_telefone() { return pessoa_telefone; }

    public void setPessoa_telefone(String pessoa_telefone) {
        this.pessoa_telefone = pessoa_telefone;
    }
}
