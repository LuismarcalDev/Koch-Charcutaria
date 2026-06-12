package br.com.koch.modelo.cliente;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuarios")
public class UsuarioCliente {
    public UsuarioCliente(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuarios")
    private Integer id;

    @Column(name = "tipo", nullable = false, length = 45)
    private String tipo;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
