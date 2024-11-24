package com.example.sige.Network;

import com.example.sige.model.Estacionamento;
import com.example.sige.model.Vaga;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface APIService {
    @GET("estacionamentos")
    Call<List<Estacionamento>> getEstacionamentos();

    @PUT("vagas/{id}")
    Call<Vaga> atualizarVaga(
            @Path("id") Integer id,
            @Query("status") int status
    );

    @PUT("vagas/confirmar/{id}")
    Call<Vaga> confirmarVaga(
            @Path("id") Integer id
    );
}
