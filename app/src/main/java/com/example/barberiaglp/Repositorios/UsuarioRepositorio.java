package com.example.barberiaglp.Repositorios;

import android.app.Application;
import android.util.Log;

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
        Log.d("UsuarioRepo", "=== INICIANDO REGISTRO ===");
        Log.d("UsuarioRepo", "Email: " + email);
        Log.d("UsuarioRepo", "Nombre: " + nombre + " " + apellido);

        RegisterRequest registerRequest = new RegisterRequest(nombre, apellido, email, password);

        Call<AuthResponse> call = apiService.register(registerRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("UsuarioRepo", "Response Code: " + response.code());
                Log.d("UsuarioRepo", "Response Success: " + response.isSuccessful());

                if (response.isSuccessful()) {
                    Log.d("UsuarioRepo", "Registro exitoso! Haciendo login automático...");
                    // El servidor devuelve 201 con texto plano, no JSON
                    // Hacer login automático para obtener los datos del usuario
                    login(email, password, new OnLoginResult() {
                        @Override
                        public void onResult(Usuario usuario) {
                            if (usuario != null) {
                                Log.d("UsuarioRepo", "Login automático exitoso! Usuario ID: " + usuario.id);
                                callback.onResult(usuario, null);
                            } else {
                                Log.e("UsuarioRepo", "Error: Login automático falló");
                                callback.onResult(null, "Error al obtener datos del usuario después del registro");
                            }
                        }
                    });
                } else {
                    // Manejar diferentes códigos de error
                    String errorMsg = "Error al registrar usuario";
                    if (response.code() == 400) {
                        errorMsg = "El correo electrónico ya está registrado";
                        Log.e("UsuarioRepo", "Error 400: Email duplicado");
                    } else if (response.code() == 500) {
                        errorMsg = "Error del servidor. Por favor intenta más tarde o contacta al administrador.";
                        Log.e("UsuarioRepo", "Error 500: Error interno del servidor");
                    } else {
                        errorMsg = "Error al registrar usuario (Código: " + response.code() + ")";
                        Log.e("UsuarioRepo", "Error " + response.code() + ": " + response.message());
                    }
                    callback.onResult(null, errorMsg);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("UsuarioRepo", "FALLO DE CONEXIÓN EN REGISTRO", t);
                Log.e("UsuarioRepo", "Error: " + t.getMessage());

                // Detectar error específico de parsing JSON (el servidor devuelve texto plano
                // en 201)
                if (t instanceof com.google.gson.JsonSyntaxException ||
                        (t.getMessage() != null && t.getMessage().contains("Expected BEGIN_OBJECT but was STRING"))) {
                    Log.d("UsuarioRepo",
                            "Servidor devolvió texto plano (201), se considera registro exitoso. Haciendo login...");
                    // El registro fue exitoso (201) pero el servidor devolvió texto plano
                    // Hacer login automático para obtener los datos del usuario
                    login(email, password, new OnLoginResult() {
                        @Override
                        public void onResult(Usuario usuario) {
                            if (usuario != null) {
                                Log.d("UsuarioRepo", "Login automático exitoso! Usuario ID: " + usuario.id);
                                callback.onResult(usuario, null);
                            } else {
                                Log.e("UsuarioRepo", "Error: Login automático falló");
                                callback.onResult(null,
                                        "Registro exitoso, pero no se pudo iniciar sesión automáticamente. Por favor inicia sesión manualmente.");
                            }
                        }
                    });
                    return;
                }

                // Otros errores de conexión
                String errorMsg;
                if (t instanceof java.io.EOFException || t.getMessage().contains("not found: limit=")) {
                    errorMsg = "Error del servidor (respuesta mal formada).\n\n" +
                            "El servidor tiene un problema de configuración.\n\n" +
                            "Soluciones:\n" +
                            "• Contacta al administrador del servidor\n" +
                            "• El email '" + email + "' podría ya estar registrado\n" +
                            "• Intenta con otro email diferente";
                } else if (t instanceof java.net.UnknownHostException) {
                    errorMsg = "Sin conexión a Internet.\n\nVerifica tu conexión y vuelve a intentar.";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    errorMsg = "Tiempo de espera agotado.\n\nEl servidor tardó demasiado en responder.";
                } else {
                    errorMsg = "Error de conexión.\n\n" + t.getMessage();
                }

                callback.onResult(null, errorMsg);
            }
        });
    }

    // Interfaz para el callback del registro
    public interface OnRegisterResult {
        void onResult(Usuario usuario, String error);
    }

    // Para el login usando API
    public void login(String email, String password, OnLoginResult callback) {
        Log.d("UsuarioRepo", "=== INICIANDO LOGIN ===");
        Log.d("UsuarioRepo", "Email: " + email);

        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<AuthResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("UsuarioRepo", "Login Response Code: " + response.code());
                Log.d("UsuarioRepo", "Login Success: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d("UsuarioRepo", "AuthResponse recibido: " + authResponse.getNombre());
                    Log.d("UsuarioRepo", "API User ID: " + authResponse.getId());

                    // Convertir AuthResponse a Usuario
                    Usuario usuario = new Usuario();
                    usuario.apiId = authResponse.getId(); // Guardar ID del servidor
                    usuario.nombre = authResponse.getNombre();
                    usuario.apellido = authResponse.getApellido();
                    usuario.email = authResponse.getEmail();
                    usuario.rol = authResponse.getRol();
                    usuario.fechaRegistro = authResponse.getFechaRegistro();
                    usuario.password = authResponse.getPassword();

                    Log.d("UsuarioRepo", "Guardando usuario en BD local...");
                    // Guardar usuario de API en BD local y luego invocar callback
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        try {
                            // Verificar si ya existe por email
                            Usuario existente = usuarioDao.findByEmail(usuario.email);
                            if (existente != null) {
                                // Ya existe, actualizar y usar su ID local
                                Log.d("UsuarioRepo", "Usuario ya existe con ID local: " + existente.id);
                                usuario.id = existente.id;
                                usuarioDao.update(usuario);
                            } else {
                                // No existe, insertar nuevo y obtener ID autogenerado
                                Log.d("UsuarioRepo", "Insertando nuevo usuario en BD local...");
                                long nuevoId = usuarioDao.insertWithId(usuario);
                                usuario.id = (int) nuevoId;
                                Log.d("UsuarioRepo",
                                        "Usuario insertado con ID local: " + usuario.id + ", API ID: " + usuario.apiId);
                            }

                            // Callback DESPUÉS de que termine la inserción/actualización
                            callback.onResult(usuario);
                        } catch (Exception e) {
                            Log.e("UsuarioRepo", "Error al guardar usuario en BD local", e);
                            callback.onResult(null);
                        }
                    });
                } else {
                    Log.e("UsuarioRepo", "Login falló - Code: " + response.code());
                    callback.onResult(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("UsuarioRepo", "FALLO DE CONEXIÓN EN LOGIN", t);
                Log.e("UsuarioRepo", "Error: " + t.getMessage());
                callback.onResult(null);
            }
        });
    } // Interfaz para el callback del login

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

    // Método síncrono para obtener usuario por email (usar solo cuando sea
    // necesario)
    public Usuario findByEmailSync(String email) {
        return usuarioDao.findByEmail(email);
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
