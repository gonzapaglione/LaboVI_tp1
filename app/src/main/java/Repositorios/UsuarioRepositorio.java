package Repositorios;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData; // Importante para observar cambios en la lista
import java.util.List;

import Daos.UsuarioDao;
import Modelos.Usuario;
import BD.AppDatabase; // La clase de tu base de datos
public class UsuarioRepositorio {
    private UsuarioDao usuarioDao;
    private Application application;
    private LiveData<List<Usuario>> allUsers; // Para observar la lista de usuarios

    // El constructor recibe la aplicación para poder obtener el contexto de forma segura
    public UsuarioRepositorio(Application application) {
        this.application = application; // Guarda la aplicación
        AppDatabase db = AppDatabase.getInstance(application);
        usuarioDao = db.usuarioDao();
    }

    // La inserción se ejecuta en un hilo secundario.
    public void insert(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.insert(usuario);
        });
    }

    // Para el login, también en un hilo secundario.
    public void login(String email, String password, OnLoginResult callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Usuario usuario = usuarioDao.login(email, password);
            callback.onResult(usuario);         // Usamos un Callback para devolver el resultado al hilo principal.
        });
    }

    // Interfaz para el callback del login
    public interface OnLoginResult {
        void onResult(Usuario usuario);
    }

    public interface GetUserCallback { // <-- 1. CREA UNA NUEVA INTERFAZ PARA DEVOLVER EL USUARIO
        void onUserLoaded(Usuario usuario);
    }

    public void getUsuarioActual(GetUserCallback callback) {
        // Ejecuta la lógica en un hilo secundario
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Lee el email desde SharedPreferences
            SharedPreferences preferences = application.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userEmail = preferences.getString("userEmail", null);

            Usuario usuario = null;
            if (userEmail != null) {
                // Busca el usuario en la BD usando el email
                usuario = usuarioDao.findByEmail(userEmail);
            }

            // Devuelve el resultado (el usuario encontrado o null) a través del callback
            callback.onUserLoaded(usuario);
        });
    }

    //temporal para las pruebas con la base de datos
    public void deleteAllUsers() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.deleteAll();
        });
    }

    public void update(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.update(usuario);
        });
    }
}
