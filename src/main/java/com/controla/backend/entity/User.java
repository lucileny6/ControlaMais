package com.controla.backend.entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID gerado automaticamente pelo banco

    @Column(unique = true, nullable = false)
    private String email;

    private String username;
    private String password;
    private boolean enabled;

    public User() {}

    public User(String email, String username, String password, boolean enabled) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    // Getters e setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
