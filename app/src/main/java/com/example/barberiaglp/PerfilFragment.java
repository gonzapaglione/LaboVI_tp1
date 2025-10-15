package com.example.barberiaglp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import Repositorios.UsuarioRepositorio;

import androidx.appcompat.app.AlertDialog; // Asegúrate de importar esto

public class PerfilFragment extends Fragment {
    public PerfilFragment(){}

    Button logOut, btnBorrarTodo;
    private UsuarioRepositorio usuarioRepo;
    TextView fechadesde;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        logOut = view.findViewById(R.id.btnLogOut);

        logOut.setOnClickListener(v -> mostrarDialogoCerrarSesion());
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
        logOut.setOnClickListener(v -> mostrarDialogoCerrarSesion());

        return view;
    }

    public void mostrarDialogoCerrarSesion() {
        // Crear un título con color blanco
        SpannableString title = new SpannableString("Cerrar sesión");
        title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),
                0, title.length(), 0);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(title) // Asignamos el SpannableString
                .setMessage("¿Estás seguro que deseas cerrar sesión?")
                .setPositiveButton("Sí", (d, w) -> cerrarsesion())
                .setNegativeButton("No", null)
                .show();

        // Cambiar el fondo
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(getResources().getColor(R.color.red_3))
            );
        }

        // Botones blancos
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.white));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.white));

        // Mensaje blanco
        TextView message = dialog.findViewById(android.R.id.message);
        if (message != null) message.setTextColor(getResources().getColor(R.color.white));
    }

    public void cerrarsesion(){
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("userEmail");
        editor.remove("userPassword");
        editor.putBoolean("rememberMe", false);
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void colocarDatosUsuario() {
        // Llama al metodo asincrono del repositorio para obtener el usuario actual
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