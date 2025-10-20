package BD;

import android.content.Context;
import java.util.List;

import Modelos.BarberoServicios;
import Modelos.Horario;
import Modelos.Servicio;
import Modelos.Turno;
import Modelos.Usuario;

public class DataSeeder {

    public static void cargarDatos(Context context){
        cargarServiciosPredeterminados(context);
        cargarBarberosPredeterminados(context);
        cargarHorariosPredeterminados(context);
        cargarBarberoServicioPredeterminados(context);
    }

    public static void cargarTurnosPredetermiandos(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);

            Turno turno1 = new Turno();
            turno1.fecha = "2025-10-19";
            turno1.horaInicio = "10:00";
            turno1.servicioId = 2;
            turno1.barberoId = 4;
            turno1.clienteId = 1;
            turno1.estado = "Atendido";

            Turno turno2 = new Turno();
            turno2.fecha = "2025-10-05";
            turno2.horaInicio = "12:00";
            turno2.servicioId = 3;
            turno2.barberoId = 2;
            turno2.clienteId = 1;
            turno2.estado = "Atendido";

            Turno turno3 = new Turno();
            turno3.fecha = "2025-09-13";
            turno3.horaInicio = "20:00";
            turno3.servicioId = 1;
            turno3.barberoId = 3;
            turno3.clienteId = 1;
            turno3.estado = "Cancelado";


            Turno turno4 = new Turno();
            turno4.fecha = "2025-08-03";
            turno4.horaInicio = "20:00";
            turno4.servicioId = 3;
            turno4.barberoId = 2;
            turno4.clienteId = 1;
            turno4.estado = "Atendido";

            db.turnoDao().insertar(turno1);
            db.turnoDao().insertar(turno2);
            db.turnoDao().insertar(turno3);
            db.turnoDao().insertar(turno4);

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

        private static void cargarBarberoServicioPredeterminados(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        List<BarberoServicios> barberoServiciosExistentes = db.BarberoServicioDao().obtenerTodos();
        if(barberoServiciosExistentes.isEmpty()) {
         BarberoServicios barberoServicio1 = new BarberoServicios();
         barberoServicio1.barberoId = 2;
         barberoServicio1.servicioId = 1;
         db.BarberoServicioDao().insert(barberoServicio1);

         BarberoServicios barberoServicio2 = new BarberoServicios();
         barberoServicio2.barberoId = 2;
         barberoServicio2.servicioId = 2;
         db.BarberoServicioDao().insert(barberoServicio2);

         BarberoServicios barberoServicio3 = new BarberoServicios();
         barberoServicio3.barberoId = 2;
         barberoServicio3.servicioId = 3;
         db.BarberoServicioDao().insert(barberoServicio3);

         BarberoServicios barberoServicio4 = new BarberoServicios();
         barberoServicio4.barberoId = 3;
         barberoServicio4.servicioId = 1;
         db.BarberoServicioDao().insert(barberoServicio4);

         BarberoServicios barberoServicio5 = new BarberoServicios();
         barberoServicio5.barberoId = 4;
         barberoServicio5.servicioId = 2;
         db.BarberoServicioDao().insert(barberoServicio5);
        }
    }

        private static void cargarHorariosPredeterminados(Context context){
            AppDatabase db = AppDatabase.getInstance(context);
            List<Horario> horariosExistentes = db.horarioDao().obtenerTodos();
            if(horariosExistentes.isEmpty()){
                Horario horario1 = new Horario();
                horario1.barberoId = 2;
                horario1.dia = "Lunes";
                horario1.horaInicio = "10:00";
                horario1.horaFin = "20:00";
                db.horarioDao().insertar(horario1);

                Horario horario2 = new Horario();
                horario2.barberoId = 2;
                horario2.dia = "Martes";
                horario2.horaInicio = "10:00";
                horario2.horaFin = "20:00";
                db.horarioDao().insertar(horario2);

                Horario horario3 = new Horario();
                horario3.barberoId = 2;
                horario3.dia = "Miercoles";
                horario3.horaInicio = "10:00";
                horario3.horaFin = "20:00";
                db.horarioDao().insertar(horario3);

                Horario horario4 = new Horario();
                horario4.barberoId = 2;
                horario4.dia = "Jueves";
                horario4.horaInicio = "10:00";
                horario4.horaFin = "20:00";
                db.horarioDao().insertar(horario4);

                Horario horario5 = new Horario();
                horario5.barberoId = 2;
                horario5.dia = "Viernes";
                horario5.horaInicio = "14:00";
                horario5.horaFin = "22:00";
                db.horarioDao().insertar(horario5);

                Horario horario6 = new Horario();
                horario6.barberoId = 3;
                horario6.dia = "Lunes";
                horario6.horaInicio = "14:00";
                horario6.horaFin = "21:00";
                db.horarioDao().insertar(horario6);

                Horario horario7 = new Horario();
                horario7.barberoId = 3;
                horario7.dia = "Martes";
                horario7.horaInicio = "14:00";
                horario7.horaFin = "21:00";
                db.horarioDao().insertar(horario7);

                Horario horario8 = new Horario();
                horario8.barberoId = 3;
                horario8.dia = "Miercoles";
                horario8.horaInicio = "14:00";
                horario8.horaFin = "21:00";
                db.horarioDao().insertar(horario8);

                Horario horario9 = new Horario();
                horario9.barberoId = 3;
                horario9.dia = "Jueves";
                horario9.horaInicio = "14:00";
                horario9.horaFin = "21:00";
                db.horarioDao().insertar(horario9);

                Horario horario10 = new Horario();
                horario10.barberoId = 3;
                horario10.dia = "Viernes";
                horario10.horaInicio = "14:00";
                horario10.horaFin = "22:30";
                db.horarioDao().insertar(horario10);

                Horario horario11 = new Horario();
                horario11.barberoId = 4;
                horario11.dia = "Lunes";
                horario11.horaInicio = "14:00";
                horario11.horaFin = "21:00";
                db.horarioDao().insertar(horario11);

                Horario horario12 = new Horario();
                horario12.barberoId = 4;
                horario12.dia = "Martes";
                horario12.horaInicio = "14:00";
                horario12.horaFin = "21:00";
                db.horarioDao().insertar(horario12);

                Horario horario13 = new Horario();
                horario13.barberoId = 4;
                horario13.dia = "Jueves";
                horario13.horaInicio = "14:00";
                horario13.horaFin = "22:30";
                db.horarioDao().insertar(horario13);

                Horario horario14 = new Horario();
                horario14.barberoId = 4;
                horario14.dia = "Viernes";
                horario14.horaInicio = "14:00";
                horario14.horaFin = "22:30";
                db.horarioDao().insertar(horario14);

            }
        }

}
