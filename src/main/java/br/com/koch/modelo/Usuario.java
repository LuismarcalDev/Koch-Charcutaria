package br.com.koch.modelo;

import java.util.UUID;

public class Usuario
{
    private UUID id;
    private String nome;
    private String email;
    private String senhaHash;
    private Perfil perfil;

    public Usuario()
    {
    }

    public Usuario(UUID id, String nome, String email, String senhaHash, Perfil perfil)
    {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSenhaHash()
    {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash)
    {
        this.senhaHash = senhaHash;
    }

    public Perfil getPerfil()
    {
        return perfil;
    }

    public void setPerfil(Perfil perfil)
    {
        this.perfil = perfil;
    }
}