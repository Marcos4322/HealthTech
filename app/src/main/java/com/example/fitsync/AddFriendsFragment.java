package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.data.model.SolicitudPendiente;
import com.example.fitsync.data.model.UsuarioBuscado;
import com.example.fitsync.data.repository.AmigosRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AddFriendsFragment extends Fragment {

    private TextInputEditText etSearch;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;

    private BusquedaAdapter busquedaAdapter;
    private SolicitudAdapter solicitudAdapter;
    private AmigosRepository repo;

    private int currentTab = 0;

    public AddFriendsFragment() {
        super(R.layout.fragment_add_friends);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repo = new AmigosRepository(requireContext());

        etSearch = view.findViewById(R.id.etSearch);
        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerRequests);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        busquedaAdapter = new BusquedaAdapter(this::onEnviarSolicitud);
        solicitudAdapter = new SolicitudAdapter(new SolicitudAdapter.OnSolicitudListener() {
            @Override
            public void onAceptar(SolicitudPendiente s, int pos) {
                aceptarSolicitud(s, pos);
            }

            @Override
            public void onRechazar(SolicitudPendiente s, int pos) {
                rechazarSolicitud(s, pos);
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Buscar"));
        tabLayout.addTab(tabLayout.newTab().setText("Solicitudes"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                if (currentTab == 0) {
                    recyclerView.setAdapter(busquedaAdapter);
                    etSearch.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setAdapter(solicitudAdapter);
                    etSearch.setVisibility(View.GONE);
                    cargarSolicitudes();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        recyclerView.setAdapter(busquedaAdapter);

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                buscar();
                return true;
            }
            return false;
        });
    }

    private void buscar() {
        String termino = etSearch.getText() != null ? etSearch.getText().toString().trim() : "";
        if (termino.length() < 2) {
            Toast.makeText(requireContext(), "Escribe al menos 2 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        repo.buscarUsuarios(termino, new AmigosRepository.ListCallback<UsuarioBuscado>() {
            @Override
            public void onSuccess(List<UsuarioBuscado> items) {
                if (!isAdded()) return;
                if (items.isEmpty()) {
                    Toast.makeText(requireContext(), "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                }
                busquedaAdapter.setItems(items);
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarSolicitudes() {
        repo.misSolicitudesPendientes(new AmigosRepository.ListCallback<SolicitudPendiente>() {
            @Override
            public void onSuccess(List<SolicitudPendiente> items) {
                if (!isAdded()) return;
                if (items.isEmpty()) {
                    Toast.makeText(requireContext(), "No tienes solicitudes pendientes", Toast.LENGTH_SHORT).show();
                }
                solicitudAdapter.setItems(items);
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onEnviarSolicitud(UsuarioBuscado usuario, int position) {
        repo.enviarSolicitud(usuario.getId(), new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Solicitud enviada a " + usuario.getUsername(), Toast.LENGTH_SHORT).show();
                buscar();
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aceptarSolicitud(SolicitudPendiente solicitud, int position) {
        repo.aceptarSolicitud(solicitud.getAmistadId(), new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), solicitud.getUsername() + " es ahora tu amigo", Toast.LENGTH_SHORT).show();
                solicitudAdapter.removeItem(position);
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rechazarSolicitud(SolicitudPendiente solicitud, int position) {
        repo.rechazarSolicitud(solicitud.getAmistadId(), new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Solicitud rechazada", Toast.LENGTH_SHORT).show();
                solicitudAdapter.removeItem(position);
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}