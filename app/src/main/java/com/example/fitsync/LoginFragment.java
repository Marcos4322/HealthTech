package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    // Constructor que carga el diseño XML del login
    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Buscar el botón por su ID (Asegúrate que en el XML sea @+id/btnLogin)
        Button btnLogin = view.findViewById(R.id.btnLogin);

        // Configurar el click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar hacia el Home usando la acción definida en el grafo
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
            }
        });
    }
}