package com.example.barberiaglp;

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
