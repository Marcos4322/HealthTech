package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.R;
import com.example.fitsync.data.model.Amigo;
import com.example.fitsync.data.model.Profile;
import com.example.fitsync.data.repository.AmigosRepository;
import com.example.fitsync.data.repository.ProfileRepository;
import com.example.fitsync.ui.adapter.RankingAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {

    public RankingFragment() {
        super(R.layout.fragment_ranking);
    }

    private RankingAdapter adapter;
    private List<Profile> rankingGlobal = new ArrayList<>();
    private List<String> amigoIds = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = view.findViewById(R.id.recyclerRanking);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new RankingAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);

        ProfileRepository profileRepo = new ProfileRepository(requireContext());
        AmigosRepository amigosRepo = new AmigosRepository(requireContext());

        // Cargar ranking global
        profileRepo.getRanking(new ProfileRepository.ListCallback<Profile>() {
            @Override
            public void onSuccess(List<Profile> items) {
                requireActivity().runOnUiThread(() -> {
                    rankingGlobal = items;
                    adapter.updateLista(items);
                });
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "Error ranking: " + message,
                                Toast.LENGTH_SHORT).show());
            }
        });

        // Cargar IDs de amigos para el tab "Amigos"
        amigosRepo.misAmigos(new AmigosRepository.ListCallback<Amigo>() {
            @Override
            public void onSuccess(List<Amigo> items) {
                for (Amigo a : items) amigoIds.add(a.getAmigoId());
            }
            @Override
            public void onError(String message) { /* silencioso */ }
        });

        // Tabs
        TabLayout tabs = view.findViewById(R.id.tabLayoutRanking);
        tabs.addTab(tabs.newTab().setText("Global"));
        tabs.addTab(tabs.newTab().setText("Amigos"));
        tabs.addTab(tabs.newTab().setText("Semanal"));
        tabs.addTab(tabs.newTab().setText("Mensual"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // Global
                        adapter.updateLista(rankingGlobal);
                        break;
                    case 1: // Amigos
                        List<Profile> soloAmigos = new ArrayList<>();
                        for (Profile p : rankingGlobal) {
                            if (amigoIds.contains(p.getId())) soloAmigos.add(p);
                        }
                        adapter.updateLista(soloAmigos);
                        break;
                    case 2: // Semanal - mismos datos por ahora
                    case 3: // Mensual - mismos datos por ahora
                        adapter.updateLista(rankingGlobal);
                        break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}