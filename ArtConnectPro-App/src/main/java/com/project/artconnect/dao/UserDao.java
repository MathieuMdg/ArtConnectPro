package com.project.artconnect.dao;

import com.project.artconnect.model.AppUser;
import java.util.Optional;

public interface UserDao {
    /**
     * Cherche un utilisateur par son nom d'utilisateur.
     * Retourne Optional.empty() si aucun utilisateur trouvé.
     */
    Optional<AppUser> findByUsername(String username);

    boolean register(
            String name,
            String email,
            int birthYear,
            String phone,
            String city,
            String membership,
            String username,
            String password
    );
}