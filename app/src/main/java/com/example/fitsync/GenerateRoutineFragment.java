package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitsync.data.model.RoutineGeneratedResponse;
import com.example.fitsync.data.model.RoutineRequest;
import com.example.fitsync.data.repository.RutinaRepository;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;

public class GenerateRoutineFragment extends Fragment {

    private ChipGroup cgObjetivo, cgNivel, cgEquipamiento;
    private Slider sliderDias;
    private TextView tvDiasValor;
    private Button btnGenerar;
    private FrameLayout loadingOverlay;

    public GenerateRoutineFragment() {
        super(R.layout.fragment_generate_routine);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        cgObjetivo = view.findViewById(R.id.chipGroupObjetivo);
        cgNivel = view.findViewById(R.id.chipGroupNivel);
        cgEquipamiento = view.findViewById(R.id.chipGroupEquipamiento);
        sliderDias = view.findViewById(R.id.sliderDias);
        tvDiasValor = view.findViewById(R.id.tvDiasValor);
        btnGenerar = view.findViewById(R.id.btnGenerar);
        loadingOverlay = view.findViewById(R.id.loadingOverlay);

        // Defaults: seleccionamos un chip por defecto en cada grupo
        cgObjetivo.check(R.id.chipObjGanarMusculo);
        cgNivel.check(R.id.chipNivIntermedio);
        cgEquipamiento.check(R.id.chipEqGimnasioCompleto);

        // Slider: actualiza el número grande en vivo
        sliderDias.addOnChangeListener((slider, value, fromUser) ->
                tvDiasValor.setText(String.valueOf((int) value)));

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        btnGenerar.setOnClickListener(v -> generar());
    }

    private void generar() {
        String objetivo = chipToObjetivo(cgObjetivo.getCheckedChipId());
        String nivel = chipToNivel(cgNivel.getCheckedChipId());
        String equipamiento = chipToEquipamiento(cgEquipamiento.getCheckedChipId());
        int dias = (int) sliderDias.getValue();

        if (objetivo == null || nivel == null || equipamiento == null) {
            Toast.makeText(requireContext(), "Selecciona todas las opciones", Toast.LENGTH_SHORT).show();
            return;
        }

        RoutineRequest request = new RoutineRequest(objetivo, nivel, dias, equipamiento);

        setLoading(true);
        new RutinaRepository(requireContext()).generateRoutineWithAI(request,
                new RutinaRepository.GenerateCallback() {
                    @Override
                    public void onSuccess(RoutineGeneratedResponse response) {
                        if (!isAdded()) return;
                        setLoading(false);

                        Toast.makeText(requireContext(),
                                "Rutina creada: " + response.getNombre(),
                                Toast.LENGTH_SHORT).show();

                        // Navegar al detalle de la rutina pasando el ID
                        Bundle args = new Bundle();
                        args.putString("rutinaId", response.getRutinaId());
                        args.putString("rutinaNombre", response.getNombre());
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_generateRoutineFragment_to_rutinaDetailFragment, args);
                    }

                    @Override
                    public void onError(String message) {
                        if (!isAdded()) return;
                        setLoading(false);
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String chipToObjetivo(int chipId) {
        if (chipId == R.id.chipObjPerderGrasa) return "perder grasa";
        if (chipId == R.id.chipObjGanarMusculo) return "ganar musculo";
        if (chipId == R.id.chipObjTonificar) return "tonificar";
        if (chipId == R.id.chipObjResistencia) return "resistencia";
        return null;
    }

    private String chipToNivel(int chipId) {
        if (chipId == R.id.chipNivPrincipiante) return "principiante";
        if (chipId == R.id.chipNivIntermedio) return "intermedio";
        if (chipId == R.id.chipNivAvanzado) return "avanzado";
        return null;
    }

    private String chipToEquipamiento(int chipId) {
        if (chipId == R.id.chipEqNinguno) return "ninguno";
        if (chipId == R.id.chipEqCasaBasico) return "casa_basico";
        if (chipId == R.id.chipEqGimnasioCompleto) return "gimnasio_completo";
        return null;
    }

    private void setLoading(boolean loading) {
        loadingOverlay.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnGenerar.setEnabled(!loading);
    }
}