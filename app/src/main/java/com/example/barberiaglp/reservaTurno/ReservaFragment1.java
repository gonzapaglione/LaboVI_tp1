package com.example.barberiaglp.reservaTurno;

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

import com.example.barberiaglp.Modelos.Servicio;

public class ReservaFragment1 extends Fragment {

    private ReservaViewModel viewModel;
    private Servicio servicioSeleccionadoLocalmente;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_1, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);

        RecyclerView recycler = view.findViewById(R.id.recyclerServicios);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Button btnNext = view.findViewById(R.id.btnNext1);
        View btnBack = view.findViewById(R.id.btnBack3);

        if (btnBack != null)
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        btnNext.setOnClickListener(v -> {
            if (viewModel.getTodosLosServicios() == null || viewModel.getTodosLosServicios().getValue() == null) {
                Toast.makeText(getContext(), "Seleccione un servicio antes de continuar", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.setServicio(servicioSeleccionadoLocalmente);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.reserva_fragment_container, new ReservaFragment2())
                    .addToBackStack(null)
                    .commit();
        });

        viewModel.getTodosLosServicios().observe(getViewLifecycleOwner(), servicios -> {
            // Cuando la lista de servicios llega, se la pasamos al adapter.
            ReservaAdapter adapter = new ReservaAdapter(servicios, R.layout.item_reserva_1);

            adapter.setOnItemClickListener(servicio -> {
                servicioSeleccionadoLocalmente = servicio;
            });
            recycler.setAdapter(adapter);

        });


        return view;
    }
}
