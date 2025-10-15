package com.example.barberiaglp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ServiciosFragment extends Fragment {

    public ServiciosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servicios, container, false);

        // Servicio 1: Corte
        View servicioCorte = view.findViewById(R.id.servicioCorte);
        ImageView imgCorte = servicioCorte.findViewById(R.id.imgServicio);
        TextView tvCorteNombre = servicioCorte.findViewById(R.id.tvServicioTitulo);
        TextView tvCorteDesc = servicioCorte.findViewById(R.id.tvServicioDescripcion);
        TextView tvCortePrecio = servicioCorte.findViewById(R.id.tvServicioPrecio);

        imgCorte.setImageResource(R.drawable.fotocorte);
        tvCorteNombre.setText("Corte de cabello");
        tvCorteDesc.setText("Cortes clásicos y modernos adaptados a tu estilo.");
        tvCortePrecio.setText("$7000");


        // Servicio 2: Barba
        View servicioBarba = view.findViewById(R.id.servicioBarba);
        ImageView imgBarba = servicioBarba.findViewById(R.id.imgServicio);
        TextView tvBarbaNombre = servicioBarba.findViewById(R.id.tvServicioTitulo);
        TextView tvBarbaDesc = servicioBarba.findViewById(R.id.tvServicioDescripcion);
        TextView tvBarbaPrecio = servicioBarba.findViewById(R.id.tvServicioPrecio);

        imgBarba.setImageResource(R.drawable.fotobarba);
        tvBarbaNombre.setText("Perfilado de barba");
        tvBarbaDesc.setText("Definí tu estilo con un perfilado preciso y prolijo.");
        tvBarbaPrecio.setText("$9000");

        // Servicio 3: Premium
        View servicioPremium = view.findViewById(R.id.servicioPremium);
        ImageView imgPremium = servicioPremium.findViewById(R.id.imgServicio);
        TextView tvPremiumNombre = servicioPremium.findViewById(R.id.tvServicioTitulo);
        TextView tvPremiumDesc = servicioPremium.findViewById(R.id.tvServicioDescripcion);
        TextView tvPremiumPrecio = servicioPremium.findViewById(R.id.tvServicioPrecio);

        imgPremium.setImageResource(R.drawable.fotocortebarba);
        tvPremiumNombre.setText("Corte + Barba");
        tvPremiumDesc.setText("Experiencia completa con un corte moderno a tijera y/o maquina + perfilado de barba.");
        tvPremiumPrecio.setText("$11000");
        return view;
    }
}
