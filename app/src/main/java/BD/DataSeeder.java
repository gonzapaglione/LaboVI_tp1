package BD;

import android.content.Context;
import java.util.List;

import Modelos.BarberoServicios;
import Modelos.Servicio;
import Modelos.Usuario;

public class DataSeeder {

    public static void cargarDatos(Context context){
        cargarServiciosPredeterminados(context);
        cargarBarberosPredeterminados(context);
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

        private static void cargarBarberosPredeterminados(Context context) {
            AppDatabase db = AppDatabase.getInstance(context);
            List<Usuario> barberosExistentes = db.usuarioDao().obtenerPorRol("Barbero");
            if(barberosExistentes.isEmpty()){
                Usuario barbero1 = new Usuario();
                barbero1.nombre = "Martin";
                barbero1.apellido = "Lopez";
                barbero1.email = "martinlopez@gmail.com";
                barbero1.password = "martin123";
                barbero1.rol = "Barbero";
                barbero1.fechaRegistro = "17 de Octubre, 2025";
                barbero1.fechaNacimiento = "10/01/1998";
                db.usuarioDao().insert(barbero1);

                Usuario barbero2 = new Usuario();
                barbero2.nombre = "Juan";
                barbero2.apellido = "Perez";
                barbero2.email = "juanp@gmail.com";
                barbero2.password = "juan123";
                barbero2.rol = "Barbero";
                barbero2.fechaRegistro = "17 de Octubre, 2025";
                barbero2.fechaNacimiento = "28/09/1999";
                db.usuarioDao().insert(barbero2);

                Usuario barbero3 = new Usuario();
                barbero3.nombre = "José";
                barbero3.apellido = "Gomez";
                barbero3.email = "josegz@gmail.com";
                barbero3.password = "jose123";
                barbero3.rol = "Barbero";
                barbero3.fechaRegistro = "17 de Octubre, 2025";
                barbero3.fechaNacimiento = "04/11/1996";
                db.usuarioDao().insert(barbero3);

            }

        }

}
