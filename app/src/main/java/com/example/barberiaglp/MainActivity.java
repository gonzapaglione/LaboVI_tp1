package com.example.barberiaglp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView btnLoginTab, btnRegisterTab;
    boolean isLoginSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLoginTab = findViewById(R.id.btnLoginTab);
        btnRegisterTab = findViewById(R.id.btnRegisterTab);

        // Estado inicial: Login seleccionado
        actualizarEstilo();

        btnLoginTab.setOnClickListener(v -> {
            if (!isLoginSelected) {
                isLoginSelected = true;
                actualizarEstilo();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnRegisterTab.setOnClickListener(v -> {
            if (isLoginSelected) {
                isLoginSelected = false;
                actualizarEstilo();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void actualizarEstilo() {
        if (isLoginSelected) {
            btnLoginTab.setBackgroundResource(R.drawable.tab_left_selected);
            btnRegisterTab.setBackgroundResource(R.drawable.tab_right_unselected);
        } else {
            btnLoginTab.setBackgroundResource(R.drawable.tab_left_unselected);
            btnRegisterTab.setBackgroundResource(R.drawable.tab_right_selected);
        }
    }
    }
