package BD;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import Daos.HorarioDao;
import Daos.ServicioDao;
import Daos.TurnoDao;
import Daos.UsuarioDao;
import Modelos.Horario;
import Modelos.Servicio;
import Modelos.Turno;
import Modelos.Usuario;

@Database(entities = {Usuario.class, Servicio.class, Turno.class, Horario.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);


    public abstract UsuarioDao usuarioDao();
    public abstract ServicioDao servicioDao();
    public abstract TurnoDao turnoDao();
    public abstract HorarioDao horarioDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "barberia_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // ⚠️ solo para pruebas
                    .build();
        }
        return instancia;
    }
}
