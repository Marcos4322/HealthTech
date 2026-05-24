package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class FriendsFragment extends Fragment {

    public FriendsFragment() {
        super(R.layout.fragment_friends);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Botón volver
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        // Botón "Añadir Amigos" → navega a AddFriendsFragment
        Button btnAdd = view.findViewById(R.id.btnAddFriend);
        btnAdd.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_friendsFragment_to_addFriendsFragment));
    }
}