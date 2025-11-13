package com.example.barberiaglp.reservaTurno;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.barberiaglp.BD.AppDatabase;
import com.example.barberiaglp.Modelos.Horario;
import com.example.barberiaglp.Modelos.Servicio;
import com.example.barberiaglp.Modelos.Usuario;
import com.example.barberiaglp.Repositorios.HorarioRepositorio;
import com.example.barberiaglp.Repositorios.ServicioRepositorio;
import com.example.barberiaglp.Repositorios.TurnosRepositorio;
import com.example.barberiaglp.Repositorios.UsuarioRepositorio;

public class ReservaViewModel extends AndroidViewModel {

    // 1. REPOSITORIOS: Las únicas fuentes de datos.
    private final UsuarioRepositorio usuarioRepo;
    private final TurnosRepositorio turnoRepo;
    private final ServicioRepositorio servicioRepo;
    private final HorarioRepositorio horarioRepo;

    // MutableLiveData para que los fragmentos puedan "observar" los cambios.
    private final MutableLiveData<Servicio> servicioSeleccionado = new MutableLiveData<>();
    private final MutableLiveData<Usuario> barberoSeleccionado = new MutableLiveData<>();
    private final MutableLiveData<String> fechaSeleccionada = new MutableLiveData<>();
    private final MutableLiveData<String> horaSeleccionada = new MutableLiveData<>();

    // 3. Los datos que se mostrarán en los RecyclerViews o Spinners.
    private final LiveData<List<Servicio>> todosLosServicios; // Inmutable porque la lista completa rara vez cambia.
    private final MutableLiveData<List<Usuario>> barberosFiltrados = new MutableLiveData<>();
    private final MutableLiveData<List<String>> fechasDisponibles = new MutableLiveData<>();
    private final MutableLiveData<List<String>> horasDisponibles = new MutableLiveData<>();

    public ReservaViewModel(@NonNull Application application) {
        super(application);
        // Inicializamos los repositorios.
        usuarioRepo = new UsuarioRepositorio(application);
        turnoRepo = new TurnosRepositorio(application);
        servicioRepo = new ServicioRepositorio(application);
        horarioRepo = new HorarioRepositorio(application);


        // Carga inicial de la lista completa de servicios.
        this.todosLosServicios = servicioRepo.getAllServicios();
    }


    public void setServicio(Servicio servicio) {
        // Para evitar recálculos innecesarios, solo actuamos si el servicio realmente cambia.
        if (servicioSeleccionado.getValue() == null || !servicioSeleccionado.getValue().equals(servicio)) {
            servicioSeleccionado.setValue(servicio);
            // Cada vez que se establece un nuevo servicio, se reinician las selecciones posteriores.
            setBarbero(null);
            // Y se dispara el recálculo de los barberos.
            actualizarBarberosDisponibles();
        }
    }

    public void setBarbero(Usuario barbero) {
        if (barberoSeleccionado.getValue() == null || !barberoSeleccionado.getValue().equals(barbero)) {
            barberoSeleccionado.setValue(barbero);
            setFecha(null);
        }
    }

    public void setFecha(String fecha) {
        if (fechaSeleccionada.getValue() == null || !fechaSeleccionada.getValue().equals(fecha)) {
            fechaSeleccionada.setValue(fecha);
            // Se reinicia la hora y se recalculan las horas.
            setHora(null);
            actualizarHorasDisponibles();
           ;
        }
    }

    public void setHora(String hora) {
        horaSeleccionada.setValue(hora);
    }

    public LiveData<Servicio> getServicioSeleccionado() { return servicioSeleccionado; }
    public LiveData<Usuario> getBarberoSeleccionado() { return barberoSeleccionado; }
    public LiveData<String> getFechaSeleccionada() { return fechaSeleccionada; }
    public LiveData<String> getHoraSeleccionada() { return horaSeleccionada; }

    public LiveData<List<Servicio>> getTodosLosServicios() { return todosLosServicios; }
    public LiveData<List<Usuario>> getBarberosFiltrados() { return barberosFiltrados; }
    public LiveData<List<String>> getFechasDisponibles() { return fechasDisponibles; }
    public LiveData<List<String>> getHorasDisponibles() { return horasDisponibles; }


