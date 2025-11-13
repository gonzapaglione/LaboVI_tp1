package com.example.barberiaglp.Modelos;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "horarios",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "id",
                childColumns = "barberoId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Horario {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int barberoId;  // Usuario con rol = "barbero"
    public String dia;     // Ej: "Lunes"
    public String horaInicio;
    public String horaFin;
}