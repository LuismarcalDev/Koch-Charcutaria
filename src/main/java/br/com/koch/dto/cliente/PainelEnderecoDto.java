package br.com.koch.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PainelEnderecoDto {

    @NotBlank(message = "Informe o endereço completo.")
    @Size(max = 255, message = "Endereço pode ter no máximo 255 caracteres.")
    private String endereco;

    public PainelEnderecoDto() {
    }

    public PainelEnderecoDto(String endereco) {
        this.endereco = endereco;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
