package com.project.artconnect.model;

public class AppUser {

    private int userId;
    private String username;
    private String passwordHash;
    private String role;         // "USER" ou "ADMIN"
    private Integer memberId;

    // --- Constructeur vide (requis pour JDBC) ---
    public AppUser() {}

    // --- Constructeur complet ---
    public AppUser(int userId, String username, String passwordHash, String role, Integer memberId) {
        this.userId       = userId;
        this.username     = username;
        this.passwordHash = passwordHash;
        this.role         = role;
        this.memberId     = memberId;
    }

    // --- Getters ---
    public int     getUserId()       { return userId; }
    public String  getUsername()     { return username; }
    public String  getPasswordHash() { return passwordHash; }
    public String  getRole()         { return role; }
    public Integer getMemberId()     { return memberId; }

    // --- Setters ---
    public void setUserId(int userId)             { this.userId = userId; }
    public void setUsername(String username)       { this.username = username; }
    public void setPasswordHash(String hash)       { this.passwordHash = hash; }
    public void setRole(String role)               { this.role = role; }
    public void setMemberId(Integer memberId)      { this.memberId = memberId; }

    // --- Méthodes utilitaires ---
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    @Override
    public String toString() {
        return "AppUser{username='" + username + "', role='" + role + "'}";
    }
}