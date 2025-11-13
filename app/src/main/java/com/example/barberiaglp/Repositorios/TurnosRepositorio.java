package com.example.barberiaglp.Repositorios;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import com.example.barberiaglp.BD.AppDatabase;

import com.example.barberiaglp.Daos.ServicioDao;
import com.example.barberiaglp.Daos.TurnoDao;
import com.example.barberiaglp.Daos.UsuarioDao;
import com.example.barberiaglp.Modelos.Servicio;
import com.example.barberiaglp.Modelos.Turno;
import com.example.barberiaglp.Modelos.TurnoConDetalles;
import com.example.barberiaglp.Modelos.Usuario;

public class TurnosRepositorio {
    private TurnoDao turnoDao;
    private UsuarioDao usuarioDao;
    private ServicioDao servicioDao;
    private Application application;
    private LiveData<List<Turno>> allUsers; // Para observar la lista de turnos

    // El constructor recibe la aplicación para poder obtener el contexto de forma segura
    public TurnosRepositorio(Application application) {
        this.application = application; // Guarda la aplicación
        AppDatabase db = AppDatabase.getInstance(application);
        this.turnoDao = db.turnoDao();
        this.usuarioDao = db.usuarioDao();
        this.servicioDao = db.servicioDao();
    }

    // La inserción se ejecuta en un hilo secundario.
    public void insert(Turno turno) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            turnoDao.insertar(turno);
        });
    }


    public void update(Turno turno) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            turnoDao.update(turno);
        });
    }

    public List<TurnoConDetalles> getTurnosConDetalles(int usuarioId) {
        List<Turno> turnosBasicos = turnoDao.obtenerTurnosDeCliente(usuarioId);
        List<TurnoConDetalles> turnosEnriquecidos = new ArrayList<>();

        for (Turno turno : turnosBasicos) {
            Usuario barbero = usuarioDao.findById(turno.barberoId);
            String nombreBarbero = (barbero != null) ? barbero.nombre + " " + barbero.apellido : "Barbero no encontrado";

            Servicio servicio = servicioDao.findById(turno.servicioId);
            String nombreServicio = (servicio != null) ? servicio.nombre : "Servicio no encontrado";

            // 3. Creamos el objeto enriquecido y lo añadimos a la lista final
            turnosEnriquecidos.add(new TurnoConDetalles(turno, nombreBarbero, nombreServicio));
        }

        return turnosEnriquecidos;
    }


    public List<Turno> getTurnosAtendidos(int clienteId) {
        List<Turno> turnosAtendidos = turnoDao.getTurnosAtendidosPorCliente(clienteId);
        return turnosAtendidos;
    }

    public List<String> getHorasOcupadas(int barberoId, String fecha) {
        return turnoDao.getHorasOcupadas(barberoId, fecha);
    }

    public void delete(Turno turno) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            turnoDao.delete(turno.id);
        });
    }
}
