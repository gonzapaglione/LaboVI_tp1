package com.example.barberiaglp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private TextView btnLogin, btnRegister;
    private boolean showingLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLoginTab);
        btnRegister = findViewById(R.id.btnRegisterTab);

        // Cargar el fragmento inicial (Login)
        replaceFragment(new FragmentoLogin());

        btnLogin.setOnClickListener(v -> showLogin());
        btnRegister.setOnClickListener(v -> showRegister());
    }

    private void showLogin() {
        if (!showingLogin) {
            replaceFragment(new FragmentoLogin());
            btnLogin.setBackgroundResource(R.drawable.tab_left_selected);
            btnRegister.setBackgroundResource(R.drawable.tab_right_unselected);
            showingLogin = true;
        }
    }

    private void showRegister() {
        if (showingLogin) {
            replaceFragment(new FragmentoRegistrar());
            btnRegister.setBackgroundResource(R.drawable.tab_right_selected);
            btnLogin.setBackgroundResource(R.drawable.tab_left_unselected);
            showingLogin = false;
        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
