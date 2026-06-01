package com.example.fitsync.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.GroupPost;

import java.util.List;

public class GroupPostAdapter extends RecyclerView.Adapter<GroupPostAdapter.VH> {

    private List<GroupPost> lista;

    public GroupPostAdapter(List<GroupPost> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_post, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        GroupPost post = lista.get(position);

        holder.tvAuthorName.setText(post.getAutorNombre());
        holder.tvGroup.setText(post.getGrupo());
        holder.tvTime.setText(post.getTiempo());
        holder.tvPostText.setText(post.getTexto());
        holder.tvLikes.setText("♡  " + post.getLikes());
        holder.tvComments.setText("💬  " + post.getComentarios());
        holder.tvShares.setText("↗  " + post.getCompartidos());
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void updateLista(List<GroupPost> nueva) {
        lista.clear();
        lista.addAll(nueva);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvAuthorName, tvGroup, tvTime, tvPostText,
                tvLikes, tvComments, tvShares;

        VH(View v) {
            super(v);
            tvAuthorName = v.findViewById(R.id.tvAuthorName);
            tvGroup      = v.findViewById(R.id.tvGroup);
            tvTime       = v.findViewById(R.id.tvTime);
            tvPostText   = v.findViewById(R.id.tvPostText);
            tvLikes      = v.findViewById(R.id.tvLikes);
            tvComments   = v.findViewById(R.id.tvComments);
            tvShares     = v.findViewById(R.id.tvShares);
        }
    }
}