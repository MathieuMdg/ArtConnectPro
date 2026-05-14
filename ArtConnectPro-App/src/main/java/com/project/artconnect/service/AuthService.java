package com.project.artconnect.service;

import org.mindrot.jbcrypt.BCrypt;
import com.project.artconnect.dao.UserDao;
import com.project.artconnect.model.AppUser;
import com.project.artconnect.session.UserSession;

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
    

    /** Déconnecte l'utilisateur courant. */
    public void logout() {

        this.currentUser = null;

        UserSession.logout();
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

    public boolean register(
            String name,
            String email,
            int birthYear,
            String phone,
            String city,
            String membership,
            String username,
            String password
    ) {

        try {

            return userDao.register(
                    name,
                    email,
                    birthYear,
                    phone,
                    city,
                    membership,
                    username,
                    password
            );

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    public boolean login(String username, String password) {

        Optional<AppUser> optionalUser =
                userDao.findByUsername(username);

        if(optionalUser.isEmpty()) {

            return false;
        }

        AppUser user = optionalUser.get();

        boolean validPassword =
                BCrypt.checkpw(
                        password,
                        user.getPasswordHash()
                );

        if(validPassword) {

            this.currentUser = user;

            UserSession.login(user);

            return true;
        }

        return false;
    }
}