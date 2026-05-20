package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitsync.data.repository.AuthRepository;
import com.example.fitsync.databinding.FragmentEmailLoginBinding;

public class EmailLoginFragment extends Fragment {

    private FragmentEmailLoginBinding binding;
    private AuthRepository authRepository;

    public EmailLoginFragment() {
        super(R.layout.fragment_email_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentEmailLoginBinding.bind(view);
        authRepository = new AuthRepository(requireContext());

        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        binding.btnLogin.setOnClickListener(v -> entrar());
    }

    private void entrar() {
        String email = binding.etEmail.getText() != null
                ? binding.etEmail.getText().toString().trim() : "";
        String pass = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString() : "";

        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Email no válido");
            return;
        }
        if (pass.isEmpty()) {
            binding.tilPassword.setError("Introduce tu contraseña");
            return;
        }

        setLoading(true);
        authRepository.signIn(email, pass, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                if (binding == null) return;
                setLoading(false);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_emailLoginFragment_to_homeFragment);
            }

            @Override
            public void onError(String message) {
                if (binding == null) return;
                setLoading(false);
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        binding.btnLogin.setEnabled(!loading);
        binding.progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}