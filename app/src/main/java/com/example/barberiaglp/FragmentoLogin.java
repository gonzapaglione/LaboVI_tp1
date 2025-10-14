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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fragmento_login, container, false);

        etEmail = view.findViewById(R.id.inputMail);
        etPassword = view.findViewById(R.id.inputPassword);

        btnLoginn = view.findViewById(R.id.btnLogin);

        Application application = requireActivity().getApplication();
        usuarioRepo = new UsuarioRepositorio(application);

        btnLoginn.setOnClickListener(v -> verificarLogin());
        return view;
    }

    private void verificarLogin(){
      String  emailIngresado = etEmail.getText().toString();
      String passwordIngresada = etPassword.getText().toString();

      // verifica que se ingresen los datos
      if(emailIngresado.isEmpty() || passwordIngresada.isEmpty()){
          Toast.makeText(getContext(), "Se deben completar los campos", Toast.LENGTH_SHORT).show();
          return;
      }

        usuarioRepo.login(emailIngresado, passwordIngresada, usuario -> {
            // ESTE CÓDIGO SE EJECUTA CUANDO EL REPOSITORIO DEVUELVE UNA RESPUESTA
            // Es crucial asegurarse de que la actividad/fragmento todavía existe
            // y ejecutar cambios de UI en el hilo principal.
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(() -> {
                if (usuario != null) {
                    // Login Exitoso
                    Toast.makeText(getContext(), "Bienvenida " + usuario.nombre, Toast.LENGTH_SHORT).show();

                    // Guardar la sesión
                    SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("userEmail", usuario.email); // Guarda el email real de la BD
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    // Redirigir a MainActivity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    // Login Fallido
                    Toast.makeText(getContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    }

