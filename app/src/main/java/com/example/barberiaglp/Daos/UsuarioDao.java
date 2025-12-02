package com.example.barberiaglp.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import com.example.barberiaglp.Modelos.Usuario;

@Dao
public interface UsuarioDao {
    @Insert
    void insert(Usuario usuario);

    @Insert
    long insertWithId(Usuario usuario); // Retorna el ID insertado

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password")
    Usuario login(String email, String password);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario findByEmail(String email);

    @Query("SELECT * FROM usuarios WHERE rol = :rol")
    List<Usuario> obtenerPorRol(String rol);

    @Query("SELECT * FROM usuarios")
    LiveData<List<Usuario>> getAllUsersAsLiveData();

    // temporal para las pruebas con la base de datos
    @Query("DELETE FROM usuarios")
    void deleteAll();

    @Update
    void update(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario findById(int id);

    // En Daos/UsuarioDao.java
    @Query("SELECT * FROM usuarios WHERE id IN (:userIds)")
    List<Usuario> getUsersByIds(List<Integer> userIds);

}
