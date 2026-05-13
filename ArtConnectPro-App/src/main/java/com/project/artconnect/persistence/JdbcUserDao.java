package com.project.artconnect.persistence;

import com.project.artconnect.dao.UserDao;
import com.project.artconnect.model.AppUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserDao implements UserDao {

    private final Connection connection;

    // On injecte la connexion via le constructeur
    public JdbcUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        // La requête SQL qui cherche l'utilisateur par son username
        String sql = "SELECT user_id, username, password, role, member_id " +
                     "FROM app_user WHERE username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username); // le ? est remplacé par le username

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // s'il y a un résultat
                    AppUser user = new AppUser();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    // getObject retourne null si la colonne est NULL en base
                    int memberId = rs.getInt("member_id");
                    user.setMemberId(rs.wasNull() ? null : memberId);

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur JdbcUserDao.findByUsername : " + e.getMessage());
        }

        return Optional.empty(); // aucun utilisateur trouvé
    }
}