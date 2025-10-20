package com.example.barberiaglp.reservaTurno;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.barberiaglp.R;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import BD.AppDatabase;
import Modelos.Servicio;
import Modelos.Turno;
import Modelos.Usuario;

public class ReservaFragment4 extends Fragment {
    private ReservaViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_4, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);


        TextView tvServicio = view.findViewById(R.id.tvDetalleServicio);
        TextView tvBarbero = view.findViewById(R.id.tvDetallePeluquero);
        TextView tvFechaHora = view.findViewById(R.id.tvDetalleFecha);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        // Rellenar con datos del ViewModel
        tvServicio.setText(viewModel.getServicioSeleccionado().getValue().nombre);
        tvBarbero.setText(viewModel.getBarberoSeleccionado().getValue().nombre + " "+ viewModel.getBarberoSeleccionado().getValue().apellido);
        String fechaHora = viewModel.getFechaSeleccionada().getValue() + " a las " + viewModel.getHoraSeleccionada().getValue();
        tvFechaHora.setText(fechaHora);

        btnConfirm.setOnClickListener(v -> {
            crearTurno();
            Toast.makeText(getContext(), "Turno reservado con éxito", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        });

        View btnBack = view.findViewById(R.id.btnBack3);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }

    public void crearTurno() {
        Usuario barbero = viewModel.getBarberoSeleccionado().getValue();
        Servicio servicio = viewModel.getServicioSeleccionado().getValue();
        String fecha = viewModel.getFechaSeleccionada().getValue();
        String horaInicioStr = viewModel.getHoraSeleccionada().getValue(); // Hora elegida por el usuario

        if (barbero == null || servicio == null || fecha == null || horaInicioStr == null) {
            Log.e("ReservaViewModel", "No se puede crear el turno. Faltan datos.");
            return;
        }

        // Calcular hora de fin según duración del servicio
        int duracion = servicio.duracionMin;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime inicio = LocalTime.parse(horaInicioStr, fmt);
        LocalTime fin = inicio.plusMinutes(duracion);
        String horaFinStr = fin.format(fmt);

        // Crear objeto Turno
        Turno turno = new Turno();
        turno.clienteId = viewModel.getClienteId();
        turno.barberoId = barbero.id;
        turno.servicioId = servicio.id;
        turno.fecha = fecha;
        turno.horaInicio = horaInicioStr;
        turno.horaFin = horaFinStr;
        turno.estado = "Pendiente";


        // Guardar en base de datos en background
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getInstance(requireContext()).turnoDao().insertar(turno);
            Log.d("ReservaViewModel", "Turno guardado: " + turno.fecha + " " + turno.horaInicio);
            Log.d("ReservaViewModel", "Turno guardado: " + turno.clienteId);
        });
    }

}
