package BD;

import android.content.Context;
import java.util.List;
import Modelos.Servicio;

public class DataSeeder {

    public static void cargarDatos(Context context){
        cargarServiciosPredeterminados(context);
    }

    public static void cargarServiciosPredeterminados(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        List<Servicio> serviciosExistentes = db.servicioDao().obtenerTodos();

        if (serviciosExistentes.isEmpty()) {
            Servicio corte = new Servicio();
            corte.nombre = "Corte de cabello";
            corte.descripcion = "Cortes clásicos y modernos adaptados a tu estilo.";
            corte.duracionMin = 30;
            corte.precio = 7000;

            Servicio barba = new Servicio();
            barba.nombre = "Perfilado de barba";
            barba.descripcion = "Definí tu estilo con un perfilado preciso y prolijo.";
            barba.duracionMin = 25;
            barba.precio = 9000;

            Servicio premium = new Servicio();
            premium.nombre = "Corte + Barba";
            premium.descripcion = "Experiencia completa con un corte moderno a tijera y/o maquina + perfilado de barba.";
            premium.duracionMin = 60;
            premium.precio = 11000;

            db.servicioDao().insertar(corte);
            db.servicioDao().insertar(barba);
            db.servicioDao().insertar(premium);
        }
    }
}
