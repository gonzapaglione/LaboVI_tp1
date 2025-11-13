package com.example.barberiaglp.Modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "servicios")
public class Servicio {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public int duracionMin; // duración en minutos
    public double precio;
    public String descripcion;
}