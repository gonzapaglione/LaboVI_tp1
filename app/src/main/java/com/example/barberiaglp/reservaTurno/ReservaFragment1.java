package com.example.barberiaglp.reservaTurno;

import android.os.Bundle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberiaglp.R;

import java.util.List;

import BD.AppDatabase;
import Modelos.Servicio;

public class ReservaFragment1 extends Fragment {

    private ReservaViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_1, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);

        RecyclerView recycler = view.findViewById(R.id.recyclerServicios);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Button btnNext = view.findViewById(R.id.btnNext1);

        // Back button
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // Cargar servicios desde la BD
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Servicio> servicios = AppDatabase.getInstance(requireContext()).servicioDao().obtenerTodos();
            requireActivity().runOnUiThread(() -> {
                ReservaAdapter adapter = new ReservaAdapter(servicios, R.layout.item_reserva_1);
                adapter.setOnItemClickListener(s -> viewModel.setServicio(s.nombre));
                recycler.setAdapter(adapter);
            });
        });

        btnNext.setOnClickListener(v -> {
            if (viewModel.getServicio() == null || viewModel.getServicio().isEmpty()) {
                Toast.makeText(getContext(), "Seleccione un servicio antes de continuar", Toast.LENGTH_SHORT).show();
                return;
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.reserva_fragment_container, new ReservaFragment2())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
