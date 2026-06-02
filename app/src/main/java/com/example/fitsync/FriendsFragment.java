package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.data.model.Amigo;
import com.example.fitsync.data.model.SolicitudPendiente;
import com.example.fitsync.data.model.UsuarioBuscado;
import com.example.fitsync.data.repository.AmigosRepository;
import com.example.fitsync.ui.adapter.AmigosAdapter;
import com.example.fitsync.ui.adapter.BusquedaAdapter;
import com.example.fitsync.ui.adapter.SolicitudAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class FriendsFragment extends Fragment {

    private RecyclerView recycler;
    private TextInputLayout layoutSearch;
    private TextInputEditText etSearch;
    private AmigosRepository repo;

    private AmigosAdapter amigosAdapter;
    private BusquedaAdapter busquedaAdapter;
    private SolicitudAdapter solicitudAdapter;

    public FriendsFragment() {
        super(R.layout.fragment_friends);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repo = new AmigosRepository(requireContext());

        recycler     = view.findViewById(R.id.recyclerFriends);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        etSearch     = view.findViewById(R.id.etSearch);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        amigosAdapter = new AmigosAdapter(new java.util.ArrayList<>());
        amigosAdapter.setOnAmigoListener(new AmigosAdapter.OnAmigoListener() {
            @Override
            public void onClick(Amigo amigo, int position) {
                Bundle args = new Bundle();
                args.putString("amigoId", amigo.getAmigoId());
                args.putString("amistadId", amigo.getAmistadId());
                args.putString("username", amigo.getUsername());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_friendsFragment_to_friendDetailFragment, args);
            }

            @Override
            public void onEliminar(Amigo amigo, int position) {
                confirmarEliminar(amigo, position);
            }
        });

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

        TabLayout tabs = view.findViewById(R.id.tabLayoutFriends);
        tabs.addTab(tabs.newTab().setText("Mis Amigos"));
        tabs.addTab(tabs.newTab().setText("Buscar"));
        tabs.addTab(tabs.newTab().setText("Solicitudes"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        layoutSearch.setVisibility(View.GONE);
                        recycler.setAdapter(amigosAdapter);
                        cargarAmigos();
                        break;
                    case 1:
                        layoutSearch.setVisibility(View.VISIBLE);
                        recycler.setAdapter(busquedaAdapter);
                        break;
                    case 2:
                        layoutSearch.setVisibility(View.GONE);
                        recycler.setAdapter(solicitudAdapter);
                        cargarSolicitudes();
                        break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        recycler.setAdapter(amigosAdapter);
        cargarAmigos();

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                buscar();
                return true;
            }
            return false;
        });
    }

    // ── Mis Amigos ──

    private void cargarAmigos() {
        repo.misAmigos(new AmigosRepository.ListCallback<Amigo>() {
            @Override
            public void onSuccess(List<Amigo> items) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    amigosAdapter.updateLista(items);
                    if (items.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Aún no tienes amigos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void confirmarEliminar(Amigo amigo, int position) {
        String nombre = amigo.getNombreCompleto() != null
                ? amigo.getNombreCompleto() : amigo.getUsername();

        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar amigo")
                .setMessage("¿Seguro que quieres eliminar a " + nombre + " de tus amigos?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarAmigo(amigo, position))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarAmigo(Amigo amigo, int position) {
        repo.eliminarAmistad(amigo.getAmistadId(), new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            "Amigo eliminado", Toast.LENGTH_SHORT).show();
                    amigosAdapter.removeItem(position);
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

    // ── Buscar ──

    private void buscar() {
        String termino = etSearch.getText() != null
                ? etSearch.getText().toString().trim() : "";
        if (termino.length() < 2) {
            Toast.makeText(requireContext(),
                    "Escribe al menos 2 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        repo.buscarUsuarios(termino, new AmigosRepository.ListCallback<UsuarioBuscado>() {
            @Override
            public void onSuccess(List<UsuarioBuscado> items) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    busquedaAdapter.setItems(items);
                    if (items.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "No se encontraron usuarios", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void onEnviarSolicitud(UsuarioBuscado usuario, int position) {
        repo.enviarSolicitud(usuario.getId(), new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            "Solicitud enviada a @" + usuario.getUsername(),
                            Toast.LENGTH_SHORT).show();
                    buscar();
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

    // ── Solicitudes ──

    private void cargarSolicitudes() {
        repo.misSolicitudesPendientes(new AmigosRepository.ListCallback<SolicitudPendiente>() {
            @Override
            public void onSuccess(List<SolicitudPendiente> items) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    solicitudAdapter.setItems(items);
                    if (items.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "No tienes solicitudes pendientes",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void aceptarSolicitud(SolicitudPendiente solicitud, int position) {
        repo.aceptarSolicitud(solicitud.getAmistadId(), new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            solicitud.getUsername() + " es ahora tu amigo",
                            Toast.LENGTH_SHORT).show();
                    solicitudAdapter.removeItem(position);
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

    private void rechazarSolicitud(SolicitudPendiente solicitud, int position) {
        repo.rechazarSolicitud(solicitud.getAmistadId(), new AmigosRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(),
                            "Solicitud rechazada", Toast.LENGTH_SHORT).show();
                    solicitudAdapter.removeItem(position);
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