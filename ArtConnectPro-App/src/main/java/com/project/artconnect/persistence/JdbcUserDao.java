package com.project.artconnect.persistence;

import com.project.artconnect.dao.UserDao;
import com.project.artconnect.model.AppUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.PreparedStatement;

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

    @Override
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

            String memberQuery = """
                INSERT INTO community_member
                (
                    name,
                    email,
                    birth_year,
                    phone,
                    city,
                    membership_type
                )
                VALUES (?,?,?,?,?,?)
            """;

            PreparedStatement memberStmt =
                    connection.prepareStatement(
                            memberQuery,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            memberStmt.setString(1, name);

            memberStmt.setString(2, email);

            memberStmt.setInt(3, birthYear);

            memberStmt.setString(4, phone);

            memberStmt.setString(5, city);

            memberStmt.setString(6, membership);

            memberStmt.executeUpdate();

            var generatedKeys =
                    memberStmt.getGeneratedKeys();

            int memberId = -1;

            if(generatedKeys.next()) {

                memberId = generatedKeys.getInt(1);
            }

            String hashedPassword =
                    BCrypt.hashpw(password, BCrypt.gensalt());

            String userQuery = """
                INSERT INTO app_user
                (
                    username,
                    password,
                    role,
                    member_id
                )
                VALUES (?,?,?,?)
            """;

            PreparedStatement userStmt =
                    connection.prepareStatement(userQuery);

            userStmt.setString(1, username);

            userStmt.setString(2, hashedPassword);

            userStmt.setString(3, "USER");

            userStmt.setInt(4, memberId);

            userStmt.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}