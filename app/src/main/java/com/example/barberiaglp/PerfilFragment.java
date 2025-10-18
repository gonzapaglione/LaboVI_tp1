package com.example.barberiaglp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import Repositorios.TurnosRepositorio;
import Repositorios.UsuarioRepositorio;

import androidx.appcompat.app.AlertDialog;

public class PerfilFragment extends Fragment {
    public PerfilFragment(){}

    Button logOut, btnBorrarTodo, btnBorrarTurnos, btnCargarTurnos, btnBorrarSharedP;
    private UsuarioRepositorio usuarioRepo;
    private TurnosRepositorio turnoRepo;
    TextView fechadesde, editar;
    EditText nombre, apellido, email, password;

    ImageButton btnTogglePassword;
    private boolean isPasswordVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        logOut = view.findViewById(R.id.btnLogOut);

        logOut.setOnClickListener(v -> mostrarDialogoCerrarSesion());
        fechadesde = view.findViewById(R.id.fechaClienteDesde);
        nombre = view.findViewById(R.id.inputNombre);
        apellido = view.findViewById(R.id.inputApellido);
        email = view.findViewById(R.id.inputMail);
        password = view.findViewById(R.id.etPassword);
        editar = view.findViewById(R.id.editarDatosPerfil);

        editar.setOnClickListener(v -> activarEdicion(v));
        btnTogglePassword = view.findViewById(R.id.btnTogglePassword);

        btnTogglePassword.setOnClickListener(v -> {
            togglePasswordVisibility();
        });

        usuarioRepo = new UsuarioRepositorio(requireActivity().getApplication());
        //temporal para las pruebas con la base de datos para eliminar tdos los usuarios
        btnBorrarTodo = view.findViewById(R.id.btnBorrarTodo);
        btnBorrarTodo.setOnClickListener(v -> {
            usuarioRepo.deleteAllUsers();
            Toast.makeText(getContext(), "¡Tabla de usuarios borrada!", Toast.LENGTH_SHORT).show();
            cerrarsesion();
        });

        turnoRepo = new TurnosRepositorio(requireActivity().getApplication());
        //temporal para las pruebas con la base de datos para eliminar tdos los turnos
        btnBorrarTurnos = view.findViewById(R.id.btnBorrarTurnos);
        btnBorrarTurnos.setOnClickListener(v -> {
            turnoRepo.deleteAllTurnos();
            Toast.makeText(getContext(), "¡Turnos borrados!", Toast.LENGTH_SHORT).show();
        });

        //botón para cargar turnos predeterminados
        btnCargarTurnos = view.findViewById(R.id.btnCargarTurnos);
        btnCargarTurnos.setOnClickListener(v -> {
            turnoRepo.cargarTurnosPredeterminados();
            Toast.makeText(getContext(), "¡Turnos cargados!", Toast.LENGTH_SHORT).show();
        });

        //boton para eliminar toodo de shared preferences
        btnBorrarSharedP = view.findViewById(R.id.btnBorrarSharedP);
        btnBorrarSharedP.setOnClickListener(v -> {
           SharedPreferences preferences = requireActivity()
                   .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
           SharedPreferences.Editor editor = preferences.edit();
           editor.clear();
           editor.apply();
           Toast.makeText(getContext(), "¡Shared preferences borrados!", Toast.LENGTH_SHORT).show();
        });

        // 4. Llama al nuevo metodo para cargar los datos
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
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (usuarioActual != null) {
                        // Usuario encontrado, ahora actualizamos la interfaz
                        String textoFecha = usuarioActual.fechaRegistro;
                        fechadesde.setText(textoFecha);
                        nombre.setText(usuarioActual.nombre);
                        apellido.setText(usuarioActual.apellido);
                        email.setText(usuarioActual.email);
                        password.setText(usuarioActual.password);
                    } else {
                        // Si por alguna razón no se encuentra el usuario
                        fechadesde.setText("Fecha no disponible");
                    }
                });
            }
        });
    }

    public void activarEdicion(View v) {
        // Obtenemos el texto actual del TextView para decidir qué hacer
        String estadoActual = editar.getText().toString();

        if (estadoActual.equalsIgnoreCase("Editar")) {
            // Cambiamos el texto del botón
            editar.setText("Guardar");

            // Habilitamos los campos para que se puedan editar
            nombre.setEnabled(true);
            apellido.setEnabled(true);
            email.setEnabled(true);
            password.setEnabled(true);

            nombre.requestFocus();

            // Cambia el color del texto para que se vea que es editable
            int colorBlanco = getResources().getColor(android.R.color.white, null);
            nombre.setTextColor(colorBlanco);
            apellido.setTextColor(colorBlanco);
            email.setTextColor(colorBlanco);
            password.setTextColor(colorBlanco);

        } else if (estadoActual.equalsIgnoreCase("Guardar")) {
            guardarCambios();
        }
    }

    private void guardarCambios() {
        // Llamamos a getUsuarioActual para tener el objeto de usuario más reciente
        usuarioRepo.getUsuarioActual(usuarioActual -> {
            if (usuarioActual != null) {
                // 1. Actualizamos el objeto 'usuario' en memoria con los nuevos datos de los EditText
                usuarioActual.nombre = nombre.getText().toString();
                usuarioActual.apellido = apellido.getText().toString();
                usuarioActual.email = email.getText().toString();
                usuarioActual.password = password.getText().toString();

                // 2. Le pasamos el objeto actualizado al repositorio para que lo guarde en la BD
                usuarioRepo.update(usuarioActual);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        restaurarEstadoVisual();
                        Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                // Manejar caso de error
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Error: No se pudo encontrar el usuario para actualizar", Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }

    private void restaurarEstadoVisual() {
        // Cambiamos el texto del botón de vuelta a "Editar"
        editar.setText("Editar");

        // Deshabilitamos los campos nuevamente
        nombre.setEnabled(false);
        apellido.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);

        // Restauramos el color del texto a un gris para indicar que no es editable
        int colorGris = getResources().getColor(android.R.color.darker_gray, null);
        nombre.setTextColor(colorGris);
        apellido.setTextColor(colorGris);
        email.setTextColor(colorGris);
        password.setTextColor(colorGris);
    }

    private void togglePasswordVisibility() {
        // Invertimos el estado actual
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            //mostrar contraseña
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            // cambiamos el icono al ojo "tachado"
            btnTogglePassword.setImageResource(R.drawable.ic_visibility_off);

        } else {
            // ocultar contraseña
            // restaurar el input
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            // cambiamos el icono de vuelta al ojo "normal"
            btnTogglePassword.setImageResource(R.drawable.ic_visibility_on);
        }

        password.setSelection(password.getText().length());
    }

}