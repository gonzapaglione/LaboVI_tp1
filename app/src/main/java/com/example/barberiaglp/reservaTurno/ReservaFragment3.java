package com.example.barberiaglp.reservaTurno;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.Calendar;
import java.text.DateFormatSymbols;
import java.util.Locale;

public class ReservaFragment3 extends Fragment {
    private ReservaViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_3, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);

        TextView tvSeleccion = view.findViewById(R.id.tvSeleccionFecha);
        Button btnPick = view.findViewById(R.id.btnPickDate);
        Button btnNext = view.findViewById(R.id.btnNext3);

        // Mostrar valor previo
        if (viewModel.getFecha() != null && !viewModel.getFecha().isEmpty()) {
            String f = viewModel.getFecha() + " " + (viewModel.getHora() != null ? viewModel.getHora() : "");
            tvSeleccion.setText(f);
        }

        btnPick.setOnClickListener(v -> showDateTimePicker(tvSeleccion));

        btnNext.setOnClickListener(v -> {
            // Validar que haya seleccionado fecha y hora
            if (viewModel.getFecha() == null || viewModel.getFecha().isEmpty() || viewModel.getHora() == null
                    || viewModel.getHora().isEmpty()) {
                android.widget.Toast.makeText(getContext(), "Seleccione fecha y hora antes de continuar",
                        android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.reserva_fragment_container, new ReservaFragment4())
                    .addToBackStack(null)
                    .commit();
        });

        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void showDateTimePicker(TextView tvSeleccion) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(requireContext(), (view, y, m, d) -> {
            // guardar fecha
            String fechaStr = String.format("%04d-%02d-%02d", y, m + 1, d);
            viewModel.setFecha(fechaStr);

            // luego mostrar TimePicker
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog tpd = new TimePickerDialog(requireContext(), (timeView, h, min) -> {
                String horaStr = String.format("%02d:%02d", h, min);
                viewModel.setHora(horaStr);
                // Mostrar versión legible en español
                String legible = formatFechaLegible(y, m, d, h, min);
                tvSeleccion.setText(legible);
            }, hour, minute, true);
            tpd.show();
        }, year, month, day);
        dpd.show();
    }

    private String formatFechaLegible(int year, int monthZeroBased, int day, int hour, int minute) {
        // monthZeroBased: 0..11
        String[] meses = DateFormatSymbols.getInstance(Locale.forLanguageTag("es")).getMonths();
        String nombreMes = meses[monthZeroBased];
        // Quitar acentos/normalizar si es necesario (dejamos como viene)
        String horaForm = String.format(Locale.getDefault(), "%02d:%02d hs", hour, minute);
        return String.format(Locale.getDefault(), "%d de %s, %s", day, nombreMes, horaForm);
    }
}
