package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.Amigo;

import java.util.ArrayList;
import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.VH> {

    private final List<Amigo> lista;

    public AmigosAdapter(List<Amigo> lista) {
        this.lista = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Amigo amigo = lista.get(position);
        holder.tvName.setText(amigo.getNombreCompleto() != null
                ? amigo.getNombreCompleto() : amigo.getUsername());
        holder.tvUsername.setText("@" + amigo.getUsername());
        holder.tvStreak.setText("Nv. " + amigo.getNivel());
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void updateLista(List<Amigo> nueva) {
        lista.clear();
        lista.addAll(nueva);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvUsername, tvStreak;
        VH(View v) {
            super(v);
            tvName     = v.findViewById(R.id.tvName);
            tvUsername = v.findViewById(R.id.tvUsername);
            tvStreak   = v.findViewById(R.id.tvStreak);
        }
    }
}