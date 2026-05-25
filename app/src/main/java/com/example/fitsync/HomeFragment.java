package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitsync.data.session.SessionManager;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvSaludo = view.findViewById(R.id.tvSaludo);

        SessionManager session = new SessionManager(requireContext());
        String email = session.getUserEmail();
        String nombre = extraerNombre(email);
        tvSaludo.setText("Hola, " + nombre + "!");

        Button btnComenzar = view.findViewById(R.id.btnComenzar);
        btnComenzar.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_homeFragment_to_generateRoutineFragment));
    }

    private String extraerNombre(String email) {
        if (email == null || email.isEmpty()) return "atleta";
        String parte = email.split("@")[0];
        if (parte.isEmpty()) return "atleta";
        return Character.toUpperCase(parte.charAt(0)) + parte.substring(1);
    }
}