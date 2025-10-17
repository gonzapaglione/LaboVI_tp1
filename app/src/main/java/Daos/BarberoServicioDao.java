// En Daos/BarberoServicioDao.java
package Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import Modelos.BarberoServicios;
import Modelos.Servicio;

@Dao
public interface BarberoServicioDao {

    // Inserta una nueva relación entre un barbero y un servicio.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(BarberoServicios barberoServicio);

    // Esta es la consulta MÁGICA.
    // Une las tablas 'servicios' y 'barbero_servicios' para obtener todos los
    // servicios (objetos Servicio completos) que corresponden a un barbero específico (barberoId).
    @Query("SELECT s.* FROM servicios s INNER JOIN barbero_servicios bs ON s.id = bs.servicioId WHERE bs.barberoId = :barberoId")
    List<Servicio> getServiciosForBarbero(int barberoId);

    @Query("SELECT * FROM barbero_servicios")
    List<BarberoServicios> obtenerTodos();
}
