package com.example.sige.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sige.MainActivity;
import com.example.sige.R;
import com.example.sige.Network.APIService;
import com.example.sige.Network.RetrofitClient;
import com.example.sige.adapter.EstacionamentoAdapter;
import com.example.sige.model.Estacionamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstacionamentoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEstacionamentos;
    private Button btSairEstacionamento;
    private Button btConfirmarEstacionamento;
    private Estacionamento estacionamentoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacionamento);

        recyclerViewEstacionamentos = findViewById(R.id.recyclerViewEstacionamentos);
        recyclerViewEstacionamentos.setLayoutManager(new LinearLayoutManager(this));

        btSairEstacionamento = findViewById(R.id.btSairEstacionamento);
        btConfirmarEstacionamento = findViewById(R.id.btConfirmarEstacionamento);

        btSairEstacionamento.setOnClickListener(v -> {
            Intent intent = new Intent(EstacionamentoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btConfirmarEstacionamento.setOnClickListener(v -> {
            if (estacionamentoSelecionado != null) {
                Intent intent = new Intent(EstacionamentoActivity.this, LocalizacaoActivity.class);
                intent.putExtra("estacionamento", estacionamentoSelecionado);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Selecione um estacionamento antes de confirmar!", Toast.LENGTH_SHORT).show();
            }
        });

        fetchEstacionamentoData();
    }

    private void fetchEstacionamentoData() {
        APIService apiService = RetrofitClient.getClient().create(APIService.class);
        Call<List<Estacionamento>> call = apiService.getEstacionamentos();

        call.enqueue(new Callback<List<Estacionamento>>() {
            @Override
            public void onResponse(Call<List<Estacionamento>> call, Response<List<Estacionamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Estacionamento> estacionamentos = response.body();

                    EstacionamentoAdapter adapter = new EstacionamentoAdapter(estacionamentos, estacionamento -> {
                        estacionamentoSelecionado = estacionamento;
                        Toast.makeText(EstacionamentoActivity.this, "Selecionado: " + estacionamento.getRua(), Toast.LENGTH_SHORT).show();
                    });
                    recyclerViewEstacionamentos.setAdapter(adapter);
                } else {
                    Log.e("Estacionamento", "Erro na resposta: " + response.code());
                    Toast.makeText(EstacionamentoActivity.this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Estacionamento>> call, Throwable t) {
                Log.e("Estacionamento", "Falha na chamada: " + t.getMessage());
                Toast.makeText(EstacionamentoActivity.this, "Erro na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
