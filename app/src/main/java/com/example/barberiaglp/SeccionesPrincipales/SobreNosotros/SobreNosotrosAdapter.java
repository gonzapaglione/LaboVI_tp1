package com.example.barberiaglp.SeccionesPrincipales.SobreNosotros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.barberiaglp.Modelos.Servicio;
import com.example.barberiaglp.R;

public class SobreNosotrosAdapter extends RecyclerView.Adapter<SobreNosotrosAdapter.ServicioViewHolder> {

    private List<Servicio> servicios;

    public SobreNosotrosAdapter(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = servicios.get(position);

        holder.tvTitulo.setText(servicio.nombre);
        holder.tvDescripcion.setText(servicio.descripcion);
        holder.tvPrecio.setText("$" + (int) servicio.precio);

        // Imagen por defecto segun el nombre del servicio
        switch (servicio.nombre) {
            case "Corte de cabello":
                holder.imgServicio.setImageResource(R.drawable.fotocorte);
                break;
            case "Perfilado de barba":
                holder.imgServicio.setImageResource(R.drawable.fotobarba);
                break;
            case "Corte + Barba":
                holder.imgServicio.setImageResource(R.drawable.fotocortebarba);
                break;
            default:
                holder.imgServicio.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    static class ServicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion, tvPrecio;
        ImageView imgServicio;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvServicioTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvServicioDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvServicioPrecio);
            imgServicio = itemView.findViewById(R.id.imgServicio);
        }
    }
}
