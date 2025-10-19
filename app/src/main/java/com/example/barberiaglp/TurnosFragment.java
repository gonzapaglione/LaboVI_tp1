package com.example.barberiaglp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import BD.AppDatabase;
import Daos.TurnoDao;
import Modelos.Turno;
import Modelos.TurnoConDetalles;
import Repositorios.TurnosRepositorio;

public class TurnosFragment extends Fragment {

    private TextView tvProximos, tvPasados;
    private View lineaProximos, lineaPasados;
    private RecyclerView rvProximos, rvPasados;
    private TurnoAdapter adapterProximos, adapterPasados;
    private FloatingActionButton fabAgregar;
    private LinearLayout btnProximos, btnPasados;
    private TurnoDao turnoDao;

    private TurnosRepositorio turnoRepo;
    private int usuarioActualId = -1;

    public TurnosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turnos, container, false);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        turnoDao = db.turnoDao();
        turnoRepo = new TurnosRepositorio(requireActivity().getApplication());

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

        // se crean adaptadpres con listas vacías
        adapterProximos = new TurnoAdapter(new ArrayList<>());
        adapterPasados = new TurnoAdapter(new ArrayList<>());

        rvProximos.setAdapter(adapterProximos);
        rvPasados.setAdapter(adapterPasados);

        obtenerIdUsuarioYcargarTurnos();

        // Cambios entre secciones
        btnProximos.setOnClickListener(v -> mostrarProximos());
        btnPasados.setOnClickListener(v -> mostrarPasados());

        // Acción del botón flotante: abrir ReservasActivity
        fabAgregar.setOnClickListener(v -> {
            // iniciar la activity de reservas
            startActivity(new android.content.Intent(requireContext(),
                    com.example.barberiaglp.reservaTurno.ReservasActivity.class));
        });

        return view;
    }

    private void obtenerIdUsuarioYcargarTurnos() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        usuarioActualId = preferences.getInt("userId", -1);

        if (usuarioActualId != -1) {
            cargarTurnosDeLaBD();
        } else {
            Toast.makeText(getContext(), "Error: No se pudo identificar al usuario.", Toast.LENGTH_LONG).show();

        }
    }

    private void cargarTurnosDeLaBD() {
        new Thread(() -> {
            // 1. Obtiene los turnos del usuario desde la BD (fuera del hilo principal)
            List<TurnoConDetalles> todosLosTurnos = turnoRepo.getTurnosConDetalles(usuarioActualId);

            // 2. Prepara las listas para filtrar y la fecha actual para comparar
            List<TurnoConDetalles> turnosProximos = new ArrayList<>();
            List<TurnoConDetalles> turnosPasados = new ArrayList<>();
            Date fechaActual = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // 3. Filtra cada turno
            for (TurnoConDetalles turnodetalle : todosLosTurnos) {
                try {
                    java.util.Date fechaTurno = sdf.parse(turnodetalle.turno.fecha);

                    // Comparamos la fecha del turno con la fecha actual.
                    if (fechaActual.before(fechaTurno) || sdf.format(fechaActual).equals(sdf.format(fechaTurno))) {
                        turnosProximos.add(turnodetalle);
                    } else {
                        turnosPasados.add(turnodetalle);
                    }
                } catch (Exception e) {
                    // Manejo de error si el formato de la fecha en la BD es incorrecto
                    e.printStackTrace();
                }
            }

            // 4. Actualiza los adaptadores en el hilo principal
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapterProximos.actualizarTurnos(turnosProximos);
                    adapterPasados.actualizarTurnos(turnosPasados);
                });
            }
        }).start();
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
