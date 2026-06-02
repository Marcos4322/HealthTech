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

    public interface OnAmigoListener {
        void onClick(Amigo amigo, int position);
        void onEliminar(Amigo amigo, int position);
    }

    private final List<Amigo> lista;
    private OnAmigoListener listener;

    public AmigosAdapter(List<Amigo> lista) {
        this.lista = new ArrayList<>(lista);
    }

    public void setOnAmigoListener(OnAmigoListener listener) {
        this.listener = listener;
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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(amigo, holder.getAdapterPosition());
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEliminar(amigo, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void updateLista(List<Amigo> nueva) {
        lista.clear();
        lista.addAll(nueva);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < lista.size()) {
            lista.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvUsername, tvStreak, btnEliminar;
        VH(View v) {
            super(v);
            tvName      = v.findViewById(R.id.tvName);
            tvUsername  = v.findViewById(R.id.tvUsername);
            tvStreak    = v.findViewById(R.id.tvStreak);
            btnEliminar = v.findViewById(R.id.btnEliminar);
        }
    }
}