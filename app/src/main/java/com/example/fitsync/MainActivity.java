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

        // 1. Encontrar la barra de navegación inferior (BottomNavigationView)
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);

        // 2. Encontrar el NavHostFragment (el contenedor de las pantallas)
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            // 3. Obtener el controlador de navegación
            NavController navController = navHostFragment.getNavController();

            // 4. Vincular la barra inferior con el controlador (¡Esto hace que los botones funcionen!)
            NavigationUI.setupWithNavController(bottomNav, navController);

            // 5. Lógica para ocultar la barra inferior si estamos en el Login
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller,
                                                 @NonNull NavDestination destination,
                                                 @Nullable Bundle arguments) {
                    if (destination.getId() == R.id.loginFragment) {
                        bottomNav.setVisibility(View.GONE); // Ocultar en Login
                    } else {
                        bottomNav.setVisibility(View.VISIBLE); // Mostrar en el resto
                    }
                }
            });
        }
    }
}