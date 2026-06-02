package br.com.koch.modelo.admin;

import br.com.koch.modelo.cliente.Cliente;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Assinaturas")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assinaturas")
    private Long id;

    @Column(name = "Nome_assinatura", nullable = false)
    private String nomeAssinatura;

    @Column(name = "Preco_assinatura", nullable = false)
    private BigDecimal preco;

    @Column(name = "ativo_inativo", nullable = false)
    private Boolean ativo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Produtos_id", nullable = false)
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Clientes_id", nullable = false)
    private Cliente cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeAssinatura() {
        return nomeAssinatura;
    }

    public void setNomeAssinatura(String nomeAssinatura) {
        this.nomeAssinatura = nomeAssinatura;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
