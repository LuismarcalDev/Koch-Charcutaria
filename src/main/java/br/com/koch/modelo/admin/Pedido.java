package br.com.koch.modelo.admin;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Assinaturas")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assinaturas")
    private Long id;

    @Column(name = "Nome_assinatura")
    private String nomeCliente;

    @Column(name = "Preco_assinatura")
    private BigDecimal preco;

    @Column(name = "ativo_inativo")
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "Produtos_id")
    private Produto produto;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
}
