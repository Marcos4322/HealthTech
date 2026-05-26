package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.Rutina;

import java.util.List;

public class RutinaCardAdapter extends RecyclerView.Adapter<RutinaCardAdapter.ViewHolder> {

    public interface OnRutinaClickListener {
        void onRutinaClick(Rutina rutina);
    }

    private final List<Rutina> items;
    private final OnRutinaClickListener listener;

    public RutinaCardAdapter(List<Rutina> items, OnRutinaClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rutina_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNombre;
        private final TextView tvNivel;
        private final TextView tvDuracion;
        private final TextView tvCalorias;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre   = itemView.findViewById(R.id.tvRutinaNombreCard);
            tvNivel    = itemView.findViewById(R.id.tvNivelCard);
            tvDuracion = itemView.findViewById(R.id.tvDuracionCard);
            tvCalorias = itemView.findViewById(R.id.tvCaloriasCard);
        }

        void bind(Rutina rutina, OnRutinaClickListener listener) {
            tvNombre.setText(rutina.getNombre());

            if (rutina.getNivel() != null) {
                tvNivel.setText(capitalizar(rutina.getNivel()));
                tvNivel.setVisibility(View.VISIBLE);
            } else {
                tvNivel.setVisibility(View.GONE);
            }

            if (rutina.getDuracionEstimadaMin() != null) {
                tvDuracion.setText(rutina.getDuracionEstimadaMin() + " min");
                tvDuracion.setVisibility(View.VISIBLE);
            } else {
                tvDuracion.setVisibility(View.GONE);
            }

            if (rutina.getCaloriasEstimadas() != null) {
                tvCalorias.setText(rutina.getCaloriasEstimadas() + " kcal");
                tvCalorias.setVisibility(View.VISIBLE);
            } else {
                tvCalorias.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onRutinaClick(rutina));
        }

        private String capitalizar(String s) {
            if (s == null || s.isEmpty()) return s;
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
    }
}