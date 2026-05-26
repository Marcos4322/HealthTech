package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.data.model.Rutina;
import com.example.fitsync.data.model.RutinaEjercicio;
import com.example.fitsync.data.repository.RutinaRepository;
import com.example.fitsync.ui.adapter.EjercicioAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class RutinaDetailFragment extends Fragment {

    public static final String ARG_RUTINA_ID     = "rutinaId";
    public static final String ARG_RUTINA_NOMBRE = "rutinaNombre";

    private TextView tvNombre, tvNivel, tvDuracion, tvCalorias, tvDescripcion, tvTotalEjercicios;
    private RecyclerView rvEjercicios;
    private ProgressBar progressBar;
    private View layoutInfoChips;
    private ExtendedFloatingActionButton fabIniciar;

    private RutinaRepository repository;
    private String rutinaId;
    private List<RutinaEjercicio> ejerciciosCargados;

    public RutinaDetailFragment() {
        super(R.layout.fragment_rutina_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnBack    = view.findViewById(R.id.btnBack);
        ImageButton btnEliminar = view.findViewById(R.id.btnEliminar);
        fabIniciar             = view.findViewById(R.id.fabIniciar);
        tvNombre               = view.findViewById(R.id.tvRutinaNombre);
        tvNivel                = view.findViewById(R.id.tvNivel);
        tvDuracion             = view.findViewById(R.id.tvDuracion);
        tvCalorias             = view.findViewById(R.id.tvCalorias);
        tvDescripcion          = view.findViewById(R.id.tvDescripcion);
        tvTotalEjercicios      = view.findViewById(R.id.tvTotalEjercicios);
        rvEjercicios           = view.findViewById(R.id.rvEjercicios);
        progressBar            = view.findViewById(R.id.progressBar);
        layoutInfoChips        = view.findViewById(R.id.layoutInfoChips);

        repository = new RutinaRepository(requireContext());

        Bundle args = getArguments();
        if (args != null) {
            rutinaId = args.getString(ARG_RUTINA_ID, "");
            tvNombre.setText(args.getString(ARG_RUTINA_NOMBRE, "Rutina"));
        }

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        btnEliminar.setOnClickListener(v -> confirmarEliminar());
        fabIniciar.setOnClickListener(v -> iniciarRutina());
        fabIniciar.setVisibility(View.GONE); // oculto hasta que carguen los ejercicios

        rvEjercicios.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvEjercicios.setNestedScrollingEnabled(false);

        cargarDatos();
    }

    // ─── Carga ───────────────────────────────────────────────────────────────

    private void cargarDatos() {
        setLoading(true);
        repository.getRutinaById(rutinaId, new RutinaRepository.RutinaCallback() {
            @Override
            public void onSuccess(Rutina rutina) {
                if (!isAdded()) return;
                mostrarCabeceraRutina(rutina);
                cargarEjercicios();
            }
            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                setLoading(false);
                Toast.makeText(requireContext(),
                        "Error al cargar la rutina: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarEjercicios() {
        repository.getRutinaEjercicios(rutinaId, new RutinaRepository.EjerciciosCallback() {
            @Override
            public void onSuccess(List<RutinaEjercicio> ejercicios) {
                if (!isAdded()) return;
                setLoading(false);
                ejerciciosCargados = ejercicios;
                tvTotalEjercicios.setText(ejercicios.size() + " ejercicios");
                rvEjercicios.setAdapter(new EjercicioAdapter(ejercicios));
                fabIniciar.setVisibility(ejercicios.isEmpty() ? View.GONE : View.VISIBLE);
            }
            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                setLoading(false);
                Toast.makeText(requireContext(),
                        "Error al cargar ejercicios: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // ─── Eliminar ────────────────────────────────────────────────────────────

    private void confirmarEliminar() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar rutina")
                .setMessage("¿Seguro que quieres eliminar esta rutina? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarRutina())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarRutina() {
        repository.deleteRutina(rutinaId, new RutinaRepository.DeleteCallback() {
            @Override
            public void onSuccess() {
                if (!isAdded()) return;
                Toast.makeText(requireContext(),
                        "Rutina eliminada", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigateUp();
            }
            @Override
            public void onError(String message) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(),
                        "Error al eliminar: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // ─── Iniciar rutina ──────────────────────────────────────────────────────

    private void iniciarRutina() {
        if (ejerciciosCargados == null || ejerciciosCargados.isEmpty()) return;

        Bundle args = new Bundle();
        args.putString("rutinaId", rutinaId);
        args.putString("rutinaNombre", tvNombre.getText().toString());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_rutinaDetailFragment_to_workoutFragment, args);
    }

    // ─── UI ──────────────────────────────────────────────────────────────────

    private void mostrarCabeceraRutina(Rutina rutina) {
        tvNombre.setText(rutina.getNombre());
        if (rutina.getNivel() != null && !rutina.getNivel().isEmpty()) {
            tvNivel.setText(capitalizar(rutina.getNivel()));
            tvNivel.setVisibility(View.VISIBLE);
        }
        if (rutina.getDuracionEstimadaMin() != null) {
            tvDuracion.setText(rutina.getDuracionEstimadaMin() + " min");
            tvDuracion.setVisibility(View.VISIBLE);
        }
        if (rutina.getCaloriasEstimadas() != null) {
            tvCalorias.setText(rutina.getCaloriasEstimadas() + " kcal");
            tvCalorias.setVisibility(View.VISIBLE);
        }
        if (rutina.getDescripcion() != null && !rutina.getDescripcion().isEmpty()) {
            tvDescripcion.setText(rutina.getDescripcion());
            tvDescripcion.setVisibility(View.VISIBLE);
        }
        layoutInfoChips.setVisibility(View.VISIBLE);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private String capitalizar(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}