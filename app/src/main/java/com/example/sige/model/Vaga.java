package com.example.sige.model;

import com.google.gson.annotations.SerializedName;

public class Vaga {
    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
