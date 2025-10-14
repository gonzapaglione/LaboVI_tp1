package Repositorios;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import android.app.Application;
import androidx.lifecycle.LiveData; // Importante para observar cambios en la lista
import java.util.List;

import Daos.UsuarioDao;
import Modelos.Usuario;
import BD.AppDatabase; // La clase de tu base de datos
public class UsuarioRepositorio {
        private UsuarioDao usuarioDao;
        private LiveData<List<Usuario>> allUsers; // Para observar la lista de usuarios

        // El constructor recibe la aplicación para poder obtener el contexto de forma segura
        public UsuarioRepositorio(Application application) {
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
}
