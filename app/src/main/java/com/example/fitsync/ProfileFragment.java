package com.example.fitsync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.fitsync.data.model.Profile;
import com.example.fitsync.data.repository.ProfileRepository;
import com.example.fitsync.data.session.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvNivelRacha;
    private TextView tvEntrenosCount;
    private TextView tvRachaCount;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tv_profile_username);
        tvNivelRacha = view.findViewById(R.id.tv_profile_subtitle);
        tvEntrenosCount = view.findViewById(R.id.tv_entrenos_count);
        tvRachaCount = view.findViewById(R.id.tv_racha_count);

        CardView btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> mostrarDialogoCerrarSesion());

        cargarPerfil();
        cargarEntrenos();
    }

    private void cargarPerfil() {
        tvUsername.setText("Cargando...");
        tvNivelRacha.setText("");

        new ProfileRepository(requireContext()).getMyProfile(new ProfileRepository.ProfileCallback() {
            @Override
            public void onSuccess(Profile profile) {
                if (!isAdded()) return;
                tvUsername.setText(profile.getUsername());

                String subtitulo;
                if (profile.getNombreCompleto() != null && !profile.getNombreCompleto().isEmpty()) {
                    subtitulo = profile.getNombreCompleto() + " • Nivel " + profile.getNivel();
                } else {
                    subtitulo = "Nivel " + profile.getNivel() + " • " + profile.getRachaDias() + " días de racha";
                }
                tvNivelRacha.setText(subtitulo);

                // La card "Racha" se rellena aquí porque el dato viene del profile
                tvRachaCount.setText(profile.getRachaDias() + " Días");
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                tvUsername.setText("Error");
                tvNivelRacha.setText(message);
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarEntrenos() {
        new ProfileRepository(requireContext()).countMyCompletedSessions(new ProfileRepository.CountCallback() {
            @Override
            public void onSuccess(int count) {
                if (!isAdded()) return;
                tvEntrenosCount.setText(String.valueOf(count));
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                // Si falla, dejamos el "0" del XML. No mostramos toast para no molestar.
            }
        });
    }

    private void mostrarDialogoCerrarSesion() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que quieres cerrar tu sesión?")
                .setPositiveButton("Sí, cerrar", (dialog, which) -> cerrarSesion())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cerrarSesion() {
        new SessionManager(requireContext()).clearSession();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}