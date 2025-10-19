package com.example.barberiaglp.reservaTurno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        View view = inflater.inflate(R.layout.fragment_reserva_2, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);

        // preparar RecyclerView
        androidx.recyclerview.widget.RecyclerView recycler = view.findViewById(R.id.recyclerServicios);
        recycler.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));

        // Cargar barberos desde la BD y setear adapter con item_reserva_2
        BD.AppDatabase.databaseWriteExecutor.execute(() -> {
            java.util.List<Modelos.Usuario> barberos = BD.AppDatabase.getInstance(requireContext()).usuarioDao()
                    .obtenerPorRol("Barbero");
            requireActivity().runOnUiThread(() -> {
                ReservaBarberoAdapter adapter = new ReservaBarberoAdapter(barberos, R.layout.item_reserva_2);
                adapter.setOnItemClickListener(u -> {
                    // Guardar nombre completo en el ViewModel como peluquero seleccionado
                    String nombreCompleto = (u.nombre != null ? u.nombre : "") + " "
                            + (u.apellido != null ? u.apellido : "");
                    viewModel.setNombreCliente(nombreCompleto.trim());
                });
                recycler.setAdapter(adapter);
            });
        });

        Button btnNext = view.findViewById(R.id.btnNext2);
        btnNext.setOnClickListener(v -> {
            // si quieres tomar la selección real, se puede expandir el adapter para manejar
            // clicks
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.reserva_fragment_container, new ReservaFragment3())
                    .addToBackStack(null)
                    .commit();
        });

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}
