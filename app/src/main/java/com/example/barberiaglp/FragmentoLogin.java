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

import BD.AppDatabase;
import Modelos.Usuario;

public class FragmentoLogin extends Fragment {
    public FragmentoLogin(){}

    public EditText etEmail, etPassword;
    public Button btnLoginn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fragmento_login, container, false);

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

        AppDatabase db = AppDatabase.getInstance(getContext());
        Usuario usuario = db.usuarioDao().login(emailIngresado, passwordIngresada);

        if (usuario != null) {
            Toast.makeText(getContext(), "Bienvenida " + usuario.nombre, Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = requireActivity()
                    .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", emailIngresado);
            editor.putString("password", passwordIngresada);
            editor.putBoolean("isLoggedIn", true); // Guardamos que la sesión está iniciada
            editor.apply();
            startActivity(new Intent(getActivity(), MainActivity.class));
        } else {
            Toast.makeText(getContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }

    }
}
