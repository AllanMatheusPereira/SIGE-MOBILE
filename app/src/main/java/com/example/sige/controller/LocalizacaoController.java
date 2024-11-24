package com.example.sige.controller;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class LocalizacaoController {

    private Context context;

    public LocalizacaoController(Context context) {
        this.context = context;
    }

    public LatLng obterCoordenadas(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    public String calcularTrajeto(LatLng origem, LatLng destino) {
        return "Trajeto simulado entre origem e destino.";
    }
}
