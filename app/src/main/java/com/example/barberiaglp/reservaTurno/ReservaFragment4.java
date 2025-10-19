package com.example.barberiaglp.reservaTurno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.barberiaglp.R;
import java.util.Locale;
import java.text.DateFormatSymbols;

public class ReservaFragment4 extends Fragment {
    private ReservaViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_4, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);

        TextView tvDetalleServicio = view.findViewById(R.id.tvDetalleServicio);
        TextView tvDetallePeluquero = view.findViewById(R.id.tvDetallePeluquero);
        TextView tvDetalleFecha = view.findViewById(R.id.tvDetalleFecha);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        // Rellenar con datos del ViewModel
        tvDetalleServicio.setText(viewModel.getServicio() != null ? viewModel.getServicio() : "-");
        // Peluquero: si no tienes selección real, mostrar un placeholder o nombre
        // guardado en ViewModel
        tvDetallePeluquero.setText(viewModel.getNombreCliente() != null ? viewModel.getNombreCliente() : "-");
        String fechaDetalle = "-";
        if (viewModel.getFecha() != null && !viewModel.getFecha().isEmpty()) {
            // formatear ISO yyyy-MM-dd y hora HH:mm a formato legible en español
            try {
                String iso = viewModel.getFecha(); // yyyy-MM-dd
                String hora = viewModel.getHora(); // HH:mm
                String[] parts = iso.split("-");
                if (parts.length == 3) {
                    int y = Integer.parseInt(parts[0]);
                    int m = Integer.parseInt(parts[1]);
                    int d = Integer.parseInt(parts[2]);
                    String[] meses = DateFormatSymbols.getInstance(Locale.forLanguageTag("es")).getMonths();
                    String nombreMes = meses[m - 1];
                    String horaPart = (hora != null && !hora.isEmpty())
                            ? String.format(Locale.getDefault(), ", %s hs", hora)
                            : "";
                    fechaDetalle = String.format(Locale.getDefault(), "%d de %s%s", d, nombreMes, horaPart);
                } else {
                    fechaDetalle = iso + (viewModel.getHora() != null ? ", " + viewModel.getHora() : "");
                }
            } catch (Exception e) {
                fechaDetalle = viewModel.getFecha() + (viewModel.getHora() != null ? ", " + viewModel.getHora() : "");
            }
        }
        tvDetalleFecha.setText(fechaDetalle);

        btnConfirm.setOnClickListener(v -> {
            // Aquí podrías guardar en la BD o llamar al repositorio
            requireActivity().finish();
        });

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}
