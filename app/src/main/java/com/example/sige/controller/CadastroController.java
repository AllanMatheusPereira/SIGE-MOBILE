package com.example.sige.controller;

import android.content.Context;
import com.example.sige.dao.CadastroDao;
import com.example.sige.model.Cadastro;

public class CadastroController {
    private CadastroDao cadastroDao;

    public CadastroController(Context context) {
        cadastroDao = new CadastroDao(context);
    }

    public boolean isCamposValidos(String nome, String email, String senha) {
        return !(nome.isEmpty() || email.isEmpty() || senha.isEmpty());
    }

    public boolean isEmailExistente(String email) {
        cadastroDao.open();
        boolean existe = cadastroDao.buscarCadastroPorEmail(email) != null;
        cadastroDao.close();
        return existe;
    }

    public boolean adicionarCadastro(String nome, String email, String senha) {
        Cadastro cadastro = new Cadastro();

        try {
            cadastro.setNome(nome);
            cadastro.setEmail(email);
            cadastro.setSenha(senha);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        cadastroDao.open();
        long resultado = cadastroDao.inserirCadastro(cadastro);
        cadastroDao.close();

        return resultado > 0;
    }
}
