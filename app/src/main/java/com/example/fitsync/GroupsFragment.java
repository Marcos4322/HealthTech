package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitsync.data.model.GroupPost;
import com.example.fitsync.ui.adapter.GroupPostAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupsFragment extends Fragment {

    public GroupsFragment() {
        super(R.layout.fragment_groups);
    }

    private GroupPostAdapter adapter;
    private List<GroupPost> todosLosPosts;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView
        RecyclerView recycler = view.findViewById(R.id.recyclerGroups);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Datos de ejemplo (reemplaza con llamada a Supabase cuando tengas el endpoint)
        todosLosPosts = Arrays.asList(
                new GroupPost("Paula Gómez",   "Gym Crew",     "Hace 5 min",
                        "¡Nuevo récord personal en sentadillas! Superando mis límites. #fitness #gymlife",
                        128, 15, 8),
                new GroupPost("Carlos Ruiz",   "Running Club", "Hace 20 min",
                        "10km completados esta mañana. ¡El mejor comienzo de semana! 🏃",
                        94, 7, 3),
                new GroupPost("María López",   "Yoga Fans",    "Hace 1 hora",
                        "Sesión de yoga matutino completada. La calma antes del día. 🧘",
                        67, 12, 5),
                new GroupPost("Javier Torres", "Gym Crew",     "Hace 2 horas",
                        "Rutina de espalda terminada. Progreso lento pero seguro 💪",
                        45, 4, 2),
                new GroupPost("Ana Martín",    "Running Club", "Hace 3 horas",
                        "¿Alguien se apunta a la carrera del domingo? ¡Somos equipo!",
                        38, 22, 10)
        );

        adapter = new GroupPostAdapter(new ArrayList<>(todosLosPosts));
        recycler.setAdapter(adapter);

        // Chips de filtro
        configurarChips(view);

        // Botones
        view.findViewById(R.id.btnCreateGroup).setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Crear grupo (próximamente)", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btnJoinGroup).setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Unirse a grupo (próximamente)", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.fabNewPost).setOnClickListener(v ->
                Toast.makeText(requireContext(),
                        "Nueva publicación (próximamente)", Toast.LENGTH_SHORT).show());
    }

    private void configurarChips(View view) {
        TextView chipTodos   = view.findViewById(R.id.chipTodos);
        TextView chipRunning = view.findViewById(R.id.chipRunning);
        TextView chipYoga    = view.findViewById(R.id.chipYoga);
        TextView chipGym     = view.findViewById(R.id.chipGym);

        View[] chips = {chipTodos, chipRunning, chipYoga, chipGym};

        chipTodos.setOnClickListener(v -> {
            seleccionarChip(chips, chipTodos);
            adapter.updateLista(new ArrayList<>(todosLosPosts));
        });

        chipRunning.setOnClickListener(v -> {
            seleccionarChip(chips, chipRunning);
            adapter.updateLista(filtrarPorGrupo("Running Club"));
        });

        chipYoga.setOnClickListener(v -> {
            seleccionarChip(chips, chipYoga);
            adapter.updateLista(filtrarPorGrupo("Yoga Fans"));
        });

        chipGym.setOnClickListener(v -> {
            seleccionarChip(chips, chipGym);
            adapter.updateLista(filtrarPorGrupo("Gym Crew"));
        });
    }

    private void seleccionarChip(View[] todos, TextView seleccionado) {
        for (View chip : todos) {
            chip.setBackgroundColor(0xFF2C2C2C);
            if (chip instanceof TextView) ((TextView) chip).setTextColor(0xFFCCCCCC);
        }
        seleccionado.setBackgroundColor(0xFFCC0000);
        seleccionado.setTextColor(0xFFFFFFFF);
    }

    private List<GroupPost> filtrarPorGrupo(String grupo) {
        List<GroupPost> filtrados = new ArrayList<>();
        for (GroupPost p : todosLosPosts) {
            if (p.getGrupo().equals(grupo)) filtrados.add(p);
        }
        return filtrados;
    }
}