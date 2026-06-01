package com.example.fitsync;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SocialFragment extends Fragment {

    public SocialFragment() {
        super(R.layout.fragment_social);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Amigos (ahora incluye buscar y solicitudes)
        view.findViewById(R.id.cardAmigos).setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_socialFragment_to_friendsFragment));

        view.findViewById(R.id.cardRanking).setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_socialFragment_to_rankingFragment));

        view.findViewById(R.id.cardGrupos).setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_socialFragment_to_groupsFragment));
    }
}