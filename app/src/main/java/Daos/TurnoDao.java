package Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Modelos.Turno;

@Dao
public interface TurnoDao {
    @Insert
    void insertar(Turno turno);

    @Query("SELECT * FROM turnos WHERE clienteId = :clienteId")
    List<Turno> obtenerTurnosDeCliente(int clienteId);

    @Query("SELECT * FROM turnos WHERE barberoId = :barberoId")
    List<Turno> obtenerTurnosDeBarbero(int barberoId);
}
