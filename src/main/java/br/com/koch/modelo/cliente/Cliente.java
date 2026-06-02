package br.com.koch.modelo.cliente;

import jakarta.persistence.*;

@Entity
@Table(name = "Clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clientes")
    private Integer id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "telefone", nullable = false, length = 12)
    private String telefone;

    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;

    @OneToOne(optional = false)
    @JoinColumn(name = "Usuarios_id", nullable = false)
    private UsuarioCliente usuario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public UsuarioCliente getUsuario() { return usuario; }
    public void setUsuario(UsuarioCliente usuario) { this.usuario = usuario; }
}
