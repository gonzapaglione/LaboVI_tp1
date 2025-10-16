package com.example.barberiaglp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import BD.AppDatabase;
import Modelos.Servicio;

public class ServiciosFragment extends Fragment {

    private RecyclerView recyclerServicios;
    private ServicioAdapter adapter;

    public ServiciosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_servicios, container, false);

        recyclerServicios = view.findViewById(R.id.recyclerServicios);
        recyclerServicios.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cargar servicios desde la base de datos en un hilo separado
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Servicio> servicios = AppDatabase.getInstance(requireContext())
                    .servicioDao().obtenerTodos();

            requireActivity().runOnUiThread(() -> {
                adapter = new ServicioAdapter(servicios);
                recyclerServicios.setAdapter(adapter);
            });
        });

        return view;
    }
}
