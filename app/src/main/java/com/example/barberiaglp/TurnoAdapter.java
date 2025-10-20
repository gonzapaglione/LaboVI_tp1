package com.example.barberiaglp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import BD.AppDatabase;
import Modelos.Turno;
import Modelos.TurnoConDetalles;
import Repositorios.TurnosRepositorio;

public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder> {

    private TurnosRepositorio turnoRepo;
    private final List<TurnoConDetalles> turnos;

    private final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

    public TurnoAdapter(List<TurnoConDetalles> turnos, TurnosRepositorio turnoRepo) {
        this.turnos = turnos;
        this.turnoRepo = turnoRepo;
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        TurnoConDetalles detalles = turnos.get(position);

        try {
            Date fechaDate = parser.parse(detalles.turno.fecha);
            String fechaFormateada = formatter.format(fechaDate);
            String fechaFinal = fechaFormateada.substring(0, 1).toUpperCase() + fechaFormateada.substring(1);
            holder.tvFecha.setText(fechaFinal);
        } catch (Exception e) {
            holder.tvFecha.setText(detalles.turno.fecha);
            e.printStackTrace();
        }

        holder.tvHora.setText(detalles.turno.horaInicio);
        holder.tvEstado.setText(detalles.turno.estado);
        holder.tvBarbero.setText(detalles.nombreBarbero);
        holder.tvServicio.setText(detalles.nombreServicio);
        obtenerEstiloEstado(detalles, holder);

        // --- Lógica para habilitar/deshabilitar botón de cancelar ---
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date turnoDate = sdf.parse(detalles.turno.fecha + " " + detalles.turno.horaInicio);

            Calendar limite = Calendar.getInstance();
            limite.add(Calendar.HOUR_OF_DAY, 4); // límite para cancelar: 4h antes

            if (turnoDate.before(limite.getTime()) || detalles.turno.estado.equals("cancelado")) {
                holder.btnCancelarTurno.setEnabled(false);
                holder.btnCancelarTurno.setAlpha(0.5f); // visualmente deshabilitado
            } else {
                holder.btnCancelarTurno.setEnabled(true);
                holder.btnCancelarTurno.setAlpha(1f);

                holder.btnCancelarTurno.setOnClickListener(v -> {
                    cancelarTurno(detalles.turno);
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("TurnoAdapter", "Error al parsear la fecha: " + e.getMessage());
            holder.btnCancelarTurno.setEnabled(false);
            holder.btnCancelarTurno.setAlpha(0.5f);
        }
    }
    public void cancelarTurno(Turno turno) {
        Calendar limite = Calendar.getInstance();
        limite.add(Calendar.HOUR_OF_DAY, 4);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date turnoDate = sdf.parse(turno.fecha + " " + turno.horaInicio);
            if (turnoDate.before(limite.getTime())) return; // no se puede cancelar
            turno.estado = "Cancelado";
            AppDatabase.databaseWriteExecutor.execute(() -> turnoRepo.update(turno));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void obtenerEstiloEstado(TurnoConDetalles detalle, TurnoViewHolder holder) {
        holder.tvEstado.setPadding(16, 8, 16, 8); //para que esté bien el texto

        if ("Pendiente".equals(detalle.turno.estado)) {
            holder.tvEstado.setBackgroundResource(R.drawable.estado_pendiente_background);
        } else if ("Atendido".equals(detalle.turno.estado)) {
            holder.tvEstado.setBackgroundResource(R.drawable.estado_atendido_background);
        } else {
            holder.tvEstado.setBackgroundResource(R.drawable.estado_cancelado_background);
        }
    }

    @Override
    public int getItemCount() {
        return turnos.size();
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvHora, tvEstado, tvBarbero, tvServicio;
        Button btnCancelarTurno;
        private TurnosRepositorio turnoRepo;


        TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvBarbero = itemView.findViewById(R.id.tvBarber);
            tvServicio = itemView.findViewById(R.id.tvNombreServicio);
            btnCancelarTurno = itemView.findViewById(R.id.btnCancelarTurno);
        }

    }

    public void actualizarTurnos(List<TurnoConDetalles> nuevosTurnos) {
        this.turnos.clear();
        this.turnos.addAll(nuevosTurnos);
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado y debe redibujarse.
    }
}
