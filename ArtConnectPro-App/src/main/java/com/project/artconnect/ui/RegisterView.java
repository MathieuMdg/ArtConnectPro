package com.project.artconnect.ui;

import com.project.artconnect.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterView extends JDialog {

    private final AuthService authService;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField birthYearField;
    private JTextField phoneField;
    private JTextField cityField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JComboBox<String> membershipBox;

    private JLabel messageLabel;

    public RegisterView(AuthService authService) {

        this.authService = authService;

        buildUI();
    }

    private void buildUI() {

        setTitle("Créer un compte");

        setSize(500, 620);

        setLocationRelativeTo(null);

        setModal(true);

        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBackground(new Color(245,245,245));

        panel.setBorder(new EmptyBorder(20,30,20,30));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Création de compte");

        title.setHorizontalAlignment(SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 24));

        title.setForeground(new Color(44,62,80));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;

        panel.add(new JLabel("Nom complet"), gbc);

        nameField = new JTextField();

        gbc.gridx = 1;

        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(new JLabel("Email"), gbc);

        emailField = new JTextField();

        gbc.gridx = 1;

        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(new JLabel("Année de naissance"), gbc);

        birthYearField = new JTextField();

        gbc.gridx = 1;

        panel.add(birthYearField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(new JLabel("Téléphone"), gbc);

        phoneField = new JTextField();

        gbc.gridx = 1;

        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(new JLabel("Ville"), gbc);

        cityField = new JTextField();

        gbc.gridx = 1;

        panel.add(cityField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(new JLabel("Type d'abonnement"), gbc);

        membershipBox = new JComboBox<>();

        membershipBox.addItem("Standard");
        membershipBox.addItem("Premium");
        membershipBox.addItem("Student");

        gbc.gridx = 1;

        panel.add(membershipBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(new JLabel("Nom d'utilisateur"), gbc);

        usernameField = new JTextField();

        gbc.gridx = 1;

        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(new JLabel("Mot de passe"), gbc);

        passwordField = new JPasswordField();

        gbc.gridx = 1;

        panel.add(passwordField, gbc);

        JButton registerButton = new JButton("Créer le compte");

        gbc.gridx = 0;
        gbc.gridy++;

        gbc.gridwidth = 2;

        panel.add(registerButton, gbc);

        messageLabel = new JLabel("");

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy++;

        panel.add(messageLabel, gbc);

        registerButton.addActionListener(e -> register());

        add(panel);
    }

    private void register() {

        try {

            String name = nameField.getText().trim();

            String email = emailField.getText().trim();

            int birthYear =
                    Integer.parseInt(
                            birthYearField.getText().trim()
                    );

            String phone = phoneField.getText().trim();

            String city = cityField.getText().trim();

            String membership =
                    membershipBox.getSelectedItem().toString();

            String username = usernameField.getText().trim();

            String password =
                    new String(passwordField.getPassword());

            if(name.isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()
                    || city.isEmpty()
                    || username.isEmpty()
                    || password.isEmpty()) {

                messageLabel.setForeground(Color.RED);

                messageLabel.setText(
                        "Tous les champs sont obligatoires."
                );

                return;
            }

            boolean success = authService.register(
                    name,
                    email,
                    birthYear,
                    phone,
                    city,
                    membership,
                    username,
                    password
            );

            if(success) {

                messageLabel.setForeground(
                        new Color(0,128,0)
                );

                messageLabel.setText(
                        "Compte créé avec succès."
                );

            } else {

                messageLabel.setForeground(Color.RED);

                messageLabel.setText(
                        "Erreur lors de la création."
                );
            }

        } catch (NumberFormatException e) {

            messageLabel.setForeground(Color.RED);

            messageLabel.setText(
                    "Année de naissance invalide."
            );
        }
    }
}