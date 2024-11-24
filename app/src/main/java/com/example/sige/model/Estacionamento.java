package com.example.sige.model;

import java.io.Serializable;

public class Estacionamento implements Serializable {
    private Integer id;
    private String nome;
    private String cnpj;
    private String rua;
    private String proprietario;
    private Integer numero;
    private String urlMaps;

    public Estacionamento(Integer id, String nome, String cnpj, String proprietario, String rua, Integer numero, String urlMaps) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.proprietario = proprietario;
        this.rua = rua;
        this.numero = numero;
        this.urlMaps = urlMaps;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getUrlMaps() {
        return urlMaps;
    }

    public void setUrlMaps(String urlMaps) {
        this.urlMaps = urlMaps;
    }
}
