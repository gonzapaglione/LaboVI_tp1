package com.example.barberiaglp;

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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import Modelos.Usuario;
import Repositorios.UsuarioRepositorio;

public class FragmentoRegistrar extends Fragment {

    private EditText etNombre, etEmail, etPassword, etApellido, etPassword2;
    private Button btnRegistrar;

    private UsuarioRepositorio usuarioRepo;

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

        //Inicializar el repositorio
        Application application = requireActivity().getApplication();
        usuarioRepo = new UsuarioRepositorio(application);

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

        Usuario nuevo = new Usuario();
        nuevo.nombre = nombre;
        nuevo.apellido = apellido;
        nuevo.email = email;
        nuevo.password = password;
        nuevo.rol = "Cliente";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            java.time.LocalDate hoy = java.time.LocalDate.now(); // obtiene fecha de hoy
            // 1. Extrae las partes de la fecha
            int dia = hoy.getDayOfMonth();
            String mes = hoy.getMonth().getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "ES"));
            int anio = hoy.getYear();

            // 2. Construye el String final
            String mesCapitalizado = mes.substring(0, 1).toUpperCase() + mes.substring(1);
            nuevo.fechaRegistro = dia + " de " + mesCapitalizado + ", " + anio;

        } else {
            // 1. Define el formato de salida
            SimpleDateFormat sdf = new SimpleDateFormat("d 'de' MMMM, yyyy", new Locale("es", "ES"));
            // 2. Formatea la fecha de hoy y la guarda
            nuevo.fechaRegistro = sdf.format(new java.util.Date());
        }
        usuarioRepo.insert(nuevo);
        Toast.makeText(getContext(), "Usuario registrado correctamente: "+ nuevo.fechaRegistro, Toast.LENGTH_SHORT).show();

        //limpiar inputs
        etNombre.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etApellido.setText("");
        etPassword2.setText("");


    }
}