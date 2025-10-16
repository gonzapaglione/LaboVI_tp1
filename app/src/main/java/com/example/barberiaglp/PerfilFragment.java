package com.example.barberiaglp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import Repositorios.UsuarioRepositorio;

import androidx.appcompat.app.AlertDialog; // Asegúrate de importar esto

public class PerfilFragment extends Fragment {
    public PerfilFragment(){}

    Button logOut, btnBorrarTodo;
    private UsuarioRepositorio usuarioRepo;
    TextView fechadesde, editar;
    EditText nombre, apellido, email, password;

    ImageButton btnTogglePassword; // <-- AÑADE ESTA LÍNEA
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
            // Después de borrar, es buena idea cerrar sesión para evitar errores
            cerrarsesion();
        });

        // <-- 4. LLAMA AL NUEVO METODO PARA CARGAR LOS DATOS
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
            // --- MODO EDICIÓN ---
            // Cambiamos el texto del botón
            editar.setText("Guardar");

            // Habilitamos los campos para que se puedan editar
            nombre.setEnabled(true);
            apellido.setEnabled(true);
            // El email generalmente no se permite cambiar, pero lo dejamos según tu código
            email.setEnabled(true);
            password.setEnabled(true);

            // Opcional: Pon el foco en el primer campo editable para una mejor UX
            nombre.requestFocus();

            // Cambia el color del texto para que se vea que es editable (opcional)
            int colorBlanco = getResources().getColor(android.R.color.white, null);
            nombre.setTextColor(colorBlanco);
            apellido.setTextColor(colorBlanco);
            email.setTextColor(colorBlanco);
            password.setTextColor(colorBlanco);

        } else if (estadoActual.equalsIgnoreCase("Guardar")) {
            // --- MODO GUARDAR ---
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

                // 3. Volvemos al hilo de la UI para restaurar el estado visual
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        restaurarEstadoVisual();
                        Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                // Manejar caso de error (aunque es poco probable si ya estabas en el perfil)
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

        // Restauramos el color del texto a un gris para indicar que no es editable (opcional)
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
            // --- MOSTRAR CONTRASEÑA ---
            // 1. Cambiamos el InputType para que el texto sea visible.
            //    Usamos TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD.
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            // 2. Cambiamos el icono al ojo "tachado" (visibility_off).
            btnTogglePassword.setImageResource(R.drawable.ic_visibility_off);

        } else {
            // --- OCULTAR CONTRASEÑA ---
            // 1. Restauramos el InputType para que se muestre como contraseña (puntos).
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            // 2. Cambiamos el icono de vuelta al ojo "normal" (visibility_on).
            btnTogglePassword.setImageResource(R.drawable.ic_visibility_on);
        }

        // MUY IMPORTANTE: Después de cambiar el InputType, debemos mover el cursor al final
        // del texto para que el usuario no se desoriente.
        password.setSelection(password.getText().length());
    }

}