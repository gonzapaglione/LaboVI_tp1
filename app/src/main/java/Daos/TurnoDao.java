package Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Modelos.Turno;
import Modelos.Usuario;

@Dao
public interface TurnoDao {
    @Insert
    void insertar(Turno turno);

    @Query("SELECT * FROM turnos")
    List<Turno> obtenerTodos();

    @Query("SELECT * FROM turnos WHERE clienteId = :clienteId ORDER BY fecha DESC, horaInicio DESC")
    List<Turno> obtenerTurnosDeCliente(int clienteId);

    @Query("SELECT * FROM turnos WHERE clienteId = :clienteId AND estado = 'Atendido'")
    List<Turno> getTurnosAtendidosPorCliente(int clienteId);

    @Query("SELECT * FROM turnos WHERE barberoId = :barberoId")
    List<Turno> obtenerTurnosDeBarbero(int barberoId);

    @Query("DELETE FROM turnos")
    void deleteAll();

    @Update
    void update(Turno turno);


}
