package com.example.barberiaglp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Modelos.Turno;
import Modelos.TurnoConDetalles;
import Repositorios.TurnosRepositorio;

public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder> {

    private TurnosRepositorio turnoRepo;
    private final List<TurnoConDetalles> turnos;
    private final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

    public TurnoAdapter(List<TurnoConDetalles> turnos) {
        this.turnos = turnos;
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
            // 1. Usamos el 'parser' para convertir el String de la BD a un objeto Date
            Date fechaDate = parser.parse(detalles.turno.fecha);

            // 2. Usamos el 'formatter' para convertir el objeto Date al String que queremos
            String fechaFormateada = formatter.format(fechaDate);

            // 3. Hacemos la primera letra mayúscula para un mejor estilo (ej. "domingo" -> "Domingo")
            String fechaFinal = fechaFormateada.substring(0, 1).toUpperCase() + fechaFormateada.substring(1);

            // 4. Asignamos la fecha final al TextView
            holder.tvFecha.setText(fechaFinal);

        } catch (Exception e) {
            // Si hay un error al parsear la fecha, mostramos la fecha original para no crashear la app
            holder.tvFecha.setText(detalles.turno.fecha);
            e.printStackTrace();
        }
        holder.tvHora.setText(detalles.turno.horaInicio);
        holder.tvEstado.setText(detalles.turno.estado);
        obtenerEstiloEstado(detalles, holder);
        holder.tvBarbero.setText(detalles.nombreBarbero);
        holder.tvServicio.setText(detalles.nombreServicio);
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

        TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvBarbero = itemView.findViewById(R.id.tvBarber);
            tvServicio = itemView.findViewById(R.id.tvNombreServicio);
        }
    }

    public void actualizarTurnos(List<TurnoConDetalles> nuevosTurnos) {
        this.turnos.clear();
        this.turnos.addAll(nuevosTurnos);
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado y debe redibujarse.
    }
}
