package Repositorios;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import BD.AppDatabase;
import Daos.TurnoDao;
import Modelos.Turno;
import Modelos.Usuario;

public class TurnosRepositorio {
    private TurnoDao turnoDao;
    private Application application;
    private LiveData<List<Turno>> allUsers; // Para observar la lista de turnos

    // El constructor recibe la aplicación para poder obtener el contexto de forma segura
    public TurnosRepositorio(Application application) {
        this.application = application; // Guarda la aplicación
        AppDatabase db = AppDatabase.getInstance(application);
        turnoDao = db.turnoDao();
    }

    // La inserción se ejecuta en un hilo secundario.
    public void insert(Turno turno) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            turnoDao.insertar(turno);
        });
    }

    public void deleteAllTurnos() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            turnoDao.deleteAll();
        });
    }

    public void update(Turno turno) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            turnoDao.update(turno);
        });
    }

}
