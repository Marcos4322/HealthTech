package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.SolicitudPendiente;

import java.util.ArrayList;
import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.ViewHolder> {

    public interface OnSolicitudListener {
        void onAceptar(SolicitudPendiente solicitud, int position);
        void onRechazar(SolicitudPendiente solicitud, int position);
    }

    private List<SolicitudPendiente> items = new ArrayList<>();
    private final OnSolicitudListener listener;

    public SolicitudAdapter(OnSolicitudListener listener) {
        this.listener = listener;
    }

    public void setItems(List<SolicitudPendiente> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_solicitud, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SolicitudPendiente s = items.get(position);
        holder.tvUsername.setText(s.getUsername());
        holder.tvNivel.setText("Nivel " + s.getNivel());

        holder.btnAceptar.setOnClickListener(v ->
                listener.onAceptar(s, holder.getAdapterPosition()));
        holder.btnRechazar.setOnClickListener(v ->
                listener.onRechazar(s, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvNivel;
        Button btnAceptar, btnRechazar;

        ViewHolder(View v) {
            super(v);
            tvUsername = v.findViewById(R.id.tvUsername);
            tvNivel = v.findViewById(R.id.tvNivel);
            btnAceptar = v.findViewById(R.id.btnAceptar);
            btnRechazar = v.findViewById(R.id.btnRechazar);
        }
    }
}