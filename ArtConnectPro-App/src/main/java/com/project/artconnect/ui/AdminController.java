package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ResourceBundle;

public class AdminController implements Initializable {

    // =========================================================
    // LISTVIEWS
    // =========================================================

    @FXML
    private ListView<String> artistListView;

    @FXML
    private ListView<String> artworkListView;

    @FXML
    private ListView<String> galleryListView;

    @FXML
    private ListView<String> memberListView;

    @FXML
    private ListView<String> exhibitionListView;

    // =========================================================
    // FORMULAIRE ARTISTE
    // =========================================================

    @FXML
    private TextField nameField;

    @FXML
    private TextArea bioField;

    @FXML
    private TextField birthYearField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField websiteField;

    @FXML
    private TextField socialField;

    @FXML
    private CheckBox activeCheckBox;


    // =========================================================
    // FORMULAIRE GALERIE
    // =========================================================

    @FXML
    private TextField galleryNameField;

    @FXML
    private TextField galleryAddressField;

    @FXML
    private TextField galleryOwnerField;

    @FXML
    private TextField galleryHoursField;


    @FXML
    private TextField galleryWebsiteField;

    
    @FXML
    private TextField galleryRatingField;

    @FXML
    private TextField galleryPhoneField;

    // =========================================================
    // INITIALISATION
    // =========================================================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadArtists();
        loadArtworks();
        loadGalleries();
        loadMembers();
        loadExhibitions();
    }

    // =========================================================
    // LOAD ARTISTS
    // =========================================================

    @FXML
    public void loadArtists() {

        artistListView.getItems().clear();

        String sql = "SELECT name FROM artist ORDER BY name";

        try (
                Connection connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

                PreparedStatement ps = connection.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                artistListView.getItems().add(
                        rs.getString("name")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Erreur chargement artistes",
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // LOAD ARTWORKS
    // =========================================================

    public void loadArtworks() {

        artworkListView.getItems().clear();

        String sql = "SELECT title FROM artwork";

        try (
                Connection connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

                PreparedStatement ps = connection.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                artworkListView.getItems().add(
                        rs.getString("title")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD GALLERIES
    // =========================================================

    public void loadGalleries() {

        galleryListView.getItems().clear();

        String sql = "SELECT name FROM gallery";

        try (
                Connection connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

                PreparedStatement ps = connection.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                galleryListView.getItems().add(
                        rs.getString("name")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD MEMBERS
    // =========================================================

    public void loadMembers() {

        memberListView.getItems().clear();

        String sql = "SELECT name FROM community_member";

        try (
                Connection connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

                PreparedStatement ps = connection.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                memberListView.getItems().add(
                        rs.getString("name")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD EXHIBITIONS
    // =========================================================

    public void loadExhibitions() {

        exhibitionListView.getItems().clear();

        String sql = "SELECT title FROM exhibition";

        try (
                Connection connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

                PreparedStatement ps = connection.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                exhibitionListView.getItems().add(
                        rs.getString("title")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // ADD ARTIST
    // =========================================================

    @FXML
    private void handleAddArtist() {

        String sql = """
            INSERT INTO artist
            (
                name,
                bio,
                birth_year,
                contact_email,
                phone,
                city,
                website,
                social_media,
                is_active
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, nameField.getText());
            ps.setString(2, bioField.getText());

            if (birthYearField.getText().isEmpty()) {

                ps.setNull(3, java.sql.Types.INTEGER);

            } else {

                ps.setInt(
                        3,
                        Integer.parseInt(birthYearField.getText())
                );
            }

            ps.setString(4, emailField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, cityField.getText());
            ps.setString(7, websiteField.getText());
            ps.setString(8, socialField.getText());

            ps.setBoolean(
                    9,
                    activeCheckBox.isSelected()
            );

            ps.executeUpdate();

            loadArtists();

            clearFields();

            System.out.println(
                    "Artiste ajouté avec succès."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Erreur ajout artiste",
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // ADD GALLERY
    // =========================================================

    @FXML
    private void handleAddGallery() {

        String sql = """
            INSERT INTO gallery
            (
                name,
                address,
                owner_name,
                opening_hours,
                contact_phone,
                rating,
                website
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection connection = DatabaseConfig.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, galleryNameField.getText());
            ps.setString(2, galleryAddressField.getText());
            ps.setString(3, galleryOwnerField.getText());
            ps.setString(4, galleryHoursField.getText());
            ps.setString(5, galleryPhoneField.getText());

            if (galleryRatingField.getText().isEmpty()) {

                ps.setNull(6, java.sql.Types.DECIMAL);

            } else {

                ps.setDouble(
                        6,
                        Double.parseDouble(galleryRatingField.getText())
                );
            }

            ps.setString(7, galleryWebsiteField.getText());

            ps.executeUpdate();

            loadGalleries();

            clearGalleryFields();

            showInfo(
                    "Galerie ajoutée",
                    "La galerie a été ajoutée avec succès."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Erreur ajout galerie",
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // UPDATE GALLERY
    // =========================================================

    @FXML
    private void handleUpdateGallery() {

        String selectedGallery =
                galleryListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedGallery == null) {

            showError(
                    "Aucune sélection",
                    "Veuillez sélectionner une galerie."
            );

            return;
        }

        String sql = """
            UPDATE gallery
            SET
                name = ?,
                address = ?,
                owner_name = ?,
                opening_hours = ?,
                contact_phone = ?,
                rating = ?,
                website = ?
            WHERE name = ?
        """;

        try (
                Connection connection = DatabaseConfig.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, galleryNameField.getText());
            ps.setString(2, galleryAddressField.getText());
            ps.setString(3, galleryOwnerField.getText());
            ps.setString(4, galleryHoursField.getText());
            ps.setString(5, galleryPhoneField.getText());

            if (galleryRatingField.getText().isEmpty()) {

                ps.setNull(6, java.sql.Types.DECIMAL);

            } else {

                ps.setDouble(
                        6,
                        Double.parseDouble(galleryRatingField.getText())
                );
            }

            ps.setString(7, galleryWebsiteField.getText());

            ps.setString(8, selectedGallery);

            ps.executeUpdate();

            loadGalleries();

            showInfo(
                    "Galerie modifiée",
                    "La galerie a été modifiée avec succès."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Erreur modification galerie",
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // DELETE GALLERY
    // =========================================================

    @FXML
    private void deleteGallery() {

        String selectedGallery =
                galleryListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedGallery == null) {

            showError(
                    "Aucune sélection",
                    "Veuillez sélectionner une galerie."
            );

            return;
        }

        String sql =
                "DELETE FROM gallery WHERE name = ?";

        try (
                Connection connection = DatabaseConfig.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, selectedGallery);

            ps.executeUpdate();

            loadGalleries();

            showInfo(
                    "Galerie supprimée",
                    "La galerie a été supprimée avec succès."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Erreur suppression galerie",
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // CLEAR GALLERY FORM
    // =========================================================

    private void clearGalleryFields() {

        galleryNameField.clear();
        galleryAddressField.clear();
        galleryOwnerField.clear();
        galleryHoursField.clear();
        galleryPhoneField.clear();
        galleryRatingField.clear();
        galleryWebsiteField.clear();
    }

    // =========================================================
    // DELETE ARTIST
    // =========================================================

    @FXML
    private void deleteArtist() {

        String selectedArtist =
                artistListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedArtist == null) {

            showError(
                    "Aucune sélection",
                    "Veuillez sélectionner un artiste."
            );

            return;
        }

        String sql =
                "DELETE FROM artist WHERE name = ?";

        try (
                Connection connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                );

                PreparedStatement ps =
                        connection.prepareStatement(sql)
        ) {

            ps.setString(1, selectedArtist);

            ps.executeUpdate();

            loadArtists();

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Erreur suppression",
                    e.getMessage()
            );
        }
    }

    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearFields() {

        nameField.clear();
        bioField.clear();
        birthYearField.clear();
        emailField.clear();
        phoneField.clear();
        cityField.clear();
        websiteField.clear();
        socialField.clear();

        activeCheckBox.setSelected(true);
    }

    // =========================================================
    // ALERTS
    // =========================================================

    private void showError(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    @FXML
    private void handleUpdateArtist() {

        String selectedArtist =
                artistListView.getSelectionModel().getSelectedItem();

        if (selectedArtist == null) {

            showError(
                    "Aucune sélection",
                    "Veuillez sélectionner un artiste à modifier."
            );

            return;
        }

        String sql = """
            UPDATE artist
            SET name = ?,
                bio = ?,
                birth_year = ?,
                contact_email = ?,
                phone = ?,
                city = ?,
                website = ?,
                social_media = ?,
                is_active = ?
            WHERE name = ?
        """;

        try (
                Connection connection = DatabaseConfig.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, nameField.getText());
            ps.setString(2, bioField.getText());

            if (birthYearField.getText().isEmpty()) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, Integer.parseInt(birthYearField.getText()));
            }

            ps.setString(4, emailField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, cityField.getText());
            ps.setString(7, websiteField.getText());
            ps.setString(8, socialField.getText());
            ps.setBoolean(9, activeCheckBox.isSelected());

            ps.setString(10, selectedArtist);

            ps.executeUpdate();

            loadArtists();

            showInfo(
                    "Modification réussie",
                    "L'artiste a été modifié avec succès."
            );

        } catch (Exception e) {

            e.printStackTrace();

            showError(
                    "Erreur modification",
                    e.getMessage()
            );
        }
    }

    private void showInfo(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}