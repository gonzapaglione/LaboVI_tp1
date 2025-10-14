package com.example.barberiaglp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    LinearLayout navInicio, navTurnos, navServicios, navPerfil;
    ImageView iconInicio, iconTurnos, iconServicios, iconPerfil;
    TextView textoInicio, textoServicios, textoTurnos, textoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificarSesion();

        //Botones
        navInicio = findViewById(R.id.navInicio);
        navTurnos = findViewById(R.id.navTurnos);
        navServicios = findViewById(R.id.navServicios);
        navPerfil = findViewById(R.id.navPerfil);

        //Elementos para cambiar el color al seleccionado (Icono y Texto)
        iconInicio = findViewById(R.id.iconInicio);
        textoInicio = findViewById(R.id.NavInicio);
        iconTurnos = findViewById(R.id.iconTurnos);
        textoTurnos = findViewById(R.id.NavMisTurnos);
        iconServicios = findViewById(R.id.iconServicios);
        textoServicios = findViewById(R.id.NavServicios);
        iconPerfil = findViewById(R.id.iconPerfil);
        textoPerfil = findViewById(R.id.NavPerfil);

        // Fragmento inicial
        replaceFragment(new InicioFragment());
        highlightSelected(iconInicio, textoInicio);

        // Listeners
        navInicio.setOnClickListener(v -> {
            replaceFragment(new InicioFragment());
            highlightSelected(iconInicio, textoInicio);
        });

        navTurnos.setOnClickListener(v -> {
            replaceFragment(new TurnosFragment());
            highlightSelected(iconTurnos, textoTurnos);
        });

        navServicios.setOnClickListener(v -> {
            replaceFragment(new ServiciosFragment());
            highlightSelected(iconServicios, textoServicios);
        });

        navPerfil.setOnClickListener(v -> {
            replaceFragment(new PerfilFragment());
            highlightSelected(iconPerfil, textoPerfil);
        });
    }
    private void verificarSesion() {
        // Obtiene el archivo de preferencias "UserPrefs"
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Lee el valor booleano de "isLoggedIn". Si no existe, devuelve 'false' por defecto.
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Si el usuario NO ha iniciado sesión:
            // 1. Inicia la actividad de Login.
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // 2. Añade flags para limpiar el historial de actividades.
            //    Así, el usuario no puede volver a la MainActivity con el botón de "atrás".
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // 3. Finaliza la MainActivity actual para que no se quede en segundo plano.
            finish();
        }
    }


    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void highlightSelected(ImageView selectedIcon, TextView selectedText) {
        // Resetear colores
        iconInicio.setColorFilter(Color.parseColor("#C7C4C4"));
        iconTurnos.setColorFilter(Color.parseColor("#C7C4C4"));
        iconServicios.setColorFilter(Color.parseColor("#C7C4C4"));
        iconPerfil.setColorFilter(Color.parseColor("#C7C4C4"));
        textoInicio.setTextColor(Color.parseColor("#C7C4C4"));
        textoTurnos.setTextColor(Color.parseColor("#C7C4C4"));
        textoServicios.setTextColor(Color.parseColor("#C7C4C4"));
        textoPerfil.setTextColor(Color.parseColor("#C7C4C4"));

        // Marcar el ícono seleccionado
        selectedIcon.setColorFilter(Color.parseColor("#8B0000"));
        selectedText.setTextColor(Color.parseColor("#8B0000"));
    }
}
