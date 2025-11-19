package com.example.barberiaglp.SeccionesPrincipales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import com.example.barberiaglp.BD.DataSeeder;
import com.example.barberiaglp.LoginAndRegister.LoginActivity;
import com.example.barberiaglp.R;
import com.example.barberiaglp.SeccionesPrincipales.Inicio.InicioFragment;
import com.example.barberiaglp.SeccionesPrincipales.Perfil.PerfilFragment;
import com.example.barberiaglp.SeccionesPrincipales.SobreNosotros.MapsActivity;
import com.example.barberiaglp.SeccionesPrincipales.Turnos.TurnosFragment;

public class MainActivity extends AppCompatActivity {

    LinearLayout navInicio, navTurnos, navSobreNosotros, navPerfil;
    ImageView iconInicio, iconTurnos, iconSobreNosotros, iconPerfil;
    TextView textoInicio, textoSobreNosotros, textoTurnos, textoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Carga de valores predeterminados en la bd
        DataSeeder.cargarDatos(this);

        //Esto es para que el nav no tape el contenido del fragmento
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragmentContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); // O deja el padding en 0, dependiendo de lo que necesites
            return insets;
        });
        EdgeToEdge.enable(this); // Esto se encarga del fitsSystemWindows
        // Asegurar que los iconos de la barra de estado sean claros (blancos)
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(false);
        }
        verificarSesion();

        //Botones
        navInicio = findViewById(R.id.navInicio);
        navTurnos = findViewById(R.id.navTurnos);
        navSobreNosotros = findViewById(R.id.navServicios);
        navPerfil = findViewById(R.id.navPerfil);

        //Elementos para cambiar el color al seleccionado (Icono y Texto)
        iconInicio = findViewById(R.id.iconInicio);
        textoInicio = findViewById(R.id.NavInicio);
        iconTurnos = findViewById(R.id.iconTurnos);
        textoTurnos = findViewById(R.id.NavMisTurnos);
        iconSobreNosotros = findViewById(R.id.iconSobreNosotros);
        textoSobreNosotros = findViewById(R.id.NavSobreNosotros);
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

        navSobreNosotros.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.barberiaglp.SeccionesPrincipales.SobreNosotros.MapsActivity.class);
            startActivity(intent);
            highlightSelected(iconSobreNosotros, textoSobreNosotros);
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

        if (!isLoggedIn) { // Si el usuario NO ha iniciado sesión:
            // 1. Inicia la actividad de Login.
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // 2. Añade flags para limpiar el historial de actividades.
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
        iconSobreNosotros.setColorFilter(Color.parseColor("#C7C4C4"));
        iconPerfil.setColorFilter(Color.parseColor("#C7C4C4"));
        textoInicio.setTextColor(Color.parseColor("#C7C4C4"));
        textoTurnos.setTextColor(Color.parseColor("#C7C4C4"));
        textoSobreNosotros.setTextColor(Color.parseColor("#C7C4C4"));
        textoPerfil.setTextColor(Color.parseColor("#C7C4C4"));

        // Marcar el ícono seleccionado
        selectedIcon.setColorFilter(Color.parseColor("#8B0000"));
        selectedText.setTextColor(Color.parseColor("#8B0000"));
    }
}
