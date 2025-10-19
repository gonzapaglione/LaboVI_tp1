package com.example.barberiaglp.reservaTurno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Modelos.Servicio;
import com.example.barberiaglp.R;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {

    private final List<Servicio> items;
    private final int itemLayoutResId;

    public interface OnItemClickListener {
        void onItemClick(Servicio servicio);
    }

    private OnItemClickListener listener;

    public ReservaAdapter(List<Servicio> items, int itemLayoutResId) {
        this.items = items;
        this.itemLayoutResId = itemLayoutResId;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutResId, parent, false);
        return new ViewHolder(view);
    }

    private int selectedPosition = RecyclerView.NO_POSITION;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Servicio s = items.get(position);
        holder.tvTitulo.setText(s.nombre);
        holder.tvDescripcion.setText(s.descripcion != null ? s.descripcion : "");

        // Si el layout contiene un RadioButton lo manejamos
        if (holder.rb != null) {
            holder.rb.setChecked(position == selectedPosition);
            holder.rb.setOnClickListener(v -> selectPosition(position));
        }

        holder.itemView.setOnClickListener(v -> {
            // click en todo el item equivale a seleccionar
            if (holder.rb != null) {
                selectPosition(position);
            } else {
                if (listener != null)
                    listener.onItemClick(s);
            }
        });
    }

    private void selectPosition(int position) {
        if (position == selectedPosition)
            return; // no cambios
        int old = selectedPosition;
        selectedPosition = position;
        if (old != RecyclerView.NO_POSITION)
            notifyItemChanged(old);
        notifyItemChanged(selectedPosition);
        if (listener != null)
            listener.onItemClick(items.get(selectedPosition));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion;
        android.widget.RadioButton rb;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvServicioTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvServicioDescripcion);
            // rb puede no existir en layout item_reserva_2
            rb = itemView.findViewById(R.id.rbSeleccion);
        }
    }
}
