package com.example.barberiaglp.SeccionesPrincipales.Perfil;

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

import com.example.barberiaglp.BD.AppDatabase;
import com.example.barberiaglp.BD.DataSeeder;
import com.example.barberiaglp.LoginAndRegister.LoginActivity;
import com.example.barberiaglp.Modelos.Turno;
import com.example.barberiaglp.R;
import com.example.barberiaglp.Repositorios.TurnosRepositorio;
import com.example.barberiaglp.Repositorios.UsuarioRepositorio;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class PerfilFragment extends Fragment {
    public PerfilFragment() {
    }

    Button logOut, btnCargarTurnosPasados;
    private UsuarioRepositorio usuarioRepo;
    private TurnosRepositorio turnoRepo;
    TextView fechadesde, editar, cantidadCortesPelo, cantidadCortesBarba, cantidadCortesPeloYBarba;
    EditText nombre, apellido, email, password;
    private int usuarioActualId;
    private AppDatabase db;

    ImageButton btnTogglePassword;
    private boolean isPasswordVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        logOut = view.findViewById(R.id.btnLogOut);
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        usuarioActualId = preferences.getInt("userId", -1);
        usuarioRepo = new UsuarioRepositorio(requireActivity().getApplication());
        turnoRepo = new TurnosRepositorio(requireActivity().getApplication());
        cargarHistorialServicios();
        logOut.setOnClickListener(v -> mostrarDialogoCerrarSesion());
        fechadesde = view.findViewById(R.id.fechaClienteDesde);
        nombre = view.findViewById(R.id.inputNombre);
        apellido = view.findViewById(R.id.inputApellido);
        email = view.findViewById(R.id.inputMail);
        password = view.findViewById(R.id.etPassword);
        editar = view.findViewById(R.id.editarDatosPerfil);
        cantidadCortesPelo = view.findViewById(R.id.cantidadCortePelo);
        cantidadCortesBarba = view.findViewById(R.id.cantidadCorteBarba);
        cantidadCortesPeloYBarba = view.findViewById(R.id.cantidadCortePeloYBarba);
        btnCargarTurnosPasados = view.findViewById(R.id.btnCargarTurnosPasados);

        editar.setOnClickListener(v -> activarEdicion(v));
        btnTogglePassword = view.findViewById(R.id.btnTogglePassword);

        btnTogglePassword.setOnClickListener(v -> {
            togglePasswordVisibility();
        });

        btnCargarTurnosPasados.setOnClickListener(v -> {
            DataSeeder.cargarTurnosPredetermiandos(getContext());
        });

        // 4. Llama al nuevo metodo para cargar los datos
        colocarDatosUsuario();
        cargarHistorialServicios();

        // 3 Le asignás el listener
        logOut.setOnClickListener(v -> mostrarDialogoCerrarSesion());

        return view;
    }

    // En PerfilFragment.java

    public void cargarHistorialServicios() {
        // Ejecuta la lógica en un hilo secundario para no bloquear la UI.
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Turno> turnosAtendidos = turnoRepo.getTurnosAtendidos(usuarioActualId);

            int cantidadCP = 0, cantidadCB = 0, cantidadCPYB = 0;

            if (!turnosAtendidos.isEmpty()) {
                for (Turno turno : turnosAtendidos) {
                    if (turno.servicioId == 1) {
                        cantidadCP++;
                    } else if (turno.servicioId == 2) {
                        cantidadCB++;
                    } else if (turno.servicioId == 3) {
                        cantidadCPYB++;
                    }
                }
            }

            // 3. Volvemos al hilo principal para actualizar los TextViews.
            int finalCantidadCP = cantidadCP;
            int finalCantidadCB = cantidadCB;
            int finalCantidadCPYB = cantidadCPYB;

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    cantidadCortesPelo.setText(String.valueOf(finalCantidadCP));
                    cantidadCortesBarba.setText(String.valueOf(finalCantidadCB));
                    cantidadCortesPeloYBarba.setText(String.valueOf(finalCantidadCPYB));
                });
            }

        });
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
                    new ColorDrawable(getResources().getColor(R.color.red_3)));
        }

        // Botones blancos
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.white));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.white));

        // Mensaje blanco
        TextView message = dialog.findViewById(android.R.id.message);
        if (message != null)
            message.setTextColor(getResources().getColor(R.color.white));
    }

    public void cerrarsesion() {
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
        // Obtener datos desde la API usando el userId guardado en SharedPreferences
        usuarioRepo.getUserFromApi(usuarioActualId, (usuarioActual, error) -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (usuarioActual != null) {
                        // Usuario encontrado, ahora actualizamos la interfaz
                        String textoFecha = usuarioActual.fechaRegistro != null
                                && !usuarioActual.fechaRegistro.isEmpty()
                                        ? usuarioActual.fechaRegistro
                                        : "Fecha no disponible";
                        fechadesde.setText(textoFecha);
                        nombre.setText(usuarioActual.nombre);
                        apellido.setText(usuarioActual.apellido);
                        email.setText(usuarioActual.email);
                        password.setText(usuarioActual.password);
                    } else {
                        // Si hay error al obtener el usuario
                        fechadesde.setText("Error al cargar datos");
                        Toast.makeText(getContext(), error != null ? error : "Error desconocido", Toast.LENGTH_SHORT)
                                .show();
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
        // Obtenemos los valores de los campos
        String nuevoNombre = nombre.getText().toString().trim();
        String nuevoApellido = apellido.getText().toString().trim();
        String nuevoEmail = email.getText().toString().trim();
        String nuevaPassword = password.getText().toString();

        // Validaciones básicas
        if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevoEmail.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si la password es la máscara, enviamos null o string vacío
        if (nuevaPassword.equals("••••••••")) {
            nuevaPassword = ""; // O puedes manejarlo diferente según tu API
        }

        // Actualizar usuario en la API
        usuarioRepo.updateUserInApi(usuarioActualId, nuevoNombre, nuevoApellido, nuevoEmail, nuevaPassword,
                (usuarioActualizado, error) -> {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (usuarioActualizado != null) {
                                restaurarEstadoVisual();
                                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT)
                                        .show();
                                // Recargar los datos desde la API
                                colocarDatosUsuario();
                            } else {
                                Toast.makeText(getContext(), error != null ? error : "Error al actualizar datos",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
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
            // mostrar contraseña
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