package com.example.barberiaglp.SeccionesPrincipales.SobreNosotros;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.example.barberiaglp.BD.AppDatabase;
import com.example.barberiaglp.Modelos.Servicio;
import com.example.barberiaglp.R;

public class SobreNosotrosFragment extends Fragment {

    private RecyclerView recyclerSobreNosotros;
    private SobreNosotrosAdapter adapter;

    public SobreNosotrosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_servicios, container, false);

        recyclerSobreNosotros = view.findViewById(R.id.recyclerServicios);
        recyclerSobreNosotros.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cargar servicios desde la base de datos en un hilo separado
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Servicio> servicios = AppDatabase.getInstance(requireContext())
                    .servicioDao().obtenerTodos();

            requireActivity().runOnUiThread(() -> {
                adapter = new SobreNosotrosAdapter(servicios);
                recyclerSobreNosotros.setAdapter(adapter);
            });
        });

        return view;
    }
}
