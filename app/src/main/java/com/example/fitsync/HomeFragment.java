package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.data.model.Rutina;
import com.example.fitsync.data.repository.RutinaRepository;
import com.example.fitsync.data.session.SessionManager;
import com.example.fitsync.ui.adapter.RutinaCardAdapter;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvMisRutinas;
    private View layoutEmptyRutinas;
    private View sectionMisRutinas;
    private RutinaRepository repository;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Saludo
        TextView tvSaludo = view.findViewById(R.id.tvSaludo);
        SessionManager session = new SessionManager(requireContext());
        tvSaludo.setText("Hola, " + extraerNombre(session.getUserEmail()) + "!");

        // Botón generar nueva rutina
        view.findViewById(R.id.btnComenzar).setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_homeFragment_to_generateRoutineFragment));

        // Sección Mis Rutinas
        rvMisRutinas      = view.findViewById(R.id.rvMisRutinas);
        layoutEmptyRutinas = view.findViewById(R.id.layoutEmptyRutinas);
        sectionMisRutinas  = view.findViewById(R.id.sectionMisRutinas);

        rvMisRutinas.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rvMisRutinas.setNestedScrollingEnabled(false);

        repository = new RutinaRepository(requireContext());
        cargarMisRutinas();
    }

    private void cargarMisRutinas() {
        repository.getMyRutinas(new RutinaRepository.MyRutinasCallback() {
            @Override
            public void onSuccess(List<Rutina> rutinas) {
                if (!isAdded()) return;

                sectionMisRutinas.setVisibility(View.VISIBLE);

                if (rutinas.isEmpty()) {
                    layoutEmptyRutinas.setVisibility(View.VISIBLE);
                    rvMisRutinas.setVisibility(View.GONE);
                } else {
                    layoutEmptyRutinas.setVisibility(View.GONE);
                    rvMisRutinas.setVisibility(View.VISIBLE);
                    rvMisRutinas.setAdapter(new RutinaCardAdapter(rutinas, rutina -> {
                        Bundle args = new Bundle();
                        args.putString("rutinaId", rutina.getId());
                        args.putString("rutinaNombre", rutina.getNombre());
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_homeFragment_to_rutinaDetailFragment, args);
                    }));
                }
            }

            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                sectionMisRutinas.setVisibility(View.VISIBLE);
                layoutEmptyRutinas.setVisibility(View.VISIBLE);
                rvMisRutinas.setVisibility(View.GONE);
            }
        });
    }

    private String extraerNombre(String email) {
        if (email == null || email.isEmpty()) return "atleta";
        String parte = email.split("@")[0];
        if (parte.isEmpty()) return "atleta";
        return Character.toUpperCase(parte.charAt(0)) + parte.substring(1);
    }
}