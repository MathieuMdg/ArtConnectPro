package com.project.artconnect;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.dao.UserDao;
import com.project.artconnect.persistence.JdbcUserDao;
import com.project.artconnect.service.AuthService;
import com.project.artconnect.ui.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Optional;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // =========================
        // CONNEXION BASE DE DONNEES
        // =========================

        Connection connection = DatabaseConfig.getConnection();

        UserDao userDao = new JdbcUserDao(connection);

        AuthService authService = new AuthService(userDao);

        // =========================
        // LOGIN USERNAME
        // =========================

        TextInputDialog userDialog = new TextInputDialog();

        userDialog.setTitle("Connexion");
        userDialog.setHeaderText("Connexion ArtConnect");
        userDialog.setContentText("Nom d'utilisateur :");

        Optional<String> usernameResult = userDialog.showAndWait();

        if (usernameResult.isEmpty()) {
            System.exit(0);
        }

        String username = usernameResult.get();

        // =========================
        // LOGIN PASSWORD
        // =========================

        TextInputDialog passwordDialog = new TextInputDialog();

        passwordDialog.setTitle("Connexion");
        passwordDialog.setHeaderText("Connexion ArtConnect");
        passwordDialog.setContentText("Mot de passe :");

        Optional<String> passwordResult = passwordDialog.showAndWait();

        if (passwordResult.isEmpty()) {
            System.exit(0);
        }

        String password = passwordResult.get();

        // =========================
        // AUTHENTIFICATION
        // =========================

        boolean success = authService.login(username, password);

        if (!success) {

            System.out.println("Identifiants incorrects.");

            System.exit(0);
        }

        // =========================
        // CHARGEMENT MAIN VIEW
        // =========================

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/project/artconnect/ui/MainView.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);
        MainController controller = loader.getController();

        controller.setAuthService(authService);

        stage.setTitle("ArtConnect Pro - Local Art Community Platform");

        stage.setScene(scene);

        stage.show();

        // =========================
        // DEBUG ROLE
        // =========================

        if (authService.isAdmin()) {
            System.out.println("Connection en tant que ADMIN");
        } else {
            System.out.println("Connection en tant que USER");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}