package com.example.sige.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sige.MainActivity;
import com.example.sige.R;
import com.example.sige.controller.CadastroController;

public class CadastroActivity extends AppCompatActivity {

    private EditText edNomeCadastro;
    private EditText edEmailCadastro;
    private EditText edSenhaCadastro;
    private Button btConfirmarCadastro;
    private Button btSairCadastro;

    private CadastroController cadastroController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edNomeCadastro = findViewById(R.id.edNomeCadastro);
        edEmailCadastro = findViewById(R.id.edEmailCadastro);
        edSenhaCadastro = findViewById(R.id.edSenhaCadastro);
        btConfirmarCadastro = findViewById(R.id.btConfirmarCadastro);
        btSairCadastro = findViewById(R.id.btSairCadastro);

        cadastroController = new CadastroController(this);

        btConfirmarCadastro.setOnClickListener(view -> salvarCadastro());

        btSairCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void salvarCadastro() {
        String nome = edNomeCadastro.getText().toString().trim();
        String email = edEmailCadastro.getText().toString().trim();
        String senha = edSenhaCadastro.getText().toString().trim();

        if (!cadastroController.isCamposValidos(nome, email, senha)) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cadastroController.isEmailExistente(email)) {
            Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            boolean sucesso = cadastroController.adicionarCadastro(nome, email, senha);
            if (sucesso) {
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                limparCampos();
                Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao realizar o cadastro", Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void limparCampos() {
        edNomeCadastro.setText("");
        edEmailCadastro.setText("");
        edSenhaCadastro.setText("");
    }
}
