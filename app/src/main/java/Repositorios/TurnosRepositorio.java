package Repositorios;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import BD.AppDatabase;
import BD.DataSeeder;
import Daos.ServicioDao;
import Daos.TurnoDao;
import Daos.UsuarioDao;
import Modelos.Servicio;
import Modelos.Turno;
import Modelos.TurnoConDetalles;
import Modelos.Usuario;

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
        // 1. Usamos el nuevo métoodo del DAO para obtener solo los turnos atendidos.
        List<Turno> turnosAtendidos = turnoDao.getTurnosAtendidosPorCliente(clienteId);
        return turnosAtendidos;
    }


    public void cargarTurnosPredeterminados() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            DataSeeder.cargarTurnosPredetermiandos(application);
        });
        Toast.makeText(application, "¡Turnos cargados!", Toast.LENGTH_SHORT).show();
    }


}
