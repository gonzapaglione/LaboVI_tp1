package com.example.barberiaglp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import BD.AppDatabase;
import Daos.UsuarioDao;
import Modelos.Usuario;

public class FragmentoRegistrar extends Fragment {

    private EditText etNombre, etEmail, etPassword, etApellido, etPassword2;
    private Button btnRegistrar;

    public FragmentoRegistrar(){}

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

        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(v -> guardarDatos());
        return view;
    }

    private void guardarDatos(){
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String password2 = etPassword2.getText().toString();

        //verificar campos
        if (nombre.isEmpty() || apellido.isEmpty() ||email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // verifica que tenga formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Por favor ingresa un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }

        //verificar claves iguales
        if(!password.equals(password2)){
            Toast.makeText(getContext(), "Las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
            return;
        }

        //guardar datos en la base de datos
        AppDatabase db = AppDatabase.getInstance(getContext());
        UsuarioDao userDao = db.usuarioDao();

        Usuario nuevo = new Usuario();
        nuevo.nombre = nombre;
        nuevo.apellido = apellido;
        nuevo.email = email;
        nuevo.password = password;
        userDao.insert(nuevo);
        Toast.makeText(getContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

        //limpiar inputs
        etNombre.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etApellido.setText("");
        etPassword2.setText("");


    }
}