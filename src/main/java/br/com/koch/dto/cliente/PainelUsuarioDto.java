package br.com.koch.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PainelUsuarioDto {

    @NotBlank(message = "Informe o nome.")
    @Size(max = 100, message = "Nome pode ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "Informe o telefone.")
    @Size(max = 12, message = "Telefone pode ter no máximo 12 caracteres.")
    private String telefone;

    @NotBlank(message = "Informe o e-mail.")
    @Email(message = "E-mail inválido.")
    @Size(max = 100, message = "E-mail pode ter no máximo 100 caracteres.")
    private String email;

    /** Apenas exibição no painel; edição do endereço fica na aba Endereços. */
    private String endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
