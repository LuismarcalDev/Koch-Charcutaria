package br.com.koch.modelo.admin;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tempo_producao")
    private LocalDateTime tempoProducao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getTempoProducao() {
        return tempoProducao;
    }

    public void setTempoProducao(LocalDateTime tempoProducao) {
        this.tempoProducao = tempoProducao;
    }
}
