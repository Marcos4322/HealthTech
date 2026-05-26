package com.example.fitsync;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitsync.data.model.Ejercicio;
import com.example.fitsync.data.model.RutinaEjercicio;
import com.example.fitsync.data.repository.RutinaRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkoutFragment extends Fragment {

    private TextView tvNombreRutina, tvProgreso, tvNumeroEjercicio;
    private TextView tvNombreEjercicio, tvGrupoMuscular;
    private TextView tvSeriesValor, tvRepsValor, tvRepsLabel;
    private TextView tvNotas, tvCuentaAtras;
    private ProgressBar progressEjercicios;
    private LinearLayout layoutDescanso;
    private Button btnSiguiente;

    private List<RutinaEjercicio> ejercicios = new ArrayList<>();
    private int indiceActual = 0;
    private CountDownTimer countDownTimer;
    private boolean enDescanso = false;

    // Estadísticas para el resumen
    private int totalEjercicios = 0;
    private int totalSeries = 0;
    private long tiempoInicioMs = 0;

    public WorkoutFragment() {
        super(R.layout.fragment_workout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind
        tvNombreRutina    = view.findViewById(R.id.tvNombreRutina);
        tvProgreso        = view.findViewById(R.id.tvProgreso);
        tvNumeroEjercicio = view.findViewById(R.id.tvNumeroEjercicio);
        tvNombreEjercicio = view.findViewById(R.id.tvNombreEjercicio);
        tvGrupoMuscular   = view.findViewById(R.id.tvGrupoMuscular);
        tvSeriesValor     = view.findViewById(R.id.tvSeriesValor);
        tvRepsValor       = view.findViewById(R.id.tvRepsValor);
        tvRepsLabel       = view.findViewById(R.id.tvRepsLabel);
        tvNotas           = view.findViewById(R.id.tvNotas);
        tvCuentaAtras     = view.findViewById(R.id.tvCuentaAtras);
        progressEjercicios = view.findViewById(R.id.progressEjercicios);
        layoutDescanso    = view.findViewById(R.id.layoutDescanso);
        btnSiguiente      = view.findViewById(R.id.btnSiguiente);

        ImageButton btnCerrar = view.findViewById(R.id.btnCerrar);
        btnCerrar.setOnClickListener(v -> confirmarSalida());
        btnSiguiente.setOnClickListener(v -> onBtnSiguienteClick());

        // Argumentos
        Bundle args = getArguments();
        String rutinaId     = args != null ? args.getString("rutinaId", "")     : "";
        String rutinaNombre = args != null ? args.getString("rutinaNombre", "") : "";
        tvNombreRutina.setText(rutinaNombre);

        tiempoInicioMs = System.currentTimeMillis();

        cargarEjercicios(rutinaId);
    }

    // ─── Carga ───────────────────────────────────────────────────────────────

    private void cargarEjercicios(String rutinaId) {
        new RutinaRepository(requireContext())
                .getRutinaEjercicios(rutinaId, new RutinaRepository.EjerciciosCallback() {
                    @Override
                    public void onSuccess(List<RutinaEjercicio> lista) {
                        if (!isAdded()) return;
                        ejercicios = lista;
                        totalEjercicios = lista.size();
                        mostrarEjercicio(0);
                    }
                    @Override
                    public void onError(String message) { }
                });
    }

    // ─── Navegación entre ejercicios ─────────────────────────────────────────

    private void onBtnSiguienteClick() {
        if (enDescanso) {
            // Saltar el descanso manualmente
            if (countDownTimer != null) countDownTimer.cancel();
            finalizarDescanso();
        } else {
            // Acumular estadísticas del ejercicio actual
            totalSeries += ejercicios.get(indiceActual).getSeries();

            boolean esUltimo = indiceActual >= ejercicios.size() - 1;
            if (esUltimo) {
                irAlResumen();
            } else {
                iniciarDescanso(ejercicios.get(indiceActual).getDescansoSeg());
            }
        }
    }

    private void iniciarDescanso(int segundos) {
        enDescanso = true;
        layoutDescanso.setVisibility(View.VISIBLE);
        btnSiguiente.setText("Saltar descanso");

        countDownTimer = new CountDownTimer(segundos * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isAdded()) return;
                tvCuentaAtras.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                if (!isAdded()) return;
                finalizarDescanso();
            }
        }.start();
    }

    private void finalizarDescanso() {
        enDescanso = false;
        layoutDescanso.setVisibility(View.GONE);
        indiceActual++;
        mostrarEjercicio(indiceActual);
    }

    private void mostrarEjercicio(int indice) {
        RutinaEjercicio re = ejercicios.get(indice);
        Ejercicio ej = re.getEjercicio();

        // Progreso
        tvProgreso.setText((indice + 1) + " / " + ejercicios.size());
        tvNumeroEjercicio.setText(String.valueOf(indice + 1));
        progressEjercicios.setMax(ejercicios.size());
        progressEjercicios.setProgress(indice + 1);

        // Datos ejercicio
        tvNombreEjercicio.setText(ej != null ? ej.getNombre() : "Ejercicio");
        tvGrupoMuscular.setText(ej != null && ej.getGrupoMuscular() != null
                ? ej.getGrupoMuscular().toUpperCase() : "");

        // Series
        tvSeriesValor.setText(String.valueOf(re.getSeries()));

        // Reps o duración
        if (re.getRepeticiones() != null) {
            tvRepsValor.setText(String.valueOf(re.getRepeticiones()));
            tvRepsLabel.setText("reps");
        } else if (re.getDuracionSeg() != null) {
            tvRepsValor.setText(String.valueOf(re.getDuracionSeg()));
            tvRepsLabel.setText("seg");
        } else {
            tvRepsValor.setText("-");
            tvRepsLabel.setText("reps");
        }

        // Notas
        if (re.getNotas() != null && !re.getNotas().isEmpty()) {
            tvNotas.setText(re.getNotas());
            tvNotas.setVisibility(View.VISIBLE);
        } else {
            tvNotas.setVisibility(View.GONE);
        }

        // Botón
        boolean esUltimo = indice >= ejercicios.size() - 1;
        btnSiguiente.setText(esUltimo ? "Finalizar entrenamiento" : "Siguiente ejercicio");
    }

    // ─── Resumen ─────────────────────────────────────────────────────────────

    private void irAlResumen() {
        long duracionMin = (System.currentTimeMillis() - tiempoInicioMs) / 60000;

        Bundle args = new Bundle();
        args.putString("rutinaNombre", tvNombreRutina.getText().toString());
        args.putInt("totalEjercicios", totalEjercicios);
        args.putInt("totalSeries", totalSeries);
        args.putLong("duracionMin", duracionMin);

        Navigation.findNavController(requireView())
                .navigate(R.id.action_workoutFragment_to_workoutSummaryFragment, args);
    }

    // ─── Salir ───────────────────────────────────────────────────────────────

    private void confirmarSalida() {
        if (countDownTimer != null) countDownTimer.cancel();
        new AlertDialog.Builder(requireContext())
                .setTitle("Salir del entrenamiento")
                .setMessage("¿Seguro que quieres abandonar? Se perderá el progreso.")
                .setPositiveButton("Salir", (d, w) ->
                        Navigation.findNavController(requireView()).navigateUp())
                .setNegativeButton("Continuar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}