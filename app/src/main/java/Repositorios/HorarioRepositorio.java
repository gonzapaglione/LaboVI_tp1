package Repositorios;


import android.app.Application;

import java.util.List;

import BD.AppDatabase;
import Daos.HorarioDao;
import Modelos.Horario;

public class HorarioRepositorio {
    private final HorarioDao horarioDao;
    private Application application;

    public HorarioRepositorio(Application app) {
        this.application = app;
        AppDatabase db = AppDatabase.getInstance(application);
        this.horarioDao = db.horarioDao();
    }

    public List<Horario> getHorariosPorBarberoYDia(int idBarbero, String diaSemana) {
        return horarioDao.obtenerHorariosPorBarberoYDia(idBarbero, diaSemana);
    }

    public List<Horario> getAllHorarios() {
        return horarioDao.obtenerTodos();
    }
}
