package com.example.barberiaglp.BD;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.barberiaglp.Daos.HorarioDao;
import com.example.barberiaglp.Daos.ServicioDao;
import com.example.barberiaglp.Daos.TurnoDao;
import com.example.barberiaglp.Daos.UsuarioDao;
import com.example.barberiaglp.Modelos.BarberoServicios;
import com.example.barberiaglp.Daos.BarberoServicioDao;
import com.example.barberiaglp.Modelos.Horario;
import com.example.barberiaglp.Modelos.Servicio;
import com.example.barberiaglp.Modelos.Turno;
import com.example.barberiaglp.Modelos.Usuario;

@Database(entities = { Usuario.class, Servicio.class, Turno.class, Horario.class, BarberoServicios.class }, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract UsuarioDao usuarioDao();

    public abstract ServicioDao servicioDao();

    public abstract TurnoDao turnoDao();

    public abstract HorarioDao horarioDao();

    public abstract BarberoServicioDao BarberoServicioDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "barberia_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // solo para pruebas
                    .build();
        }
        return instancia;
    }
}
