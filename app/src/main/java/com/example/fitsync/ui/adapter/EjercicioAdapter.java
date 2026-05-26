package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.Ejercicio;
import com.example.fitsync.data.model.RutinaEjercicio;

import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.ViewHolder> {

    private final List<RutinaEjercicio> items;

    public EjercicioAdapter(List<RutinaEjercicio> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ejercicio, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ─── ViewHolder ──────────────────────────────────────────────────────────

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvOrden;
        private final TextView tvNombreEjercicio;
        private final TextView tvGrupoMuscular;
        private final TextView tvSeries;
        private final TextView tvDescanso;
        private final TextView tvNotas;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrden           = itemView.findViewById(R.id.tvOrden);
            tvNombreEjercicio = itemView.findViewById(R.id.tvNombreEjercicio);
            tvGrupoMuscular   = itemView.findViewById(R.id.tvGrupoMuscular);
            tvSeries          = itemView.findViewById(R.id.tvSeries);
            tvDescanso        = itemView.findViewById(R.id.tvDescanso);
            tvNotas           = itemView.findViewById(R.id.tvNotas);
        }

        void bind(RutinaEjercicio re, int posicion) {
            tvOrden.setText(String.valueOf(posicion));

            Ejercicio ej = re.getEjercicio();
            if (ej != null) {
                tvNombreEjercicio.setText(ej.getNombre());

                if (ej.getGrupoMuscular() != null && !ej.getGrupoMuscular().isEmpty()) {
                    tvGrupoMuscular.setText(ej.getGrupoMuscular());
                    tvGrupoMuscular.setVisibility(View.VISIBLE);
                } else {
                    tvGrupoMuscular.setVisibility(View.GONE);
                }
            }

            // Series × Reps  /  Series × Seg  /  Solo series
            if (re.getRepeticiones() != null) {
                tvSeries.setText(re.getSeries() + " series  ×  " + re.getRepeticiones() + " reps");
            } else if (re.getDuracionSeg() != null) {
                tvSeries.setText(re.getSeries() + " series  ×  " + re.getDuracionSeg() + " seg");
            } else {
                tvSeries.setText(re.getSeries() + " series");
            }

            tvDescanso.setText("Descanso: " + re.getDescansoSeg() + " seg");

            if (re.getNotas() != null && !re.getNotas().isEmpty()) {
                tvNotas.setText(re.getNotas());
                tvNotas.setVisibility(View.VISIBLE);
            } else {
                tvNotas.setVisibility(View.GONE);
            }
        }
    }
}