// En Modelos/BarberoServicio.java
package Modelos;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

// La anotación @Entity define que esta es una tabla.
// Con primaryKeys le decimos que la combinación de barberoId y servicioId es única.
// Los índices (indices) hacen que las búsquedas por estos IDs sean mucho más rápidas.
@Entity(tableName = "barbero_servicios",
        primaryKeys = {"barberoId", "servicioId"},
        indices = {@Index("barberoId"), @Index("servicioId")},
        foreignKeys = {
                // Clave foránea que apunta a la tabla 'usuarios'
                @ForeignKey(entity = Usuario.class,
                        parentColumns = "id",
                        childColumns = "barberoId",
                        onDelete = ForeignKey.CASCADE), // Si se borra un barbero, se borran sus relaciones
                // Clave foránea que apunta a la tabla 'servicios'
                @ForeignKey(entity = Servicio.class,
                        parentColumns = "id",
                        childColumns = "servicioId",
                        onDelete = ForeignKey.CASCADE) // Si se borra un servicio, se borran sus relaciones
        })
public class BarberoServicios {

    public int barberoId;

    public int servicioId;

    public BarberoServicios(int barberoId, int servicioId) {
        this.barberoId = barberoId;
        this.servicioId = servicioId;
    }

    public BarberoServicios() {
    }
}
