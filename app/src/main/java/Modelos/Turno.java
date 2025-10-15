package Modelos;

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

}