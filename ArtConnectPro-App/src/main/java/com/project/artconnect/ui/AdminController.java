package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    // =========================================================
    // LISTVIEWS
    // =========================================================

    @FXML private ListView<String> artistListView;
    @FXML private ListView<String> artworkListView;
    @FXML private ListView<String> galleryListView;
    @FXML private ListView<String> memberListView;
    @FXML private ListView<String> exhibitionListView;
    @FXML private ListView<String> workshopListView;

    // =========================================================
    // FORMULAIRE ARTISTE
    // =========================================================

    @FXML private TextField nameField;
    @FXML private TextArea  bioField;
    @FXML private TextField birthYearField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField websiteField;
    @FXML private TextField socialField;
    @FXML private CheckBox  activeCheckBox;

    // =========================================================
    // FORMULAIRE OEUVRE
    // =========================================================

    @FXML private TextField        artworkTitleField;
    @FXML private TextField        artworkYearField;
    @FXML private TextField        artworkTypeField;
    @FXML private TextField        artworkMediumField;
    @FXML private TextField        artworkDimensionsField;
    @FXML private TextArea         artworkDescriptionField;
    @FXML private TextField        artworkPriceField;
    @FXML private ComboBox<String> artworkStatusComboBox;
    @FXML private ComboBox<String> artworkArtistComboBox;

    // =========================================================
    // FORMULAIRE GALERIE
    // =========================================================

    @FXML private TextField galleryNameField;
    @FXML private TextField galleryAddressField;
    @FXML private TextField galleryOwnerField;
    @FXML private TextField galleryHoursField;
    @FXML private TextField galleryPhoneField;
    @FXML private TextField galleryRatingField;
    @FXML private TextField galleryWebsiteField;

    // =========================================================
    // FORMULAIRE MEMBRE
    // =========================================================

    @FXML private TextField memberNameField;
    @FXML private TextField memberEmailField;
    @FXML private TextField memberBirthYearField;
    @FXML private TextField memberPhoneField;
    @FXML private TextField memberCityField;
    @FXML private TextField memberMembershipField;

    // =========================================================
    // FORMULAIRE EXPOSITION
    // =========================================================

    @FXML private TextField        exhibitionTitleField;
    @FXML private DatePicker       exhibitionStartDateField;
    @FXML private DatePicker       exhibitionEndDateField;
    @FXML private TextArea         exhibitionDescriptionField;
    @FXML private TextField        exhibitionCuratorField;
    @FXML private TextField        exhibitionThemeField;
    @FXML private ComboBox<String> exhibitionGalleryComboBox;

    // =========================================================
    // FORMULAIRE WORKSHOP
    // =========================================================

    @FXML private TextField        workshopTitleField;
    @FXML private DatePicker       workshopDateField;
    @FXML private TextField        workshopTimeField;
    @FXML private TextField        workshopDurationField;
    @FXML private TextField        workshopMaxParticipantsField;
    @FXML private TextField        workshopPriceField;
    @FXML private ComboBox<String> workshopArtistComboBox;
    @FXML private TextField        workshopLocationField;
    @FXML private TextArea         workshopDescriptionField;
    @FXML private ComboBox<String> workshopLevelComboBox;

    // =========================================================
    // INITIALISATION
    // =========================================================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Chargement de toutes les listes
        loadArtists();
        loadArtworks();
        loadGalleries();
        loadMembers();
        loadExhibitions();
        loadWorkshops();

        // Chargement des menus déroulants (ComboBox)
        loadArtworkArtists();
        loadExhibitionGalleries();
        loadWorkshopArtists();

        // Valeurs fixes pour les ComboBox
        artworkStatusComboBox.getItems().addAll("FOR_SALE", "SOLD", "EXHIBITED");
        workshopLevelComboBox.getItems().addAll("Beginner", "Intermediate", "Advanced");
    }

    // =========================================================
    // HELPER : OBTENIR UNE CONNEXION
    // =========================================================

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD
        );
    }

    // =========================================================
    // HELPER : OBTENIR L'ID D'UN ARTISTE PAR SON NOM
    // =========================================================

    private int getArtistIdByName(String artistName) {

        String sql = "SELECT artist_id FROM artist WHERE name = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, artistName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("artist_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // =========================================================
    // HELPER : OBTENIR L'ID D'UNE GALERIE PAR SON NOM
    // =========================================================

    private int getGalleryIdByName(String galleryName) {

        String sql = "SELECT gallery_id FROM gallery WHERE name = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, galleryName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("gallery_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // =========================================================
    // CHARGEMENT DES COMBOBOX
    // =========================================================

    private void loadArtworkArtists() {

        artworkArtistComboBox.getItems().clear();

        String sql = "SELECT name FROM artist ORDER BY name";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                artworkArtistComboBox.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExhibitionGalleries() {

        exhibitionGalleryComboBox.getItems().clear();

        String sql = "SELECT name FROM gallery ORDER BY name";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                exhibitionGalleryComboBox.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWorkshopArtists() {

        workshopArtistComboBox.getItems().clear();

        String sql = "SELECT name FROM artist ORDER BY name";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                workshopArtistComboBox.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD ARTISTS
    // =========================================================

    @FXML
    public void loadArtists() {

        artistListView.getItems().clear();

        String sql = "SELECT name FROM artist ORDER BY name";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                artistListView.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur chargement artistes", e.getMessage());
        }
    }

    // =========================================================
    // ADD ARTIST
    // =========================================================

    @FXML
    private void handleAddArtist() {

        String sql = """
            INSERT INTO artist
            (name, bio, birth_year, contact_email, phone, city, website, social_media, is_active)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nameField.getText());
            ps.setString(2, bioField.getText());

            if (birthYearField.getText().isEmpty()) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, Integer.parseInt(birthYearField.getText()));
            }

            ps.setString(4, emailField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, cityField.getText());
            ps.setString(7, websiteField.getText());
            ps.setString(8, socialField.getText());
            ps.setBoolean(9, activeCheckBox.isSelected());

            ps.executeUpdate();

            loadArtists();
            loadArtworkArtists();
            loadWorkshopArtists();
            clearArtistFields();

            showInfo("Artiste ajouté", "L'artiste a été ajouté avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur ajout artiste", e.getMessage());
        }
    }

    // =========================================================
    // UPDATE ARTIST
    // =========================================================

    @FXML
    private void handleUpdateArtist() {

        String selected = artistListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner un artiste à modifier.");
            return;
        }

        String sql = """
            UPDATE artist
            SET name=?, bio=?, birth_year=?, contact_email=?, phone=?, city=?, website=?, social_media=?, is_active=?
            WHERE name=?
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nameField.getText());
            ps.setString(2, bioField.getText());

            if (birthYearField.getText().isEmpty()) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, Integer.parseInt(birthYearField.getText()));
            }

            ps.setString(4, emailField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, cityField.getText());
            ps.setString(7, websiteField.getText());
            ps.setString(8, socialField.getText());
            ps.setBoolean(9, activeCheckBox.isSelected());
            ps.setString(10, selected);

            ps.executeUpdate();

            loadArtists();
            loadArtworkArtists();
            loadWorkshopArtists();

            showInfo("Artiste modifié", "L'artiste a été modifié avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur modification artiste", e.getMessage());
        }
    }

    // =========================================================
    // DELETE ARTIST
    // =========================================================

    @FXML
    private void deleteArtist() {

        String selected = artistListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner un artiste.");
            return;
        }

        String sql = "DELETE FROM artist WHERE name = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, selected);
            ps.executeUpdate();

            loadArtists();
            loadArtworkArtists();
            loadWorkshopArtists();

            showInfo("Artiste supprimé", "L'artiste a été supprimé avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            handleDeleteError(e, "artiste");
        }
    }

    // =========================================================
    // CLEAR ARTIST FORM
    // =========================================================

    private void clearArtistFields() {
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

    // Alias conservé pour compatibilité avec le code existant
    private void clearFields() {
        clearArtistFields();
    }

    // =========================================================
    // LOAD ARTWORKS
    // =========================================================

    @FXML
    public void loadArtworks() {

        artworkListView.getItems().clear();

        String sql = "SELECT title FROM artwork ORDER BY title";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                artworkListView.getItems().add(rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur chargement œuvres", e.getMessage());
        }
    }

    // =========================================================
    // ADD ARTWORK
    // =========================================================

    @FXML
    private void handleAddArtwork() {

        String sql = """
            INSERT INTO artwork
            (title, creation_year, type, medium, dimensions, description, price, status, artist_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, artworkTitleField.getText());

            if (artworkYearField.getText().isEmpty()) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, Integer.parseInt(artworkYearField.getText()));
            }

            ps.setString(3, artworkTypeField.getText());
            ps.setString(4, artworkMediumField.getText());
            ps.setString(5, artworkDimensionsField.getText());
            ps.setString(6, artworkDescriptionField.getText());

            if (artworkPriceField.getText().isEmpty()) {
                ps.setNull(7, Types.DECIMAL);
            } else {
                ps.setBigDecimal(7, new BigDecimal(artworkPriceField.getText()));
            }

            ps.setString(8, artworkStatusComboBox.getValue());
            ps.setInt(9, getArtistIdByName(artworkArtistComboBox.getValue()));

            ps.executeUpdate();

            loadArtworks();
            clearArtworkFields();

            showInfo("Œuvre ajoutée", "L'œuvre a été ajoutée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur ajout œuvre", e.getMessage());
        }
    }

    // =========================================================
    // UPDATE ARTWORK
    // =========================================================

    @FXML
    private void handleUpdateArtwork() {

        String selected = artworkListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner une œuvre à modifier.");
            return;
        }

        String sql = """
            UPDATE artwork
            SET title=?, creation_year=?, type=?, medium=?, dimensions=?, description=?, price=?, status=?, artist_id=?
            WHERE title=?
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, artworkTitleField.getText());

            if (artworkYearField.getText().isEmpty()) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, Integer.parseInt(artworkYearField.getText()));
            }

            ps.setString(3, artworkTypeField.getText());
            ps.setString(4, artworkMediumField.getText());
            ps.setString(5, artworkDimensionsField.getText());
            ps.setString(6, artworkDescriptionField.getText());

            if (artworkPriceField.getText().isEmpty()) {
                ps.setNull(7, Types.DECIMAL);
            } else {
                ps.setBigDecimal(7, new BigDecimal(artworkPriceField.getText()));
            }

            ps.setString(8, artworkStatusComboBox.getValue());
            ps.setInt(9, getArtistIdByName(artworkArtistComboBox.getValue()));
            ps.setString(10, selected);

            ps.executeUpdate();

            loadArtworks();

            showInfo("Œuvre modifiée", "L'œuvre a été modifiée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur modification œuvre", e.getMessage());
        }
    }

    // =========================================================
    // DELETE ARTWORK
    // =========================================================

    @FXML
    private void deleteArtwork() {

        String selected = artworkListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner une œuvre.");
            return;
        }

        String sql = "DELETE FROM artwork WHERE title = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, selected);
            ps.executeUpdate();

            loadArtworks();

            showInfo("Œuvre supprimée", "L'œuvre a été supprimée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            handleDeleteError(e, "oeuvre");
        }
    }

    // =========================================================
    // CLEAR ARTWORK FORM
    // =========================================================

    private void clearArtworkFields() {
        artworkTitleField.clear();
        artworkYearField.clear();
        artworkTypeField.clear();
        artworkMediumField.clear();
        artworkDimensionsField.clear();
        artworkDescriptionField.clear();
        artworkPriceField.clear();
        artworkStatusComboBox.setValue(null);
        artworkArtistComboBox.setValue(null);
    }

    // =========================================================
    // LOAD GALLERIES
    // =========================================================

    @FXML
    public void loadGalleries() {

        galleryListView.getItems().clear();

        String sql = "SELECT name FROM gallery ORDER BY name";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                galleryListView.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur chargement galeries", e.getMessage());
        }
    }

    // =========================================================
    // ADD GALLERY
    // =========================================================

    @FXML
    private void handleAddGallery() {

        String sql = """
            INSERT INTO gallery
            (name, address, owner_name, opening_hours, contact_phone, rating, website)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, galleryNameField.getText());
            ps.setString(2, galleryAddressField.getText());
            ps.setString(3, galleryOwnerField.getText());
            ps.setString(4, galleryHoursField.getText());
            ps.setString(5, galleryPhoneField.getText());

            if (galleryRatingField.getText().isEmpty()) {
                ps.setNull(6, Types.DECIMAL);
            } else {
                ps.setDouble(6, Double.parseDouble(galleryRatingField.getText()));
            }

            ps.setString(7, galleryWebsiteField.getText());

            ps.executeUpdate();

            loadGalleries();
            loadExhibitionGalleries();
            clearGalleryFields();

            showInfo("Galerie ajoutée", "La galerie a été ajoutée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur ajout galerie", e.getMessage());
        }
    }

    // =========================================================
    // UPDATE GALLERY
    // =========================================================

    @FXML
    private void handleUpdateGallery() {

        String selected = galleryListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner une galerie.");
            return;
        }

        String sql = """
            UPDATE gallery
            SET name=?, address=?, owner_name=?, opening_hours=?, contact_phone=?, rating=?, website=?
            WHERE name=?
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, galleryNameField.getText());
            ps.setString(2, galleryAddressField.getText());
            ps.setString(3, galleryOwnerField.getText());
            ps.setString(4, galleryHoursField.getText());
            ps.setString(5, galleryPhoneField.getText());

            if (galleryRatingField.getText().isEmpty()) {
                ps.setNull(6, Types.DECIMAL);
            } else {
                ps.setDouble(6, Double.parseDouble(galleryRatingField.getText()));
            }

            ps.setString(7, galleryWebsiteField.getText());
            ps.setString(8, selected);

            ps.executeUpdate();

            loadGalleries();
            loadExhibitionGalleries();

            showInfo("Galerie modifiée", "La galerie a été modifiée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur modification galerie", e.getMessage());
        }
    }

    // =========================================================
    // DELETE GALLERY
    // =========================================================

    @FXML
    private void deleteGallery() {

        String selected = galleryListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner une galerie.");
            return;
        }

        String sql = "DELETE FROM gallery WHERE name = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, selected);
            ps.executeUpdate();

            loadGalleries();
            loadExhibitionGalleries();

            showInfo("Galerie supprimée", "La galerie a été supprimée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            handleDeleteError(e, "galerie");
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
    // LOAD MEMBERS
    // =========================================================

    @FXML
    public void loadMembers() {

        memberListView.getItems().clear();

        String sql = "SELECT name FROM community_member ORDER BY name";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                memberListView.getItems().add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur chargement membres", e.getMessage());
        }
    }

    // =========================================================
    // ADD MEMBER
    // =========================================================

    @FXML
    private void handleAddMember() {

        String sql = """
            INSERT INTO community_member
            (name, email, birth_year, phone, city, membership_type)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, memberNameField.getText());
            ps.setString(2, memberEmailField.getText());

            if (memberBirthYearField.getText().isEmpty()) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, Integer.parseInt(memberBirthYearField.getText()));
            }

            ps.setString(4, memberPhoneField.getText());
            ps.setString(5, memberCityField.getText());
            ps.setString(6, memberMembershipField.getText());

            ps.executeUpdate();

            loadMembers();
            clearMemberFields();

            showInfo("Membre ajouté", "Le membre a été ajouté avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur ajout membre", e.getMessage());
        }
    }

    // =========================================================
    // UPDATE MEMBER
    // =========================================================

    @FXML
    private void handleUpdateMember() {

        String selected = memberListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner un membre à modifier.");
            return;
        }

        String sql = """
            UPDATE community_member
            SET name=?, email=?, birth_year=?, phone=?, city=?, membership_type=?
            WHERE name=?
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, memberNameField.getText());
            ps.setString(2, memberEmailField.getText());

            if (memberBirthYearField.getText().isEmpty()) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, Integer.parseInt(memberBirthYearField.getText()));
            }

            ps.setString(4, memberPhoneField.getText());
            ps.setString(5, memberCityField.getText());
            ps.setString(6, memberMembershipField.getText());
            ps.setString(7, selected);

            ps.executeUpdate();

            loadMembers();

            showInfo("Membre modifié", "Le membre a été modifié avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur modification membre", e.getMessage());
        }
    }

    // =========================================================
    // DELETE MEMBER
    // =========================================================

    @FXML
    private void deleteMember() {

        String selected = memberListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner un membre.");
            return;
        }

        String sql = "DELETE FROM community_member WHERE name = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, selected);
            ps.executeUpdate();

            loadMembers();

            showInfo("Membre supprimé", "Le membre a été supprimé avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            handleDeleteError(e, "membre"); 
        }
    }

    // =========================================================
    // CLEAR MEMBER FORM
    // =========================================================

    private void clearMemberFields() {
        memberNameField.clear();
        memberEmailField.clear();
        memberBirthYearField.clear();
        memberPhoneField.clear();
        memberCityField.clear();
        memberMembershipField.clear();
    }

    // =========================================================
    // LOAD EXHIBITIONS
    // =========================================================

    @FXML
    public void loadExhibitions() {

        exhibitionListView.getItems().clear();

        String sql = "SELECT title FROM exhibition ORDER BY title";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                exhibitionListView.getItems().add(rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur chargement expositions", e.getMessage());
        }
    }

    // =========================================================
    // ADD EXHIBITION
    // =========================================================

    @FXML
    private void handleAddExhibition() {

        String sql = """
            INSERT INTO exhibition
            (title, start_date, end_date, description, curator_name, theme, gallery_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, exhibitionTitleField.getText());
            ps.setDate(2, Date.valueOf(exhibitionStartDateField.getValue()));

            if (exhibitionEndDateField.getValue() == null) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(exhibitionEndDateField.getValue()));
            }

            ps.setString(4, exhibitionDescriptionField.getText());
            ps.setString(5, exhibitionCuratorField.getText());
            ps.setString(6, exhibitionThemeField.getText());
            ps.setInt(7, getGalleryIdByName(exhibitionGalleryComboBox.getValue()));

            ps.executeUpdate();

            loadExhibitions();
            clearExhibitionFields();

            showInfo("Exposition ajoutée", "L'exposition a été ajoutée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur ajout exposition", e.getMessage());
        }
    }

    // =========================================================
    // UPDATE EXHIBITION
    // =========================================================

    @FXML
    private void handleUpdateExhibition() {

        String selected = exhibitionListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner une exposition à modifier.");
            return;
        }

        String sql = """
            UPDATE exhibition
            SET title=?, start_date=?, end_date=?, description=?, curator_name=?, theme=?, gallery_id=?
            WHERE title=?
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, exhibitionTitleField.getText());
            ps.setDate(2, Date.valueOf(exhibitionStartDateField.getValue()));

            if (exhibitionEndDateField.getValue() == null) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(exhibitionEndDateField.getValue()));
            }

            ps.setString(4, exhibitionDescriptionField.getText());
            ps.setString(5, exhibitionCuratorField.getText());
            ps.setString(6, exhibitionThemeField.getText());
            ps.setInt(7, getGalleryIdByName(exhibitionGalleryComboBox.getValue()));
            ps.setString(8, selected);

            ps.executeUpdate();

            loadExhibitions();

            showInfo("Exposition modifiée", "L'exposition a été modifiée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur modification exposition", e.getMessage());
        }
    }

    // =========================================================
    // DELETE EXHIBITION
    // =========================================================

    @FXML
    private void deleteExhibition() {

        String selected = exhibitionListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner une exposition.");
            return;
        }

        String sql = "DELETE FROM exhibition WHERE title = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, selected);
            ps.executeUpdate();

            loadExhibitions();

            showInfo("Exposition supprimée", "L'exposition a été supprimée avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            handleDeleteError(e, "autre"); 
        }
    }

    // =========================================================
    // CLEAR EXHIBITION FORM
    // =========================================================

    private void clearExhibitionFields() {
        exhibitionTitleField.clear();
        exhibitionStartDateField.setValue(null);
        exhibitionEndDateField.setValue(null);
        exhibitionDescriptionField.clear();
        exhibitionCuratorField.clear();
        exhibitionThemeField.clear();
        exhibitionGalleryComboBox.setValue(null);
    }

    // =========================================================
    // LOAD WORKSHOPS
    // =========================================================

    @FXML
    public void loadWorkshops() {

        workshopListView.getItems().clear();

        String sql = "SELECT title FROM workshop ORDER BY date";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                workshopListView.getItems().add(rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur chargement workshops", e.getMessage());
        }
    }

    // =========================================================
    // ADD WORKSHOP
    // =========================================================

    @FXML
    private void handleAddWorkshop() {

        String sql = """
            INSERT INTO workshop
            (title, date, duration_minutes, max_participants, price, instructor_artist_id, location, description, level)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            int artistId = getArtistIdByName(workshopArtistComboBox.getValue());

            LocalDate date = workshopDateField.getValue();
            String[] timeParts = workshopTimeField.getText().split(":");
            int hour   = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            LocalDateTime dateTime = date.atTime(hour, minute);

            ps.setString(1, workshopTitleField.getText());
            ps.setTimestamp(2, Timestamp.valueOf(dateTime));
            ps.setInt(3, Integer.parseInt(workshopDurationField.getText()));
            ps.setInt(4, Integer.parseInt(workshopMaxParticipantsField.getText()));

            if (workshopPriceField.getText().isEmpty()) {
                ps.setNull(5, Types.DECIMAL);
            } else {
                ps.setBigDecimal(5, new BigDecimal(workshopPriceField.getText()));
            }

            ps.setInt(6, artistId);
            ps.setString(7, workshopLocationField.getText());
            ps.setString(8, workshopDescriptionField.getText());
            ps.setString(9, workshopLevelComboBox.getValue());

            ps.executeUpdate();

            loadWorkshops();
            clearWorkshopFields();

            showInfo("Workshop ajouté", "Le workshop a été ajouté avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur ajout workshop", e.getMessage());
        }
    }

    // =========================================================
    // UPDATE WORKSHOP
    // =========================================================

    @FXML
    private void handleUpdateWorkshop() {

        String selected = workshopListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner un workshop à modifier.");
            return;
        }

        String sql = """
            UPDATE workshop
            SET title=?, date=?, duration_minutes=?, max_participants=?, price=?,
                instructor_artist_id=?, location=?, description=?, level=?
            WHERE title=?
        """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            int artistId = getArtistIdByName(workshopArtistComboBox.getValue());

            LocalDate date = workshopDateField.getValue();
            String[] timeParts = workshopTimeField.getText().split(":");
            int hour   = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            LocalDateTime dateTime = date.atTime(hour, minute);

            ps.setString(1, workshopTitleField.getText());
            ps.setTimestamp(2, Timestamp.valueOf(dateTime));
            ps.setInt(3, Integer.parseInt(workshopDurationField.getText()));
            ps.setInt(4, Integer.parseInt(workshopMaxParticipantsField.getText()));

            if (workshopPriceField.getText().isEmpty()) {
                ps.setNull(5, Types.DECIMAL);
            } else {
                ps.setBigDecimal(5, new BigDecimal(workshopPriceField.getText()));
            }

            ps.setInt(6, artistId);
            ps.setString(7, workshopLocationField.getText());
            ps.setString(8, workshopDescriptionField.getText());
            ps.setString(9, workshopLevelComboBox.getValue());
            ps.setString(10, selected);

            ps.executeUpdate();

            loadWorkshops();

            showInfo("Workshop modifié", "Le workshop a été modifié avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur modification workshop", e.getMessage());
        }
    }

    // =========================================================
    // DELETE WORKSHOP
    // =========================================================

    @FXML
    private void deleteWorkshop() {

        String selected = workshopListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Aucune sélection", "Veuillez sélectionner un workshop.");
            return;
        }

        String sql = "DELETE FROM workshop WHERE title = ?";

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, selected);
            ps.executeUpdate();

            loadWorkshops();

            showInfo("Workshop supprimé", "Le workshop a été supprimé avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
            handleDeleteError(e, "autre");
        }
    }

    // =========================================================
    // CLEAR WORKSHOP FORM
    // =========================================================

    private void clearWorkshopFields() {
        workshopTitleField.clear();
        workshopDateField.setValue(null);
        workshopTimeField.clear();
        workshopDurationField.clear();
        workshopMaxParticipantsField.clear();
        workshopPriceField.clear();
        workshopArtistComboBox.setValue(null);
        workshopLocationField.clear();
        workshopDescriptionField.clear();
        workshopLevelComboBox.setValue(null);
    }

    // =========================================================
    // TRADUCTION DES ERREURS SQL EN MESSAGES LISIBLES
    // =========================================================

    private void handleDeleteError(Exception e, String entite) {

        // java.sql.SQLException contient le code d'erreur MySQL
        if (e instanceof java.sql.SQLException sqlEx) {

            int code = sqlEx.getErrorCode();

            switch (code) {

                case 1451 ->
                    // Erreur MySQL : suppression bloquée par une clé étrangère
                    showError(
                        "Suppression impossible",
                        switch (entite) {

                            case "artiste" ->
                                "Cet artiste ne peut pas être supprimé car il possède encore " +
                                "des œuvres ou des workshops.\n\n" +
                                "➡ Supprimez d'abord ses œuvres et ses workshops dans les onglets correspondants.";

                            case "galerie" ->
                                "Cette galerie ne peut pas être supprimée car elle héberge " +
                                "encore des expositions.\n\n" +
                                "➡ Supprimez d'abord ses expositions dans l'onglet Expositions.";

                            case "oeuvre" ->
                                "Cette œuvre ne peut pas être supprimée car elle est encore " +
                                "liée à une exposition.\n\n" +
                                "➡ Retirez-la de l'exposition concernée avant de la supprimer.";

                            default ->
                                "Cet élément est encore utilisé ailleurs dans la base de données.\n\n" +
                                "➡ Supprimez d'abord les éléments qui y sont liés.";
                        }
                    );

                case 1062 ->
                    // Erreur MySQL : doublon sur un champ unique
                    showError(
                        "Valeur déjà existante",
                        "Un élément avec ces informations existe déjà " +
                        "(email ou nom en double).\n\n" +
                        "➡ Vérifiez que l'email ou le nom saisi n'est pas déjà utilisé."
                    );

                default ->
                    // Erreur inconnue : on affiche quand même quelque chose d'utile
                    showError(
                        "Erreur inattendue (code " + code + ")",
                        "Une erreur s'est produite lors de la suppression.\n\n" +
                        "Détail technique : " + sqlEx.getMessage()
                    );
            }

        } else {
            // Erreur Java non-SQL (ex : NullPointerException)
            showError(
                "Erreur interne",
                "Une erreur inattendue s'est produite.\n\n" +
                "Détail : " + e.getMessage()
            );
        }
    }

    // =========================================================
    // ALERTES
    // =========================================================

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}