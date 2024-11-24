package com.example.sige.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sige.R;
import com.example.sige.model.Estacionamento;

import java.util.List;

public class EstacionamentoAdapter extends RecyclerView.Adapter<EstacionamentoAdapter.ViewHolder> {

    private final List<Estacionamento> estacionamentos;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Estacionamento estacionamento);
    }

    public EstacionamentoAdapter(List<Estacionamento> estacionamentos, OnItemClickListener listener) {
        this.estacionamentos = estacionamentos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estacionamento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estacionamento estacionamento = estacionamentos.get(position);

        holder.textViewRua.setText("Rua: " + estacionamento.getRua());
        holder.textViewNumero.setText("Número: " + estacionamento.getNumero());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(estacionamento));
    }

    @Override
    public int getItemCount() {
        return estacionamentos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRua, textViewNumero;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRua = itemView.findViewById(R.id.textViewRua);
            textViewNumero = itemView.findViewById(R.id.textViewNumero);
        }
    }
}