    private void actualizarBarberosDisponibles() {
        Servicio servicio = servicioSeleccionado.getValue();
        if (servicio == null) {
            barberosFiltrados.postValue(new ArrayList<>()); // Si no hay servicio, la lista de barberos está vacía.
            return;
        }
        // Ejecutamos la consulta a la BD en un hilo secundario.
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Usuario> barberos = usuarioRepo.getBarberosPorServicio(servicio.id);
            barberosFiltrados.postValue(barberos);
        });
    }
    public void prepararFechasParaBarberoSeleccionado() {
        actualizarFechasDisponibles();
    }

    private void actualizarFechasDisponibles() {
        Usuario barbero = barberoSeleccionado.getValue();
        if (barbero == null) {
            fechasDisponibles.postValue(new ArrayList<>());
            return;
        }
        // Lógica para generar un rango de fechas
        List<String> fechas = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 30; i++) {
            int diaDeLaSemana = calendar.get(Calendar.DAY_OF_WEEK);
            // Ejemplo: no abrir sábados ni domingos.
            if (diaDeLaSemana != Calendar.SATURDAY && diaDeLaSemana != Calendar.SUNDAY) {
                fechas.add(sdf.format(calendar.getTime()));
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        fechasDisponibles.postValue(fechas);
    }

    private void actualizarHorasDisponibles() {
        Usuario barbero = barberoSeleccionado.getValue();
        String fecha = fechaSeleccionada.getValue();
        Servicio servicio = servicioSeleccionado.getValue();

        if (barbero == null || fecha == null || servicio == null) {
            horasDisponibles.postValue(new ArrayList<>());
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Calendar cal = Calendar.getInstance();
                int diaSemanaa = cal.get(Calendar.DAY_OF_WEEK);
                String diaSemana = obtenerDia(diaSemanaa);

                List<Horario> horarios = horarioRepo.getHorariosPorBarberoYDia(barbero.id, diaSemana);
                if (horarios == null) horarios = new ArrayList<>();

                List<String> horasOcupadas = turnoRepo.getHorasOcupadas(barbero.id, fecha);
                if (horasOcupadas == null) horasOcupadas = new ArrayList<>();

                List<String> disponibles = new ArrayList<>();
                int duracion = servicio.duracionMin;
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("H:mm");

                for (Horario h : horarios) {
                    LocalTime inicio = LocalTime.parse(h.horaInicio, fmt);
                    LocalTime fin = LocalTime.parse(h.horaFin, fmt);

                    while (!inicio.plusMinutes(duracion).isAfter(fin)) {
                        String horaStr = inicio.format(DateTimeFormatter.ofPattern("HH:mm"));

                        // Si la fecha es hoy, verificamos que la hora sea posterior a la actual
                        if (!horasOcupadas.contains(horaStr)) {
                            if (fecha.equals(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()))) {
                                LocalTime ahora = LocalTime.now();
                                if (inicio.isAfter(ahora)) { // Solo agregamos horas futuras
                                    disponibles.add(horaStr);
                                }
                            } else {
                                disponibles.add(horaStr); // Cualquier otra fecha
                            }
                        }

                        inicio = inicio.plusMinutes(duracion);
                    }
                }


                horasDisponibles.postValue(disponibles);
                Log.d("ReservaViewModel", "Horas disponibles generadas: " + disponibles);
                Log.d("ReservaViewModel", "Barbero: " + barbero.nombre + ", Fecha: " + fecha + ", Servicio: " + servicio.nombre);
                Log.d("ReservaViewModel", "Horarios: " + horarios);
                Log.d("ReservaViewModel", "Horas ocupadas: " + horasOcupadas);
                Log.d("ReservaViewModel", "Horas disponibles finales: " + disponibles);

            } catch (Exception e) {
                Log.e("ReservaViewModel", "Error en actualizarHorasDisponibles", e);
                horasDisponibles.postValue(new ArrayList<>());
            }
        });
    }


    private String obtenerDia(int diaSemanaa) {
        if(diaSemanaa == 1) return "Domingo";
        if(diaSemanaa == 2) return "Lunes";
        if(diaSemanaa == 3) return "Martes";
        if(diaSemanaa == 4) return "Miércoles";
        if(diaSemanaa == 5) return "Jueves";
        if(diaSemanaa == 6) return "Viernes";
        if(diaSemanaa == 7) return "Sábado";
        return "";
    }

    public int getClienteId() {
        SharedPreferences preferences = getApplication().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }
}
