package com.example.barberiaglp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Repositorios.UsuarioRepositorio;


public class PerfilFragment extends Fragment {
    public PerfilFragment(){}

    Button logOut, btnBorrarTodo;
    private UsuarioRepositorio usuarioRepo;
    TextView fechadesde;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1 Inflás la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // 2 Obtenés el botón desde la vista inflada
        logOut = view.findViewById(R.id.btnLogOut);

        fechadesde = view.findViewById(R.id.fechaClienteDesde);
        usuarioRepo = new UsuarioRepositorio(requireActivity().getApplication());
        //temporal para las pruebas con la base de datos para eliminar tdos los usuarios
        btnBorrarTodo = view.findViewById(R.id.btnBorrarTodo);
        btnBorrarTodo.setOnClickListener(v -> {
            usuarioRepo.deleteAllUsers();
            Toast.makeText(getContext(), "¡Tabla de usuarios borrada!", Toast.LENGTH_SHORT).show();
            // Después de borrar, es buena idea cerrar sesión para evitar errores
            cerrarsesion();
        });

        // <-- 4. LLAMA AL NUEVO MÉTODO PARA CARGAR LOS DATOS
        colocarDatosUsuario();

        // 3 Le asignás el listener
        logOut.setOnClickListener(v -> cerrarsesion());

        // 4 Retornás la vista inflada
        return view;
    }
    public void cerrarsesion(){
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void colocarDatosUsuario() {
        // Llama al método asíncrono del repositorio para obtener el usuario actual
        usuarioRepo.getUsuarioActual(usuarioActual -> {
            // Este código se ejecuta cuando el repositorio devuelve el usuario.
            // Es crucial volver al hilo de la UI para actualizar la vista.
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (usuarioActual != null) {
                        // El usuario se encontró, ahora actualizamos la interfaz
                        String textoFecha = usuarioActual.fechaRegistro;
                        fechadesde.setText(textoFecha);
                    } else {
                        // Si por alguna razón no se encuentra el usuario
                        fechadesde.setText("Fecha no disponible");
                    }
                });
            }
        });
    }
}