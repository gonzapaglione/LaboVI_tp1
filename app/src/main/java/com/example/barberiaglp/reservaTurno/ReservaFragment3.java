package com.example.barberiaglp.reservaTurno;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.barberiaglp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ReservaFragment3 extends Fragment {

    private ReservaViewModel viewModel;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva_3, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ReservaViewModel.class);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Spinner spinnerHoras = view.findViewById(R.id.spinnerHoras);
        Button btnNext = view.findViewById(R.id.btnNext3);
        View btnBack = view.findViewById(R.id.btnBack3);

        configurarCalendario(calendarView);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
        }

        btnNext.setOnClickListener(v -> {
            if (viewModel.getFechaSeleccionada().getValue() == null || viewModel.getHoraSeleccionada().getValue() == null) {
                Toast.makeText(getContext(), "Seleccione fecha y hora", Toast.LENGTH_SHORT).show();
                return;
            }
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.reserva_fragment_container, new ReservaFragment4())
                    .addToBackStack(null)
                    .commit();
        });


        viewModel.getFechasDisponibles().observe(getViewLifecycleOwner(), fechas -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, fechas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        });

        viewModel.getHorasDisponibles().observe(getViewLifecycleOwner(), horas -> {
            if (horas == null || horas.isEmpty()) {
                Log.d("ReservaFragment3", "No hay horas disponibles");
                spinnerHoras.setEnabled(false);
                return;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, horas) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((TextView) view).setTextColor(Color.WHITE);
                    return view;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    ((TextView) view).setTextColor(Color.BLACK);
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHoras.setAdapter(adapter);
            spinnerHoras.setEnabled(!horas.isEmpty());
        });



        spinnerHoras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String hora = (String) parent.getItemAtPosition(position);
                viewModel.setHora(hora); // Notificar al ViewModel
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.setHora(null);
            }
        });

        return view;
    }
    private void configurarCalendario(CalendarView calendarView) {
        Calendar calendar = Calendar.getInstance();

        // 1. Deshabilitar fechas pasadas.
        calendarView.setMinDate(calendar.getTimeInMillis());

        // 2. Limitar la selección a, 30 días en el futuro.
        calendar.add(Calendar.DAY_OF_MONTH, 40);
        calendarView.setMaxDate(calendar.getTimeInMillis());

        // 3. Listener para cuando el usuario selecciona una fecha.
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            int diaDeLaSemana = selectedDate.get(Calendar.DAY_OF_WEEK);
            if (diaDeLaSemana == Calendar.SUNDAY || diaDeLaSemana == Calendar.SATURDAY) {
                Toast.makeText(getContext(), "No se aceptan reservas en fin de semana", Toast.LENGTH_SHORT).show();
                // Reiniciamos la selección en el ViewModel para invalidar el día.
                viewModel.setFecha(null);
            } else {
                // El día es válido, lo formateamos y lo guardamos en el ViewModel.
                String fechaFormateada = sdf.format(selectedDate.getTime());
                viewModel.setFecha(fechaFormateada);
            }
        });
        String fechaPreviaStr = viewModel.getFechaSeleccionada().getValue();
        if (fechaPreviaStr != null) {
            try {
                Calendar fechaPreviaCal = Calendar.getInstance();
                fechaPreviaCal.setTime(sdf.parse(fechaPreviaStr));
                calendarView.setDate(fechaPreviaCal.getTimeInMillis(), true, true);
            } catch (Exception e) {
            }
        }
    }
    }


