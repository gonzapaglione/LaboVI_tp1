package com.example.barberiaglp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import Repositorios.UsuarioRepositorio;

public class FragmentoLogin extends Fragment {
    public FragmentoLogin(){}

    public EditText etEmail, etPassword;
    public Button btnLoginn;
    private UsuarioRepositorio usuarioRepo;

    private CheckBox checkboxRemember;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login, container, false);

            etEmail = view.findViewById(R.id.inputMail);
            etPassword = view.findViewById(R.id.inputPassword);
            btnLoginn = view.findViewById(R.id.btnLogin);
            checkboxRemember = view.findViewById(R.id.checkboxRemember);

            usuarioRepo = new UsuarioRepositorio(requireActivity().getApplication());

            // Esto es útil para rellenar los campos si ya están guardados
            cargarPreferencias();

            btnLoginn.setOnClickListener(v -> verificarLogin());
            return view;
        }

        private void verificarLogin() {
            String emailIngresado = etEmail.getText().toString();
            String passwordIngresada = etPassword.getText().toString();

            if (emailIngresado.isEmpty() || passwordIngresada.isEmpty()) {
                Toast.makeText(getContext(), "Se deben completar los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            usuarioRepo.login(emailIngresado, passwordIngresada, usuario -> {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(() -> {
                    if (usuario != null) {
                        Toast.makeText(getContext(), "Bienvenida " + usuario.nombre, Toast.LENGTH_SHORT).show();

                        guardarPreferencias(usuario.email, passwordIngresada);

                        // Redirigir a MainActivity
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();

                    } else {
                        // Login Fallido
                        Toast.makeText(getContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        private void guardarPreferencias(String email, String password) {
            SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            // Siempre guardamos que la sesión está iniciada
            editor.putBoolean("isLoggedIn", true);
            editor.putString("userEmail", email);

            // Si el checkbox está marcado, guardamos las credenciales
            if (checkboxRemember.isChecked()) {
                editor.putString("userEmail", email);
                editor.putString("userPassword", password); // ¡Recuerda la advertencia de seguridad!
                editor.putBoolean("rememberMe", true);
            } else {
                // Si no está marcado, nos aseguramos de borrar cualquier credencial antigua
                editor.remove("userPassword");
                editor.putBoolean("rememberMe", false);
            }
            editor.apply();
        }

        private void cargarPreferencias() {
            SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            boolean rememberMe = preferences.getBoolean("rememberMe", false);

            if (rememberMe) {
                String email = preferences.getString("userEmail", "");
                String password = preferences.getString("userPassword", "");
                etEmail.setText(email);
                etPassword.setText(password);
                checkboxRemember.setChecked(true);
            }
        }
    }