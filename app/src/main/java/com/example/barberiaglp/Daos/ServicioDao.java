package com.example.barberiaglp.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.example.barberiaglp.Modelos.Servicio;

@Dao
public interface ServicioDao {
    @Insert
    void insertar(Servicio servicio);

    @Query("SELECT * FROM servicios")
    List<Servicio> obtenerTodos();

    @Query("SELECT * FROM servicios WHERE id = :id")
    Servicio findById(int id);


    @Delete
    void delete(Servicio servicio);

    @Query("DELETE FROM servicios")
    void deleteAllServicios();

    @Query("SELECT * FROM servicios ORDER BY nombre ASC")
    LiveData<List<Servicio>> getAllServicios();

    @Update
    void update(Servicio servicio);
}
