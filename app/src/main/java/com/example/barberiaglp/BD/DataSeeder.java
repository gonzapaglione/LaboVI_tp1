package com.example.barberiaglp.BD;

import android.content.Context;
import java.util.List;

import com.example.barberiaglp.Modelos.BarberoServicios;
import com.example.barberiaglp.Modelos.Horario;
import com.example.barberiaglp.Modelos.Servicio;
import com.example.barberiaglp.Modelos.Turno;
import com.example.barberiaglp.Modelos.Usuario;

public class DataSeeder {

    public static void cargarDatos(Context context) {
        cargarServiciosPredeterminados(context);
        List<Integer> barberoIds = cargarBarberosPredeterminados(context);
        cargarHorariosPredeterminados(context, barberoIds);
        cargarBarberoServicioPredeterminados(context, barberoIds);
    }

    public static void cargarTurnosPredetermiandos(Context context, int clienteId) {
        AppDatabase db = AppDatabase.getInstance(context);

        // Verificar que el usuario/cliente existe antes de crear turnos
        Usuario cliente = db.usuarioDao().findById(clienteId);
        if (cliente == null) {
            // El usuario no existe en BD local, no podemos crear turnos
            return;
        }

        // Obtener los barberos reales de la BD (con sus IDs autogenerados)
        List<Usuario> barberos = db.usuarioDao().obtenerPorRol("Barbero");
        if (barberos.isEmpty() || barberos.size() < 3) {
            // No hay suficientes barberos en la BD
            return;
        }

        // barberos[0] = Martin, barberos[1] = Juan, barberos[2] = José
        int martinId = barberos.get(0).id;
        int juanId = barberos.get(1).id;
        int joseId = barberos.get(2).id;

        Turno turno1 = new Turno();
        turno1.fecha = "2025-10-19";
        turno1.horaInicio = "10:00";
        turno1.servicioId = 2;
        turno1.barberoId = martinId; // Usar ID real de Martin
        turno1.clienteId = clienteId;
        turno1.estado = "Atendido";

        Turno turno2 = new Turno();
        turno2.fecha = "2025-10-05";
        turno2.horaInicio = "12:00";
        turno2.servicioId = 3;
        turno2.barberoId = juanId; // Usar ID real de Juan
        turno2.clienteId = clienteId;
        turno2.estado = "Atendido";

        Turno turno3 = new Turno();
        turno3.fecha = "2025-09-13";
        turno3.horaInicio = "20:00";
        turno3.servicioId = 1;
        turno3.barberoId = joseId; // Usar ID real de José
        turno3.clienteId = clienteId;
        turno3.estado = "Cancelado";

        Turno turno4 = new Turno();
        turno4.fecha = "2025-08-03";
        turno4.horaInicio = "20:00";
        turno4.servicioId = 3;
        turno4.barberoId = juanId; // Usar ID real de Juan
        turno4.clienteId = clienteId;
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

    private static List<Integer> cargarBarberosPredeterminados(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        List<Usuario> barberosExistentes = db.usuarioDao().obtenerPorRol("Barbero");
        List<Integer> barberoIds = new java.util.ArrayList<>();

        if (barberosExistentes.isEmpty()) {
            Usuario barbero1 = new Usuario();
            barbero1.nombre = "Martin";
            barbero1.apellido = "Lopez";
            barbero1.email = "martinlopez@gmail.com";
            barbero1.password = "martin123";
            barbero1.rol = "Barbero";
            barbero1.fechaRegistro = "17 de Octubre, 2025";
            barbero1.fechaNacimiento = "10/01/1998";
            long id1 = db.usuarioDao().insertWithId(barbero1);
            barberoIds.add((int) id1);

            Usuario barbero2 = new Usuario();
            barbero2.nombre = "Juan";
            barbero2.apellido = "Perez";
            barbero2.email = "juanp@gmail.com";
            barbero2.password = "juan123";
            barbero2.rol = "Barbero";
            barbero2.fechaRegistro = "17 de Octubre, 2025";
            barbero2.fechaNacimiento = "28/09/1999";
            long id2 = db.usuarioDao().insertWithId(barbero2);
            barberoIds.add((int) id2);

            Usuario barbero3 = new Usuario();
            barbero3.nombre = "José";
            barbero3.apellido = "Gomez";
            barbero3.email = "josegz@gmail.com";
            barbero3.password = "jose123";
            barbero3.rol = "Barbero";
            barbero3.fechaRegistro = "17 de Octubre, 2025";
            barbero3.fechaNacimiento = "04/11/1996";
            long id3 = db.usuarioDao().insertWithId(barbero3);
            barberoIds.add((int) id3);
        } else {
            // Si ya existen, devolver sus IDs
            for (Usuario barbero : barberosExistentes) {
                barberoIds.add(barbero.id);
            }
        }

        return barberoIds;
    }

    private static void cargarBarberoServicioPredeterminados(Context context, List<Integer> barberoIds) {
        AppDatabase db = AppDatabase.getInstance(context);
        List<BarberoServicios> barberoServiciosExistentes = db.BarberoServicioDao().obtenerTodos();

        if (barberoServiciosExistentes.isEmpty() && barberoIds.size() >= 3) {
            // barberoIds[0] = Martin (id real), barberoIds[1] = Juan, barberoIds[2] = José

            BarberoServicios barberoServicio1 = new BarberoServicios();
            barberoServicio1.barberoId = barberoIds.get(1); // Juan - Corte
            barberoServicio1.servicioId = 1;
            db.BarberoServicioDao().insert(barberoServicio1);

            BarberoServicios barberoServicio2 = new BarberoServicios();
            barberoServicio2.barberoId = barberoIds.get(1); // Juan - Barba
            barberoServicio2.servicioId = 2;
            db.BarberoServicioDao().insert(barberoServicio2);

            BarberoServicios barberoServicio3 = new BarberoServicios();
            barberoServicio3.barberoId = barberoIds.get(1); // Juan - Corte+Barba
            barberoServicio3.servicioId = 3;
            db.BarberoServicioDao().insert(barberoServicio3);

            BarberoServicios barberoServicio4 = new BarberoServicios();
            barberoServicio4.barberoId = barberoIds.get(2); // José - Corte
            barberoServicio4.servicioId = 1;
            db.BarberoServicioDao().insert(barberoServicio4);

            BarberoServicios barberoServicio5 = new BarberoServicios();
            barberoServicio5.barberoId = barberoIds.get(0); // Martin - Barba
            barberoServicio5.servicioId = 2;
            db.BarberoServicioDao().insert(barberoServicio5);
        }
    }

    private static void cargarHorariosPredeterminados(Context context, List<Integer> barberoIds) {
        AppDatabase db = AppDatabase.getInstance(context);
        List<Horario> horariosExistentes = db.horarioDao().obtenerTodos();

        if (horariosExistentes.isEmpty() && barberoIds.size() >= 3) {
            // barberoIds[0] = Martin, barberoIds[1] = Juan, barberoIds[2] = José
            int martinId = barberoIds.get(0);
            int juanId = barberoIds.get(1);
            int joseId = barberoIds.get(2);

            // Horarios de Juan (barberoId original era 2)
            Horario horario1 = new Horario();
            horario1.barberoId = juanId;
            horario1.dia = "Lunes";
            horario1.horaInicio = "10:00";
            horario1.horaFin = "20:00";
            db.horarioDao().insertar(horario1);

            Horario horario2 = new Horario();
            horario2.barberoId = juanId;
            horario2.dia = "Martes";
            horario2.horaInicio = "10:00";
            horario2.horaFin = "20:00";
            db.horarioDao().insertar(horario2);

            Horario horario3 = new Horario();
            horario3.barberoId = juanId;
            horario3.dia = "Miercoles";
            horario3.horaInicio = "10:00";
            horario3.horaFin = "20:00";
            db.horarioDao().insertar(horario3);

            Horario horario4 = new Horario();
            horario4.barberoId = juanId;
            horario4.dia = "Jueves";
            horario4.horaInicio = "10:00";
            horario4.horaFin = "20:00";
            db.horarioDao().insertar(horario4);

            Horario horario5 = new Horario();
            horario5.barberoId = juanId;
            horario5.dia = "Viernes";
            horario5.horaInicio = "14:00";
            horario5.horaFin = "22:00";
            db.horarioDao().insertar(horario5);

            // Horarios de José (barberoId original era 3)
            Horario horario6 = new Horario();
            horario6.barberoId = joseId;
            horario6.dia = "Lunes";
            horario6.horaInicio = "14:00";
            horario6.horaFin = "21:00";
            db.horarioDao().insertar(horario6);

            Horario horario7 = new Horario();
            horario7.barberoId = joseId;
            horario7.dia = "Martes";
            horario7.horaInicio = "14:00";
            horario7.horaFin = "21:00";
            db.horarioDao().insertar(horario7);

            Horario horario8 = new Horario();
            horario8.barberoId = joseId;
            horario8.dia = "Miercoles";
            horario8.horaInicio = "14:00";
            horario8.horaFin = "21:00";
            db.horarioDao().insertar(horario8);

            Horario horario9 = new Horario();
            horario9.barberoId = joseId;
            horario9.dia = "Jueves";
            horario9.horaInicio = "14:00";
            horario9.horaFin = "21:00";
            db.horarioDao().insertar(horario9);

            Horario horario10 = new Horario();
            horario10.barberoId = joseId;
            horario10.dia = "Viernes";
            horario10.horaInicio = "14:00";
            horario10.horaFin = "22:30";
            db.horarioDao().insertar(horario10);

            // Horarios de Martin (barberoId original era 4)
            Horario horario11 = new Horario();
            horario11.barberoId = martinId;
            horario11.dia = "Lunes";
            horario11.horaInicio = "14:00";
            horario11.horaFin = "21:00";
            db.horarioDao().insertar(horario11);

            Horario horario12 = new Horario();
            horario12.barberoId = martinId;
            horario12.dia = "Martes";
            horario12.horaInicio = "14:00";
            horario12.horaFin = "21:00";
            db.horarioDao().insertar(horario12);

            Horario horario13 = new Horario();
            horario13.barberoId = martinId;
            horario13.dia = "Jueves";
            horario13.horaInicio = "14:00";
            horario13.horaFin = "22:30";
            db.horarioDao().insertar(horario13);

            Horario horario14 = new Horario();
            horario14.barberoId = martinId;
            horario14.dia = "Viernes";
            horario14.horaInicio = "14:00";
            horario14.horaFin = "22:30";
            db.horarioDao().insertar(horario14);
        }
    }

}
