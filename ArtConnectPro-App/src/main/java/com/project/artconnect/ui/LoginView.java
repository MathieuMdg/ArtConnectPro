package com.project.artconnect.ui;

import com.project.artconnect.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JDialog {

    private final AuthService authService;
    private boolean loginSuccessful = false;

    // Composants de l'interface
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

    public LoginView(AuthService authService) {
        this.authService = authService;
        buildUI();
    }

    private void buildUI() {
        setTitle("ArtConnect — Connexion");
        setSize(400, 250);
        setLocationRelativeTo(null); // centre la fenêtre
        setModal(true);              // bloque les autres fenêtres tant que login pas fait
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        JLabel titleLabel = new JLabel("Bienvenue sur ArtConnect", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Label "Nom d'utilisateur"
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Nom d'utilisateur :"), gbc);

        // Champ username
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Label "Mot de passe"
        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Mot de passe :"), gbc);

        // Champ mot de passe (masqué)
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Message d'erreur (invisible par défaut)
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(errorLabel, gbc);

        // Bouton de connexion
        loginButton = new JButton("Se connecter");
        gbc.gridy = 4;
        panel.add(loginButton, gbc);

        add(panel);

        // Action du bouton
        loginButton.addActionListener(e -> tentativeDeConnexion());

        // Permet aussi d'appuyer sur Entrée dans le champ password
        passwordField.addActionListener(e -> tentativeDeConnexion());
    }

    private void tentativeDeConnexion() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (authService.login(username, password)) {
            loginSuccessful = true;
            dispose(); // ferme la fenêtre de login
        } else {
            errorLabel.setText("Identifiants incorrects. Réessayez.");
            passwordField.setText(""); // efface le mot de passe
        }
    }

    /** Retourne true si la connexion a réussi. */
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}