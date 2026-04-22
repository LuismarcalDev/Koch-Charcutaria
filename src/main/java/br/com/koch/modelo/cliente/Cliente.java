package br.com.koch.modelo.cliente;

public class Cliente {

    private Integer id;
    private String nome;
    private String telefone;
    private String endereco;
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
