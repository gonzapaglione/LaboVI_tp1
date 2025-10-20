package Repositorios;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

import BD.AppDatabase;
import Daos.ServicioDao;
import Modelos.Servicio;

public class ServicioRepositorio {

    private ServicioDao servicioDao;
    private LiveData<List<Servicio>> allServicios;

    public ServicioRepositorio(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        servicioDao = database.servicioDao();
        allServicios = servicioDao.getAllServicios();
    }


    public void insert(Servicio servicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            servicioDao.insertar(servicio);
        });
    }


    public void update(Servicio servicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            servicioDao.update(servicio);
        });
    }

    public void delete(Servicio servicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            servicioDao.delete(servicio);
        });
    }

    public void deleteAllServicios() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            servicioDao.deleteAllServicios();
        });
    }


    public LiveData<List<Servicio>> getAllServicios() {
        return allServicios;
    }
}
