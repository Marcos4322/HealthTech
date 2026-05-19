package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitsync.data.session.SessionManager;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Si ya hay sesión guardada, salta directamente a Home
        SessionManager session = new SessionManager(requireContext());
        if (session.isLoggedIn()) {
            Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_homeFragment);
            return;
        }

        Button btnRegister = view.findViewById(R.id.btnRegister);
        Button btnLogin = view.findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_loginFragment_to_registerFragment));

        btnLogin.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_loginFragment_to_emailLoginFragment));
    }
}