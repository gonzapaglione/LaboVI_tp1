package com.example.barberiaglp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import Modelos.Turno;

public class TurnosFragment extends Fragment {

    private TextView tvProximos, tvPasados;
    private View lineaProximos, lineaPasados;
    private RecyclerView rvProximos, rvPasados;
    private TurnoAdapter adapterProximos, adapterPasados;
    private FloatingActionButton fabAgregar;
    private LinearLayout btnProximos, btnPasados;

    public TurnosFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turnos, container, false);

        tvProximos = view.findViewById(R.id.tvProximos);
        tvPasados = view.findViewById(R.id.tvPasados);
        lineaProximos = view.findViewById(R.id.lineaProximos);
        lineaPasados = view.findViewById(R.id.lineaPasados);
        rvProximos = view.findViewById(R.id.rvTurnosProximos);
        rvPasados = view.findViewById(R.id.rvTurnosPasados);
        fabAgregar = view.findViewById(R.id.fabAgregarTurno);
        btnProximos = view.findViewById(R.id.btnProximos);
        btnPasados = view.findViewById(R.id.btnPasados);

        rvProximos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPasados.setLayoutManager(new LinearLayoutManager(getContext()));

        // Datos de prueba
        List<Turno> proximos = new ArrayList<>();
        proximos.add(crearTurnoEjemplo("2025-10-20", "10:00", "pendiente"));
        proximos.add(crearTurnoEjemplo("2025-10-22", "12:00", "pendiente"));

        List<Turno> pasados = new ArrayList<>();
        pasados.add(crearTurnoEjemplo("2025-10-01", "09:00", "atendido"));

        adapterProximos = new TurnoAdapter(proximos);
        adapterPasados = new TurnoAdapter(pasados);

        rvProximos.setAdapter(adapterProximos);
        rvPasados.setAdapter(adapterPasados);

        // Cambios entre secciones
        btnProximos.setOnClickListener(v -> mostrarProximos());
        btnPasados.setOnClickListener(v -> mostrarPasados());

        // Acción del botón flotante
        fabAgregar.setOnClickListener(v ->
                Toast.makeText(getContext(), "Agregar nuevo turno", Toast.LENGTH_SHORT).show()
        );

        return view;
    }

    private Turno crearTurnoEjemplo(String fecha, String hora, String estado) {
        Turno t = new Turno();
        t.fecha = fecha;
        t.horaInicio = hora;
        t.estado = estado;
        return t;
    }

    private void mostrarProximos() {
        rvProximos.setVisibility(View.VISIBLE);
        rvPasados.setVisibility(View.GONE);

        tvProximos.setTextColor(getResources().getColor(R.color.white));
        tvPasados.setTextColor(getResources().getColor(R.color.gray));

        lineaProximos.setVisibility(View.VISIBLE);
        lineaPasados.setVisibility(View.GONE);
    }

    private void mostrarPasados() {
        rvProximos.setVisibility(View.GONE);
        rvPasados.setVisibility(View.VISIBLE);

        tvProximos.setTextColor(getResources().getColor(R.color.gray));
        tvPasados.setTextColor(getResources().getColor(R.color.white));

        lineaProximos.setVisibility(View.GONE);
        lineaPasados.setVisibility(View.VISIBLE);
    }
}
