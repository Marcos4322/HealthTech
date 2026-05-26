package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class WorkoutSummaryFragment extends Fragment {

    public WorkoutSummaryFragment() {
        super(R.layout.fragment_workout_summary);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvNombre    = view.findViewById(R.id.tvResumenNombre);
        TextView tvDuracion  = view.findViewById(R.id.tvResumenDuracion);
        TextView tvEjercicios = view.findViewById(R.id.tvResumenEjercicios);
        TextView tvSeries    = view.findViewById(R.id.tvResumenSeries);
        Button btnVolver     = view.findViewById(R.id.btnVolverHome);

        Bundle args = getArguments();
        if (args != null) {
            tvNombre.setText(args.getString("rutinaNombre", ""));
            tvDuracion.setText(String.valueOf(Math.max(1, args.getLong("duracionMin"))));
            tvEjercicios.setText(String.valueOf(args.getInt("totalEjercicios")));
            tvSeries.setText(String.valueOf(args.getInt("totalSeries")));
        }

        btnVolver.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_workoutSummaryFragment_to_homeFragment));
    }
}