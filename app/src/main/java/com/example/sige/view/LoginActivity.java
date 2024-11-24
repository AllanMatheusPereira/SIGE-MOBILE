package com.example.sige.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sige.MainActivity;
import com.example.sige.R;
import com.example.sige.controller.LoginController;
import com.example.sige.model.Cadastro;

public class LoginActivity extends AppCompatActivity {

    private EditText edEmailLogin;
    private EditText edSenhaLogin;
    private Button btEntrarLogin;
    private Button btSairLogin;

    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmailLogin = findViewById(R.id.edEmailLogin);
        edSenhaLogin = findViewById(R.id.edSenhaLogin);
        btEntrarLogin = findViewById(R.id.btEntrarLogin);
        btSairLogin = findViewById(R.id.btSairLogin);

        loginController = new LoginController(this);

        btEntrarLogin.setOnClickListener(view -> validarLogin());

        btSairLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void validarLogin() {
        String email = edEmailLogin.getText().toString().trim();
        String senha = edSenhaLogin.getText().toString().trim();

        if (!loginController.isCamposValidos(email, senha)) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Cadastro usuario = loginController.autenticarUsuario(email, senha);

        if (usuario == null) {
            if (!loginController.isEmailCadastrado(email)) {
                Toast.makeText(this, "E-mail não cadastrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, EstacionamentoActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
