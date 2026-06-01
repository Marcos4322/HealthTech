package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.Profile;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.VH> {

    private final List<Profile> lista;

    public RankingAdapter(List<Profile> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ranking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Profile profile = lista.get(position);

        // Posición
        holder.tvPosition.setText(String.valueOf(position + 1));

        // Solo el nombre (nombre_completo si existe, si no username)
        String nombre = profile.getNombreCompleto() != null && !profile.getNombreCompleto().isEmpty()
                ? profile.getNombreCompleto()
                : profile.getUsername();
        holder.tvName.setText(nombre);

        // XP = nivel * 1000 (ajusta si tienes campo XP real)
        holder.tvXP.setText("Nv. " + profile.getNivel()
                + "  •  🔥 " + profile.getRachaDias() + " días");

        // Medalla para top 3
        if (position == 0)      holder.tvPosition.setText("🥇");
        else if (position == 1) holder.tvPosition.setText("🥈");
        else if (position == 2) holder.tvPosition.setText("🥉");

        // Ocultar tvChange (no tenemos datos históricos)
        holder.tvChange.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Actualizar lista (para cambio de tab)
    public void updateLista(List<Profile> nuevaLista) {
        lista.clear();
        lista.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvPosition, tvName, tvXP, tvChange;

        VH(View v) {
            super(v);
            tvPosition = v.findViewById(R.id.tvPosition);
            tvName     = v.findViewById(R.id.tvName);
            tvXP       = v.findViewById(R.id.tvXP);
            tvChange   = v.findViewById(R.id.tvChange);
        }
    }
}