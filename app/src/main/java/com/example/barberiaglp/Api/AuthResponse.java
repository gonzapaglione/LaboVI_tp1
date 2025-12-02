package com.example.barberiaglp.Network;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("isActive")
    private boolean isActive;

    // Getters
    public String getToken() {
        return null; // La API no devuelve token
    }

    public int getId() {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getNombre() {
        return firstName;
    }

    public String getApellido() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return "Cliente"; // Rol por defecto
    }

    public String getFechaRegistro() {
        return ""; // La API no devuelve este campo
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
