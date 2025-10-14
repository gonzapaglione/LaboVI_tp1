package com.example.barberiaglp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import BD.AppDatabase;
import Daos.UsuarioDao;
import Modelos.Usuario;

public class InicioFragment extends Fragment {
    public InicioFragment(){}

    TextView bienvenido;
    AppDatabase db = AppDatabase.getInstance(getContext());
    UsuarioDao userDao = db.usuarioDao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicio, container, false);

        bienvenido = v.findViewById(R.id.textBienvenida);

        // Recupera los datos guardados en SharedPreferences
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String mailGuardado = preferences.getString("email", "");

        new Thread(() -> {
            // Esta parte se ejecuta fuera del hilo principal
            final Usuario usuario = userDao.findByEmail(mailGuardado); // Necesitarás un método findByEmail en tu DAO

            // Para actualizar la UI, debes volver al hilo principal
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (usuario != null) {
                        bienvenido.setText("¡Bienvenida, " + usuario.nombre + "!");
                    } else {
                        // Opcional: manejar el caso de que no se encuentre el usuario
                        bienvenido.setText("¡Bienvenida!");
                    }
                });
            }
        }).start();
        return v;
    }

    public void cerrarsesion(){
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("userEmail");
        editor.remove("userName");
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}