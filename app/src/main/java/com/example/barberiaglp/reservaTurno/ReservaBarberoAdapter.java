package com.example.barberiaglp.reservaTurno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberiaglp.R;

import java.util.List;

import com.example.barberiaglp.Modelos.Usuario;

public class ReservaBarberoAdapter extends RecyclerView.Adapter<ReservaBarberoAdapter.ViewHolder> {
    private final List<Usuario> items;
    private final int itemLayoutResId;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(Usuario u);
    }

    private OnItemClickListener listener;

    public ReservaBarberoAdapter(List<Usuario> items, int itemLayoutResId) {
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario u = items.get(position);
        String nombre = u.nombre != null ? u.nombre : "";
        String apellido = u.apellido != null ? u.apellido : "";
        holder.tvNombre.setText((nombre + " " + apellido).trim());
        // manejar estado del RadioButton
        if (holder.rb != null) {
            holder.rb.setChecked(position == selectedPosition);
            holder.rb.setOnClickListener(v -> selectPosition(position));
        }

        holder.itemView.setOnClickListener(v -> selectPosition(position));
    }

    private void selectPosition(int position) {
        if (position == selectedPosition)
            return;
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
        TextView tvNombre;
        android.widget.RadioButton rb;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreBarber);
            rb = itemView.findViewById(R.id.rbSeleccionBarbero);
        }
    }
}
