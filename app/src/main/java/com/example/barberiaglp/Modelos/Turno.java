package com.example.barberiaglp.Modelos;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "turnos",
        foreignKeys = {
                @ForeignKey(entity = Usuario.class, parentColumns = "id", childColumns = "clienteId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Usuario.class, parentColumns = "id", childColumns = "barberoId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Servicio.class, parentColumns = "id", childColumns = "servicioId", onDelete = ForeignKey.CASCADE)
        }
)
public class Turno {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int clienteId;
    public int barberoId;
    public int servicioId;

    public String fecha;      // Ej: "2025-10-10"
    public String horaInicio; // Ej: "10:00"
    public String horaFin;    // Calculada según duración del servicio
    public String estado;     // "pendiente", "atendido", "cancelado"

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getBarberoId() {
        return barberoId;
    }

    public void setBarberoId(int barberoId) {
        this.barberoId = barberoId;
    }

    public int getServicioId() {
        return servicioId;
    }

    public void setServicioId(int servicioId) {
        this.servicioId = servicioId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}