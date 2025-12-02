package com.example.barberiaglp.LoginAndRegister;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.barberiaglp.R;
import com.example.barberiaglp.Repositorios.UsuarioRepositorio;
import com.example.barberiaglp.SeccionesPrincipales.MainActivity;

public class FragmentoRegistrar extends Fragment {

    private EditText etNombre, etEmail, etPassword, etApellido, etPassword2;
    private Button btnRegistrar;

    private UsuarioRepositorio usuarioRepo;
    private static final int MIN_PASSWORD_LENGTH = 8;

    public FragmentoRegistrar() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmento_registrar, container, false);

        etNombre = view.findViewById(R.id.inputNombre);
        etApellido = view.findViewById(R.id.inputApellido);
        etEmail = view.findViewById(R.id.inputMail);
        etPassword = view.findViewById(R.id.inputPassword);
        etPassword2 = view.findViewById(R.id.inputPassword2);

        // Inicializar el repositorio
        Application application = requireActivity().getApplication();
        usuarioRepo = new UsuarioRepositorio(application);

        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> validarYGuardarDatos());
        return view;
    }

    private void validarYGuardarDatos() {

        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String password2 = etPassword2.getText().toString();

        // 1. Verificar campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Verifica que tenga formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Por favor ingresa un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Verificar largo de contraseña
        if (password.length() < MIN_PASSWORD_LENGTH) {
            Toast.makeText(getContext(), "La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Verificar claves iguales
        if (!password.equals(password2)) {
            Toast.makeText(getContext(), "Las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamar a la API para registrar el usuario
        Log.d("FragmentoRegistrar", "Iniciando registro desde UI...");
        usuarioRepo.register(nombre, apellido, email, password, (usuario, error) -> {
            // Verificamos que el fragmento siga "vivo" antes de actuar
            if (getActivity() == null) {
                Log.e("FragmentoRegistrar", "Activity es null, no se puede actualizar UI");
                return;
            }
            // Volvemos al hilo principal para actualizar la UI
            getActivity().runOnUiThread(() -> {
                if (usuario != null) {
                    // Registro exitoso - Guardar preferencias y navegar a MainActivity
                    Log.d("FragmentoRegistrar", "Registro exitoso! Usuario: " + usuario.nombre);
                    Log.d("FragmentoRegistrar", "ID local: " + usuario.id + ", API ID: " + usuario.apiId);
                    Toast.makeText(getContext(), "Bienvenido " + usuario.nombre, Toast.LENGTH_SHORT).show();

                    // Guardar sesión con ambos IDs (local y API)
                    int apiId = (usuario.apiId != null) ? usuario.apiId : usuario.id;
                    guardarPreferencias(usuario.email, password, usuario.id, apiId);

                    // Navegar a MainActivity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    // Error en el registro - Mostrar en AlertDialog para que no desaparezca
                    Log.e("FragmentoRegistrar", "Error en registro: " + error);
                    String mensajeError = error != null ? error : "Error desconocido al registrar usuario";

                    new AlertDialog.Builder(getContext())
                            .setTitle("Error en el Registro")
                            .setMessage(mensajeError)
                            .setPositiveButton("Entendido", null)
                            .show();
                }
            });
        });
    }

    private void guardarPreferencias(String email, String password, int localId, int apiId) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Guardar sesión activa
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userEmail", email);
        editor.putInt("userLocalId", localId); // ID local de la BD
        editor.putInt("userApiId", apiId); // ID de la API
        editor.putBoolean("rememberMe", false); // No recordar contraseña en registro automático
        editor.apply();
    }

    private void limpiarInputs() {
        // limpiar inputs
        etNombre.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etApellido.setText("");
        etPassword2.setText("");
    }
}