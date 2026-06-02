package br.com.koch.dto.cliente;

import java.math.BigDecimal;

public class PainelAssinaturaDto {

    private final long id;
    private final String nome;
    private final String foto;
    private final String descricao;
    private final BigDecimal preco;
    private final String periodicidade;
    private final String fornecedor;

    public PainelAssinaturaDto(
            long id,
            String nome,
            String foto,
            String descricao,
            BigDecimal preco,
            String periodicidade,
            String fornecedor
    ) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
        this.descricao = descricao;
        this.preco = preco;
        this.periodicidade = periodicidade;
        this.fornecedor = fornecedor;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getFoto() {
        return foto;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    public String getFornecedor() {
        return fornecedor;
    }
}
