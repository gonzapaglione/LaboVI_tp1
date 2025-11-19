package com.example.barberiaglp.SeccionesPrincipales.SobreNosotros;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SobreNosotrosFragment extends Fragment implements OnMapReadyCallback {

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

        View btnAbrirMapa = view.findViewById(R.id.btn_abrir_mapa);
        btnAbrirMapa.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivity(intent);
        });

        // 2. Inicializar el mapa pequeño
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapa_pequeno);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Configuración visual del mapa pequeño
        LatLng ubicacionBarberia = new LatLng(-27.801231,-64.250597); //Ubicación UNSE

        googleMap.addMarker(new MarkerOptions().position(ubicacionBarberia).title("Barbería GLP"));
        // zoom para vista previa
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionBarberia, 15f));

        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }
}
