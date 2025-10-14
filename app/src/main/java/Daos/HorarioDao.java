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

    @Query("SELECT * FROM horarios WHERE barberoId = :barberoId")
    List<Horario> obtenerHorariosDeBarbero(int barberoId);
}
