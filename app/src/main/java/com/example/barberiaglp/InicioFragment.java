package com.example.barberiaglp;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InicioFragment extends Fragment {
    public InicioFragment(){}

    TextView bienvenido;
    ImageView notificaciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        bienvenido = v.findViewById(R.id.textBienvenida);

        // Recupera los datos guardados en SharedPreferences
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String nombreGuardado = preferences.getString("nombre", "Usuario");
        bienvenido.setText("Bienvenida, "+ nombreGuardado + "!");


        // Hacer que cuando se toque la campanita se muestren las notificaciones
        return v;
    }

}