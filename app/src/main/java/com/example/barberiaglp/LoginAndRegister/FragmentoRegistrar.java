package com.example.barberiaglp.LoginAndRegister;

import android.app.Application;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.barberiaglp.R;
import com.example.barberiaglp.Repositorios.UsuarioRepositorio;

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
        usuarioRepo.register(nombre, apellido, email, password, (usuario, error) -> {
            // Verificamos que el fragmento siga "vivo" antes de actuar
            if (getActivity() == null) {
                return;
            }
            // Volvemos al hilo principal para actualizar la UI (mostrar Toasts)
            getActivity().runOnUiThread(() -> {
                if (usuario != null) {
                    // Registro exitoso
                    Toast.makeText(getContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarInputs();
                } else {
                    // Error en el registro
                    Toast.makeText(getContext(), error != null ? error : "Error al registrar usuario",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
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