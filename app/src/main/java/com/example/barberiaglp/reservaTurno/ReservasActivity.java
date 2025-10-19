package com.example.barberiaglp.reservaTurno;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.barberiaglp.R;

public class ReservasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        //Esto es para que el nav no tape el contenido del fragmento
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reserva_fragment_container), (v, insets) -> {
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
        // Iniciar el ViewModel y el primer fragmento
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.reserva_fragment_container, new ReservaFragment1())
                    .commit();
        }
    }
}
