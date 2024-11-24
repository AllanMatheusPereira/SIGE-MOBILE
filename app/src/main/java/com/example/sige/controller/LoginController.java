package com.example.sige.controller;

import android.content.Context;
import com.example.sige.dao.CadastroDao;
import com.example.sige.model.Cadastro;

public class LoginController {

    private CadastroDao cadastroDao;

    public LoginController(Context context) {
        cadastroDao = new CadastroDao(context);
    }

    public boolean isCamposValidos(String email, String senha) {
        return !(email.isEmpty() || senha.isEmpty());
    }

    public Cadastro autenticarUsuario(String email, String senha) {
        cadastroDao.open();
        Cadastro usuario = cadastroDao.buscarCadastroPorEmail(email);
        cadastroDao.close();

        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null;
    }

    public boolean isEmailCadastrado(String email) {
        cadastroDao.open();
        Cadastro usuario = cadastroDao.buscarCadastroPorEmail(email);
        cadastroDao.close();
        return usuario != null;
    }
}
