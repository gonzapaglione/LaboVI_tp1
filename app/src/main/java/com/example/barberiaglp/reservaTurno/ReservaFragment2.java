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

import com.example.barberiaglp.R;

public class ReservaFragment2 extends Fragment {
    private ReservaViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_barbero, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);

        // preparar RecyclerView
        androidx.recyclerview.widget.RecyclerView recycler = view.findViewById(R.id.recyclerServicios);
        recycler.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));


        // OBSERVAR LA LISTA FILTRADA DE BARBEROS
        viewModel.getBarberosFiltrados().observe(getViewLifecycleOwner(), barberos -> {
            ReservaBarberoAdapter adapter = new ReservaBarberoAdapter(barberos, R.layout.item_reserva_2);
            adapter.setOnItemClickListener(barbero -> viewModel.setBarbero(barbero));
            recycler.setAdapter(adapter);
        });

        Button btnNext = view.findViewById(R.id.btnNext2);
        btnNext.setOnClickListener(v -> {
            if (viewModel.getBarberoSeleccionado().getValue() == null) {
                Toast.makeText(getContext(), "Seleccione un barbero para continuar", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.prepararFechasParaBarberoSeleccionado();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.reserva_fragment_container, new ReservaFragment3())
                    .addToBackStack(null)
                    .commit();
        });

        View btnBack = view.findViewById(R.id.btnBack3);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}
