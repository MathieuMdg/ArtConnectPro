package com.project.artconnect.service;

import org.mindrot.jbcrypt.BCrypt;
import com.project.artconnect.dao.UserDao;
import com.project.artconnect.model.AppUser;

import java.util.Optional;

public class AuthService {

    private final UserDao userDao;
    private AppUser currentUser; // l'utilisateur connecté (null si personne)

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Tente de connecter un utilisateur.
     * @return true si le login est réussi, false sinon.
     */
    public boolean login(String username, String password) {
        Optional<AppUser> result = userDao.findByUsername(username);

        if (result.isPresent()) {
            AppUser user = result.get();
            // BCrypt.checkpw compare le mot de passe tapé avec le hash stocké
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                this.currentUser = user;
                return true;
            }
        }
        return false;
    }

    /** Déconnecte l'utilisateur courant. */
    public void logout() {
        this.currentUser = null;
    }

    /** Retourne l'utilisateur actuellement connecté (peut être null). */
    public AppUser getCurrentUser() {
        return currentUser;
    }

    /** Retourne true si l'utilisateur connecté est un admin. */
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    /** Retourne true si quelqu'un est connecté. */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}