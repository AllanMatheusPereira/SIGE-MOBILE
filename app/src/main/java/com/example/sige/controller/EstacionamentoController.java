package com.example.sige.controller;

import android.util.Log;

import com.example.sige.Network.APIService;
import com.example.sige.Network.RetrofitClient;
import com.example.sige.model.Estacionamento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstacionamentoController {

    private final APIService apiService;

    public interface EstacionamentoCallback {
        void onSuccess(List<Estacionamento> estacionamentos);
        void onError(String errorMessage);
    }

    public EstacionamentoController() {
        this.apiService = RetrofitClient.getClient().create(APIService.class);
    }

    public void getEstacionamentos(EstacionamentoCallback callback) {
        Call<List<Estacionamento>> call = apiService.getEstacionamentos();
        call.enqueue(new Callback<List<Estacionamento>>() {
            @Override
            public void onResponse(Call<List<Estacionamento>> call, Response<List<Estacionamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erro na resposta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Estacionamento>> call, Throwable t) {
                callback.onError("Falha na chamada: " + t.getMessage());
            }
        });
    }
}
