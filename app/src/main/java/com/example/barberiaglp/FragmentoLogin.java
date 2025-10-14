package com.example.barberiaglp;
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

public class FragmentoLogin extends Fragment {
    public FragmentoLogin(){}

    public EditText etEmail, etPassword;
    public Button btnLoginn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = view.findViewById(R.id.inputMail);
        etPassword = view.findViewById(R.id.inputPassword);

        btnLoginn = view.findViewById(R.id.btnLogin);

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
        // Recupera los datos guardados en SharedPreferences
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        String emailGuardado = preferences.getString("email", null);
        String passwordGuardada = preferences.getString("password", null);
        String nombreGuardado = preferences.getString("nombre", "Usuario");

        // Verifica si existen datos guardados
        if (emailGuardado == null || passwordGuardada == null) {
            Toast.makeText(getContext(), "No hay usuarios registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        // Compara con lo que ingresó el usuario
        if (emailIngresado.equals(emailGuardado) && passwordIngresada.equals(passwordGuardada)) {
            Toast.makeText(getContext(), "¡Bienvenido, " + nombreGuardado + " 👋!", Toast.LENGTH_LONG).show();
            //derivar a MainActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();

        } else {
            Toast.makeText(getContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }

    }
}
