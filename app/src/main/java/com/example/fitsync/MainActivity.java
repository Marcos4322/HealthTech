package com.example.fitsync;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Configurar BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);

        // 2. Obtener el NavController desde el NavHostFragment
        // (Nota: usar findNavController directamente en onCreate a veces falla con FragmentContainerView, esta es la forma segura)
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // 3. Vincular el menú con el controlador de navegación
            NavigationUI.setupWithNavController(bottomNav, navController);

            // 4. Lógica para ocultar la barra inferior en la pantalla de Login
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller,
                                                 @NonNull NavDestination destination,
                                                 @Nullable Bundle arguments) {
                    if (destination.getId() == R.id.loginFragment) {
                        bottomNav.setVisibility(View.GONE);
                    } else {
                        bottomNav.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}