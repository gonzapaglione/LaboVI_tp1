package com.example.barberiaglp.Repositorios;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.barberiaglp.Daos.BarberoServicioDao;
import com.example.barberiaglp.Daos.UsuarioDao;
import com.example.barberiaglp.Modelos.Usuario;
import com.example.barberiaglp.BD.AppDatabase; // La clase de tu base de datos
import com.example.barberiaglp.Network.ApiService;
import com.example.barberiaglp.Network.AuthResponse;
import com.example.barberiaglp.Network.LoginRequest;
import com.example.barberiaglp.Network.RegisterRequest;
import com.example.barberiaglp.Network.RetrofitClient;
import com.example.barberiaglp.Network.UpdateUserRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepositorio {
    private UsuarioDao usuarioDao;
    private Application application;
    private LiveData<List<Usuario>> allUsers; // Para observar la lista de usuarios
    private BarberoServicioDao barberoServicioDao;
    private ApiService apiService;

    // El constructor recibe la aplicación para poder obtener el contexto de forma
    // segura
    public UsuarioRepositorio(Application application) {
        this.application = application; // Guarda la aplicación
        AppDatabase db = AppDatabase.getInstance(application);
        usuarioDao = db.usuarioDao();
        barberoServicioDao = db.BarberoServicioDao();
        apiService = RetrofitClient.getInstance().getApiService();
    }

    // La inserción se ejecuta en un hilo secundario.
    public void insert(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.insert(usuario);
        });
    }

    // Método para registro usando API
    public void register(String nombre, String apellido, String email, String password, OnRegisterResult callback) {
        RegisterRequest registerRequest = new RegisterRequest(nombre, apellido, email, password);

        Call<AuthResponse> call = apiService.register(registerRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    // Convertir AuthResponse a Usuario
                    Usuario usuario = new Usuario();
                    usuario.id = authResponse.getId();
                    usuario.nombre = authResponse.getNombre();
                    usuario.apellido = authResponse.getApellido();
                    usuario.email = authResponse.getEmail();
                    usuario.rol = authResponse.getRol();
                    usuario.fechaRegistro = authResponse.getFechaRegistro();

                    callback.onResult(usuario, null);
                } else {
                    String errorMsg = "Error al registrar usuario";
                    if (response.code() == 400) {
                        errorMsg = "El correo electrónico ya está registrado";
                    }
                    callback.onResult(null, errorMsg);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onResult(null, "Error de conexión: " + t.getMessage());
            }
        });
    }

    // Interfaz para el callback del registro
    public interface OnRegisterResult {
        void onResult(Usuario usuario, String error);
    }

    // Para el login usando API
    public void login(String email, String password, OnLoginResult callback) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<AuthResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    // Convertir AuthResponse a Usuario
                    Usuario usuario = new Usuario();
                    usuario.id = authResponse.getId();
                    usuario.nombre = authResponse.getNombre();
                    usuario.apellido = authResponse.getApellido();
                    usuario.email = authResponse.getEmail();
                    usuario.rol = authResponse.getRol();
                    usuario.fechaRegistro = authResponse.getFechaRegistro();

                    callback.onResult(usuario);
                } else {
                    callback.onResult(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onResult(null);
            }
        });
    }

    // Interfaz para el callback del login
    public interface OnLoginResult {
        void onResult(Usuario usuario);
    }

    public interface GetUserCallback {
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

    // temporal para las pruebas con la base de datos
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

    public List<Usuario> getBarberosPorServicio(int servicioId) {
        // 1. Obtiene los IDs de los barberos que ofrecen el servicio
        List<Integer> barberoIds = barberoServicioDao.getBarberoIdsPorServicio(servicioId);

        // 2. Si la lista de IDs no está vacía, busca los objetos Usuario completos
        if (barberoIds != null && !barberoIds.isEmpty()) {
            return usuarioDao.getUsersByIds(barberoIds);
        } else {
            // Si ningún barbero ofrece ese servicio, devuelve una lista vacía
            return new ArrayList<>();
        }
    }

    public interface OnUserFoundCallback {
        void onUserFound(Usuario usuario);
    }

    public void findByEmail(String email, OnUserFoundCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Usuario usuario = usuarioDao.findByEmail(email);
            callback.onUserFound(usuario);
        });
    }

    // Obtener usuario desde la API por ID
    public void getUserFromApi(int userId, OnUserLoadedFromApi callback) {
        Call<AuthResponse> call = apiService.getUserById(userId);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    Usuario usuario = new Usuario();
                    usuario.id = authResponse.getId();
                    usuario.nombre = authResponse.getNombre();
                    usuario.apellido = authResponse.getApellido();
                    usuario.email = authResponse.getEmail();
                    usuario.password = authResponse.getPassword();
                    usuario.rol = authResponse.getRol();
                    usuario.fechaRegistro = authResponse.getFechaRegistro();

                    callback.onResult(usuario, null);
                } else {
                    callback.onResult(null, "Error al obtener datos del usuario");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onResult(null, "Error de conexión: " + t.getMessage());
            }
        });
    }

    // Actualizar usuario en la API
    public void updateUserInApi(int userId, String nombre, String apellido, String email, String password,
            OnUserUpdatedCallback callback) {
        UpdateUserRequest updateRequest = new UpdateUserRequest(nombre, apellido, email, password);

        Call<AuthResponse> call = apiService.updateUser(userId, updateRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    // Si la actualización fue exitosa (código 200-299)
                    // Crear un usuario con los datos enviados ya que se actualizó correctamente
                    Usuario usuario = new Usuario();
                    usuario.id = userId;
                    usuario.nombre = nombre;
                    usuario.apellido = apellido;
                    usuario.email = email;
                    usuario.rol = "Cliente";
                    usuario.fechaRegistro = "";

                    callback.onResult(usuario, null);
                } else {
                    callback.onResult(null, "Error al actualizar usuario: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Aunque falle el parseo, si se actualizó en la API, considerarlo exitoso
                // Crear usuario con los datos enviados
                Usuario usuario = new Usuario();
                usuario.id = userId;
                usuario.nombre = nombre;
                usuario.apellido = apellido;
                usuario.email = email;
                usuario.rol = "Cliente";
                usuario.fechaRegistro = "";

                callback.onResult(usuario, null);
            }
        });
    }

    // Interfaces para callbacks
    public interface OnUserLoadedFromApi {
        void onResult(Usuario usuario, String error);
    }

    public interface OnUserUpdatedCallback {
        void onResult(Usuario usuario, String error);
    }
}
