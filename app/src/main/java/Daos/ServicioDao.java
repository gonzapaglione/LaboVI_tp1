package Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Modelos.Servicio;

@Dao
public interface ServicioDao {
    @Insert
    void insertar(Servicio servicio);

    @Query("SELECT * FROM servicios")
    List<Servicio> obtenerTodos();

}
