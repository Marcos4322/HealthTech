package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.data.api.SupabaseClient;
import com.example.fitsync.data.model.Profile;
import com.example.fitsync.data.model.Rutina;
import com.example.fitsync.data.repository.AmigosRepository;
import com.example.fitsync.data.session.SessionManager;
import com.example.fitsync.ui.adapter.RutinaAmigoAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendDetailFragment extends Fragment {

    private String amigoId;
    private String amistadId;
    private String usernameArg;

    private TextView tvName, tvUsername, tvNivel, tvRacha, tvRutinasCount, tvEmptyRutinas;
    private RecyclerView rvRutinas;
    private RutinaAmigoAdapter rutinasAdapter;

    private AmigosRepository amigosRepo;
    private SessionManager session;

    public FriendDetailFragment() {
        super(R.layout.fragment_friend_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            amigoId = args.getString("amigoId");
            amistadId = args.getString("amistadId");
            usernameArg = args.getString("username");
        }

        session = new SessionManager(requireContext());
        amigosRepo = new AmigosRepository(requireContext());

        tvName          = view.findViewById(R.id.tvName);
        tvUsername      = view.findViewById(R.id.tvUsername);
        tvNivel         = view.findViewById(R.id.tvNivel);
        tvRacha         = view.findViewById(R.id.tvRacha);
        tvRutinasCount  = view.findViewById(R.id.tvRutinasCount);
        tvEmptyRutinas  = view.findViewById(R.id.tvEmptyRutinas);
        rvRutinas       = view.findViewById(R.id.rvRutinasAmigo);

        rvRutinas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rutinasAdapter = new RutinaAmigoAdapter();
        rvRutinas.setAdapter(rutinasAdapter);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        CardView btnEliminar = view.findViewById(R.id.btnEliminarAmigo);
        btnEliminar.setOnClickListener(v -> confirmarEliminar());

        if (usernameArg != null) {
            tvName.setText(usernameArg);
            tvUsername.setText("@" + usernameArg);
        }

        cargarPerfilAmigo();
        cargarRutinasAmigo();
    }

    private void cargarPerfilAmigo() {
        String token = session.getAccessToken();
        if (token == null || amigoId == null) return;

        SupabaseClient.getDbApi()
                .getProfile("Bearer " + token, "eq." + amigoId, "*")
                .enqueue(new Callback<List<Profile>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Profile>> call,
                                           @NonNull Response<List<Profile>> response) {
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null
                                && !response.body().isEmpty()) {
                            Profile p = response.body().get(0);
                            String nombre = (p.getNombreCompleto() != null
                                    && !p.getNombreCompleto().isEmpty())
                                    ? p.getNombreCompleto() : p.getUsername();
                            tvName.setText(nombre);
                            tvUsername.setText("@" + p.getUsername());
                            tvNivel.setText(String.valueOf(p.getNivel()));
                            tvRacha.setText(String.valueOf(p.getRachaDias()));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Profile>> call,
                                          @NonNull Throwable t) { }
                });
    }

    private void cargarRutinasAmigo() {
        String token = session.getAccessToken();
        if (token == null || amigoId == null) return;

        SupabaseClient.getDbApi()
                .listMyRutinas("Bearer " + token,
                        "eq." + amigoId,
                        "*",
                        "created_at.desc")
                .enqueue(new Callback<List<Rutina>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Rutina>> call,
                                           @NonNull Response<List<Rutina>> response) {
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            List<Rutina> rutinas = response.body();
                            tvRutinasCount.setText(String.valueOf(rutinas.size()));
                            if (rutinas.isEmpty()) {
                                tvEmptyRutinas.setVisibility(View.VISIBLE);
                                rvRutinas.setVisibility(View.GONE);
                            } else {
                                tvEmptyRutinas.setVisibility(View.GONE);
                                rvRutinas.setVisibility(View.VISIBLE);
                                rutinasAdapter.setLista(rutinas);
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Rutina>> call,
                                          @NonNull Throwable t) { }
                });
    }

    private void confirmarEliminar() {
        String nombre = tvName.getText() != null ? tvName.getText().toString() : "este amigo";

        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar amigo")
                .setMessage("¿Seguro que quieres eliminar a " + nombre + " de tus amigos?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarAmigo())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarAmigo() {
        if (amistadId == null) {
            Toast.makeText(requireContext(),
                    "Error: amistad no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }
        amigosRepo.eliminarAmistad(amistadId, new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            "Amigo eliminado", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                });
            }
            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(),
                                "Error: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}