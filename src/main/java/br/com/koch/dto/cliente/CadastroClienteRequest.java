package br.com.koch.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CadastroClienteRequest {

    @NotBlank
    private String nome;

    @NotBlank @Email
    private String email;

    @NotBlank
    private String telefone;

    @NotBlank
    private String endereco;

    @NotBlank
    private String senha;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}