package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.Rutina;

import java.util.ArrayList;
import java.util.List;

public class RutinaAmigoAdapter extends RecyclerView.Adapter<RutinaAmigoAdapter.VH> {

    private final List<Rutina> lista = new ArrayList<>();

    public void setLista(List<Rutina> nueva) {
        lista.clear();
        lista.addAll(nueva);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rutina_amigo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Rutina r = lista.get(position);
        holder.tvNombre.setText(r.getNombre());

        StringBuilder meta = new StringBuilder();
        if (r.getNivel() != null) meta.append(capitalizar(r.getNivel()));
        if (r.getDuracionEstimadaMin() != null) {
            if (meta.length() > 0) meta.append(" · ");
            meta.append(r.getDuracionEstimadaMin()).append(" min");
        }
        holder.tvMeta.setText(meta.toString());
    }

    @Override
    public int getItemCount() { return lista.size(); }

    private String capitalizar(String s) {
        if (s == null || s.isEmpty()) return "";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMeta;
        VH(View v) {
            super(v);
            tvNombre = v.findViewById(R.id.tvRutinaNombre);
            tvMeta   = v.findViewById(R.id.tvRutinaMeta);
        }
    }
}