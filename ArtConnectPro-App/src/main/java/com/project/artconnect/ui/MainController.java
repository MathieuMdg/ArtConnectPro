package com.project.artconnect.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.application.Platform;
import com.project.artconnect.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;

public class MainController {
    private AuthService authService;

    @FXML
    private TabPane mainTabPane;

    @FXML
    public void initialize() {
        // Initialization logic if needed
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private Menu adminMenu;

    public void setAuthService(AuthService authService) {

        this.authService = authService;

        updateAdminUI();
    }

    private void updateAdminUI() {

        if (authService == null) {
            return;
        }

        adminMenu.setVisible(authService.isAdmin());
    }
}
