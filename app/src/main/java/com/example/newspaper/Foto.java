package com.example.newspaper;

public class Foto {

    private long id;
    private String nome;
    private String data;
    private String foto;

    public Foto(long id, String nome, String data, String foto) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.foto = foto;
    }

    public Foto(String nome, String data, String foto) {
        this.nome = nome;
        this.data = data;
        this.foto = foto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
