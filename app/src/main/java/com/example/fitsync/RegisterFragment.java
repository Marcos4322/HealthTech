package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitsync.data.repository.AuthRepository;
import com.example.fitsync.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthRepository authRepository;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentRegisterBinding.bind(view);
        authRepository = new AuthRepository(requireContext());

        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        binding.btnRegister.setOnClickListener(v -> registrar());
    }

    private void registrar() {
        String email = binding.etEmail.getText() != null
                ? binding.etEmail.getText().toString().trim() : "";
        String pass = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString() : "";
        String passConfirm = binding.etPasswordConfirm.getText() != null
                ? binding.etPasswordConfirm.getText().toString() : "";

        // Validación local
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);
        binding.tilPasswordConfirm.setError(null);

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Email no válido");
            return;
        }
        if (pass.length() < 6) {
            binding.tilPassword.setError("Mínimo 6 caracteres");
            return;
        }
        if (!pass.equals(passConfirm)) {
            binding.tilPasswordConfirm.setError("Las contraseñas no coinciden");
            return;
        }

        setLoading(true);
        authRepository.signUp(email, pass, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                if (binding == null) return; // Fragment ya destruido
                setLoading(false);
                Toast.makeText(requireContext(),
                        "¡Cuenta creada! Bienvenido a FitSync", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_registerFragment_to_homeFragment);
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
        binding.btnRegister.setEnabled(!loading);
        binding.progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}