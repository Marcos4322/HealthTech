package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.UsuarioBuscado;

import java.util.ArrayList;
import java.util.List;

public class BusquedaAdapter extends RecyclerView.Adapter<BusquedaAdapter.ViewHolder> {

    public interface OnAccionListener {
        void onEnviarSolicitud(UsuarioBuscado usuario, int position);
    }

    private List<UsuarioBuscado> items = new ArrayList<>();
    private final OnAccionListener listener;

    public BusquedaAdapter(OnAccionListener listener) {
        this.listener = listener;
    }

    public void setItems(List<UsuarioBuscado> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void updateItem(int position, String nuevoEstado) {
        if (position >= 0 && position < items.size()) {
            // No podemos modificar el campo directamente, así que forzamos refresh
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario_buscado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsuarioBuscado u = items.get(position);
        holder.tvUsername.setText(u.getUsername());
        holder.tvNivel.setText("Nivel " + u.getNivel());

        String estado = u.getEstadoAmistad();
        if (estado == null) {
            holder.btnAccion.setText("Añadir");
            holder.btnAccion.setEnabled(true);
            holder.btnAccion.setAlpha(1f);
            holder.btnAccion.setOnClickListener(v -> listener.onEnviarSolicitud(u, position));
        } else if (estado.equals("pendiente")) {
            holder.btnAccion.setText("Pendiente");
            holder.btnAccion.setEnabled(false);
            holder.btnAccion.setAlpha(0.5f);
        } else if (estado.equals("aceptada")) {
            holder.btnAccion.setText("Amigos ✓");
            holder.btnAccion.setEnabled(false);
            holder.btnAccion.setAlpha(0.6f);
        } else {
            holder.btnAccion.setText("Añadir");
            holder.btnAccion.setEnabled(true);
            holder.btnAccion.setAlpha(1f);
            holder.btnAccion.setOnClickListener(v -> listener.onEnviarSolicitud(u, position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvNivel;
        Button btnAccion;

        ViewHolder(View v) {
            super(v);
            tvUsername = v.findViewById(R.id.tvUsername);
            tvNivel = v.findViewById(R.id.tvNivel);
            btnAccion = v.findViewById(R.id.btnAccion);
        }
    }
}