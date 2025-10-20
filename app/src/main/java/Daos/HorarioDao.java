package Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Modelos.Horario;

@Dao
public interface HorarioDao {
    @Insert
    void insertar(Horario horario);

    @Query("SELECT * FROM horarios WHERE barberoId = :barberoId AND dia = :diaSemana")
    List<Horario> obtenerHorariosPorBarberoYDia(int barberoId, String diaSemana);
    @Query("SELECT * FROM horarios")
    List<Horario> obtenerTodos();


}
