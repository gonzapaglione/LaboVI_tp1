package com.example.barberiaglp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginActivity extends AppCompatActivity {

    private TextView btnLogin, btnRegister;
    private boolean showingLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chequearSesion();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity); // Asegúrate de que el nombre sea correcto

        // Asegurate que estos IDs coincidan EXACTAMENTE con los del XML
        btnLogin = findViewById(R.id.btnLoginTab);
        btnRegister = findViewById(R.id.btnRegistrar);

        // Cargar el fragmento inicial (Login)
        replaceFragment(new FragmentoLogin());
        highlightLoginTab(); // para marcar visualmente el tab activo

        // Eventos de click
        btnLogin.setOnClickListener(v -> showLogin());
        btnRegister.setOnClickListener(v -> showRegister());
    }

    private void chequearSesion() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        // Lee el valor booleano de "isLoggedIn". Si no existe, devuelve 'false' por defecto.
        boolean recordar = preferences.getBoolean("rememberMe", false);
        if(recordar){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void showLogin() {
        if (!showingLogin) {
            replaceFragment(new FragmentoLogin());
            highlightLoginTab();
            showingLogin = true;
        }
    }

    private void showRegister() {
        if (showingLogin) {
            replaceFragment(new FragmentoRegistrar());
            highlightRegisterTab();
            showingLogin = false;
        }
    }

    private void highlightLoginTab() {
        btnLogin.setBackgroundResource(R.drawable.tab_left_selected);
        btnRegister.setBackgroundResource(R.drawable.tab_right_unselected);
    }

    private void highlightRegisterTab() {
        btnLogin.setBackgroundResource(R.drawable.tab_left_unselected);
        btnRegister.setBackgroundResource(R.drawable.tab_right_selected);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
