package com.example.barberiaglp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PerfilFragment extends Fragment {
    public PerfilFragment(){}

    Button logOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1 Inflás la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // 2 Obtenés el botón desde la vista inflada
        logOut = view.findViewById(R.id.btnLogOut);

        // 3 Le asignás el listener
        logOut.setOnClickListener(v -> cerrarsesion());

        // 4 Retornás la vista inflada
        return view;
    }
    public void cerrarsesion(){
        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("userEmail");
        editor.remove("userName");
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}