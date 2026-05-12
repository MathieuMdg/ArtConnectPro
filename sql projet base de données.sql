DROP DATABASE IF EXISTS artconnect;
CREATE DATABASE artconnect CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE artconnect;

CREATE TABLE artist (
    artist_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    bio TEXT,
    birth_year INT,
    contact_email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    city VARCHAR(100),
    website VARCHAR(255),
    social_media VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE gallery (
    gallery_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    owner_name VARCHAR(100),
    opening_hours VARCHAR(100),
    contact_phone VARCHAR(30),
    rating DECIMAL(3,1),
    website VARCHAR(255),
    CONSTRAINT chk_gallery_rating
        CHECK (rating IS NULL OR (rating >= 0 AND rating <= 5))
);

CREATE TABLE discipline (
    discipline_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE artwork_tag (
    tag_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE community_member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    birth_year INT,
    phone VARCHAR(30),
    city VARCHAR(100),
    membership_type VARCHAR(50)
);

CREATE TABLE artwork (
    artwork_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    creation_year INT,
    type VARCHAR(100) NOT NULL,
    medium VARCHAR(100),
    dimensions VARCHAR(100),
    description TEXT,
    price DECIMAL(15,2),
    status VARCHAR(20) NOT NULL,
    artist_id INT NOT NULL,
    CONSTRAINT fk_artwork_artist
        FOREIGN KEY (artist_id) REFERENCES artist(artist_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_artwork_price
        CHECK (price IS NULL OR price >= 0),
    CONSTRAINT chk_artwork_status
        CHECK (status IN ('FOR_SALE', 'SOLD', 'EXHIBITED'))
);

CREATE TABLE exhibition (
    exhibition_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT,
    curator_name VARCHAR(100),
    theme VARCHAR(150),
    gallery_id INT NOT NULL,
    CONSTRAINT fk_exhibition_gallery
        FOREIGN KEY (gallery_id) REFERENCES gallery(gallery_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_exhibition_dates
        CHECK (end_date IS NULL OR end_date >= start_date)
);

CREATE TABLE workshop (
    workshop_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    date DATETIME NOT NULL,
    duration_minutes INT NOT NULL,
    max_participants INT NOT NULL,
    price DECIMAL(10,2),
    instructor_artist_id INT NOT NULL,
    location VARCHAR(150),
    description TEXT,
    level VARCHAR(30) NOT NULL,
    CONSTRAINT fk_workshop_artist
        FOREIGN KEY (instructor_artist_id) REFERENCES artist(artist_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_workshop_price
        CHECK (price IS NULL OR price >= 0),
    CONSTRAINT chk_workshop_duration
        CHECK (duration_minutes > 0),
    CONSTRAINT chk_workshop_max_participants
        CHECK (max_participants > 0),
    CONSTRAINT chk_workshop_level
        CHECK (level IN ('Beginner', 'Intermediate', 'Advanced'))
);

CREATE TABLE review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    reviewer_member_id INT NOT NULL,
    artwork_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    review_date DATE NOT NULL,
    CONSTRAINT fk_review_member
        FOREIGN KEY (reviewer_member_id) REFERENCES community_member(member_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_review_artwork
        FOREIGN KEY (artwork_id) REFERENCES artwork(artwork_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_review_rating
        CHECK (rating BETWEEN 1 AND 5)
);

CREATE TABLE booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    workshop_id INT NOT NULL,
    member_id INT NOT NULL,
    booking_date DATETIME NOT NULL,
    payment_status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_booking_workshop
        FOREIGN KEY (workshop_id) REFERENCES workshop(workshop_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_booking_member
        FOREIGN KEY (member_id) REFERENCES community_member(member_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT chk_booking_payment_status
        CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED', 'CANCELLED'))
);

CREATE TABLE exhibition_artwork (
    exhibition_id INT NOT NULL,
    artwork_id INT NOT NULL,
    PRIMARY KEY (exhibition_id, artwork_id),
    CONSTRAINT fk_exhibition_artwork_exhibition
        FOREIGN KEY (exhibition_id) REFERENCES exhibition(exhibition_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_exhibition_artwork_artwork
        FOREIGN KEY (artwork_id) REFERENCES artwork(artwork_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE artist_discipline (
    artist_id INT NOT NULL,
    discipline_id INT NOT NULL,
    PRIMARY KEY (artist_id, discipline_id),
    CONSTRAINT fk_artist_discipline_artist
        FOREIGN KEY (artist_id) REFERENCES artist(artist_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_artist_discipline_discipline
        FOREIGN KEY (discipline_id) REFERENCES discipline(discipline_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE member_favorite_discipline (
    member_id INT NOT NULL,
    discipline_id INT NOT NULL,
    PRIMARY KEY (member_id, discipline_id),
    CONSTRAINT fk_member_fav_discipline_member
        FOREIGN KEY (member_id) REFERENCES community_member(member_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_member_fav_discipline_discipline
        FOREIGN KEY (discipline_id) REFERENCES discipline(discipline_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE artwork_tag_map (
    artwork_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (artwork_id, tag_id),
    CONSTRAINT fk_artwork_tag_map_artwork
        FOREIGN KEY (artwork_id) REFERENCES artwork(artwork_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_artwork_tag_map_tag
        FOREIGN KEY (tag_id) REFERENCES artwork_tag(tag_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

USE artconnect;

INSERT INTO discipline (name) VALUES
('Painting'),
('Sculpture'),
('Photography'),
('Digital Art'),
('Mixed Media'),
('Printmaking');

INSERT INTO artwork_tag (name) VALUES
('Portrait'),
('Landscape'),
('Modern'),
('Classic'),
('BlackAndWhite'),
('Abstract');

INSERT INTO artist (name, bio, birth_year, contact_email, phone, city, website, social_media, is_active) VALUES
('Leonardo Vinci', 'Renaissance painter and workshop instructor.', 1452, 'leo@vincistudio.it', '+39-111-111', 'Florence', 'www.vincistudio.it', '@leo_vinci', TRUE),
('Claude Monet', 'Master of impressionist painting.', 1840, 'claude@monet.fr', '+33-222-222', 'Giverny', 'www.monet.fr', '@claude_monet', TRUE),
('Ansel Adams', 'Landscape and black-and-white photographer.', 1902, 'ansel@adams.co', '+1-333-333', 'San Francisco', 'www.anseladams.co', '@ansel_adams', TRUE),
('Frida Kahlo', 'Iconic painter focused on identity and symbolism.', 1907, 'frida@kahlo.mx', '+52-444-444', 'Mexico City', 'www.fridakahlo.mx', '@frida_kahlo', TRUE),
('Auguste Rodin', 'French sculptor and art mentor.', 1840, 'auguste@rodin.fr', '+33-555-555', 'Paris', 'www.rodin.fr', '@auguste_rodin', TRUE),
('Sofia Turner', 'Digital and mixed-media contemporary artist.', 1992, 'sofia@turner.art', '+44-666-666', 'London', 'www.sofiaturner.art', '@sofia_turner', TRUE);

INSERT INTO gallery (name, address, owner_name, opening_hours, contact_phone, rating, website) VALUES
('Louvre Art House', 'Rue de Rivoli, Paris', 'Marie Laurent', '09:00-18:00', '+33-701-111', 4.9, 'www.louvrearthouse.fr'),
('The British Gallery', 'Great Russell St, London', 'James Carter', '10:00-18:30', '+44-702-222', 4.7, 'www.britishgallery.uk'),
('Metropolitan Hub', '1000 5th Ave, New York', 'Olivia Stone', '09:30-19:00', '+1-703-333', 4.8, 'www.metropolitanhub.us'),
('Modern Arts Center', '22 Art Street, Berlin', 'Hans Keller', '10:00-20:00', '+49-704-444', 4.6, 'www.modernartscenter.de');

INSERT INTO gallery (name, address, owner_name, opening_hours, contact_phone, rating, website) VALUES
('Beaux Arts', '32 Art Street, Berlin', 'Mahammed', '10:01-20:00', '+49-704-445', 4.8, 'www.modernartscenter.dz');

INSERT INTO community_member (name, email, birth_year, phone, city, membership_type) VALUES
('Alice Wonderland', 'alice@art.com', 1998, '+33-801-001', 'Paris', 'Premium'),
('Bob Ross', 'bob@happytrees.com', 1985, '+44-801-002', 'London', 'Standard'),
('Charlie Brown', 'charlie@peanuts.com', 1995, '+1-801-003', 'New York', 'Student'),
('Diana Moore', 'diana@culture.org', 1990, '+49-801-004', 'Berlin', 'Premium'),
('Emma Davis', 'emma@creative.net', 2001, '+33-801-005', 'Lyon', 'Standard'),
('Farid Khan', 'farid@artsocial.io', 1993, '+212-801-006', 'Casablanca', 'Premium'),
('Mendougance', 'mendougance@art.com', 1983, '+212-801-006', 'Bamako', 'Premium');

INSERT INTO artist_discipline (artist_id, discipline_id) VALUES
(1, 1),
(2, 1),
(3, 3),
(4, 1),
(5, 2),
(6, 4),
(6, 5);

INSERT INTO member_favorite_discipline (member_id, discipline_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 3),
(4, 4),
(5, 5),
(6, 2),
(6, 4);

INSERT INTO artwork (title, creation_year, type, medium, dimensions, description, price, status, artist_id) VALUES
('Mona Lisa', 1503, 'Painting', 'Oil on wood', '77x53 cm', 'Portrait of Lisa Gherardini.', 850000000.00, 'FOR_SALE', 1),
('The Last Supper', 1498, 'Painting', 'Tempera on gesso', '460x880 cm', 'Religious mural masterpiece.', 450000000.00, 'EXHIBITED', 1),
('Water Lilies', 1916, 'Painting', 'Oil on canvas', '200x180 cm', 'Series inspired by Monet''s garden.', 40000000.00, 'FOR_SALE', 2),
('The Two Fridas', 1939, 'Painting', 'Oil on canvas', '173x173 cm', 'Double self-portrait.', 5000000.00, 'EXHIBITED', 4),
('The Thinker', 1904, 'Sculpture', 'Bronze', '186x98x145 cm', 'Famous bronze sculpture.', 15000000.00, 'FOR_SALE', 5),
('Monolith, The Face of Half Dome', 1927, 'Photography', 'Gelatin silver print', '40x50 cm', 'Iconic black-and-white landscape.', 100000.00, 'SOLD', 3),
('Neon Fragments', 2022, 'Digital Art', 'Digital mixed media', '4K', 'Contemporary digital composition.', 12000.00, 'FOR_SALE', 6),
('Urban Pulse', 2023, 'Mixed Media', 'Acrylic and digital print', '120x90 cm', 'City-inspired hybrid artwork.', 18000.00, 'FOR_SALE', 6);

INSERT INTO artwork_tag_map (artwork_id, tag_id) VALUES
(1, 1),
(1, 4),
(2, 4),
(3, 2),
(4, 1),
(4, 6),
(5, 3),
(6, 2),
(6, 5),
(7, 3),
(7, 6),
(8, 3);

INSERT INTO exhibition (title, start_date, end_date, description, curator_name, theme, gallery_id) VALUES
('Renaissance Revival', '2026-03-09', '2026-04-15', 'A celebration of classical Renaissance masterpieces.', 'Marie Laurent', 'Classic Renaissance', 1),
('Sculpting the Soul', '2026-03-25', '2026-05-01', 'Exploration of sculptural expression.', 'James Carter', 'Modern & Classical Sculpture', 2),
('Impressionist Dreams', '2026-02-09', '2026-03-20', 'Light, color and poetic atmosphere.', 'Olivia Stone', 'Light and Color', 3),
('Digital Horizons', '2026-04-05', '2026-05-30', 'Contemporary digital and hybrid artworks.', 'Hans Keller', 'Future of Expression', 4);

INSERT INTO exhibition (title, start_date, end_date, description, curator_name, theme, gallery_id) VALUES
('Large Horizons', '2026-04-08', '2026-05-05', 'Modern art work.', 'Mahammed', 'Future of Art', 5);

INSERT INTO exhibition_artwork (exhibition_id, artwork_id) VALUES
(1, 1),
(1, 2),
(2, 5),
(3, 3),
(3, 4),
(4, 7),
(4, 8),
(4, 6);

INSERT INTO workshop (title, date, duration_minutes, max_participants, price, instructor_artist_id, location, description, level) VALUES
('Mastering Oil Painting', '2026-04-14 10:00:00', 180, 15, 150.00, 1, 'Louvre Art House', 'Hands-on introduction to oil painting methods.', 'Intermediate'),
('Impressionist Landscapes', '2026-04-19 14:00:00', 150, 20, 120.00, 2, 'Metropolitan Hub', 'Painting landscapes with light and movement.', 'Beginner'),
('Sculpting Modernity', '2026-04-24 09:30:00', 210, 12, 200.00, 5, 'The British Gallery', 'Study of expressive sculpture techniques.', 'Advanced'),
('Creative Digital Layers', '2026-05-03 11:00:00', 160, 18, 90.00, 6, 'Modern Arts Center', 'Experimenting with digital and mixed-media workflows.', 'Intermediate');


INSERT INTO review (reviewer_member_id, artwork_id, rating, comment, review_date) VALUES
(1, 1, 5, 'A timeless masterpiece.', '2026-03-10'),
(2, 3, 4, 'Beautiful colors and atmosphere.', '2026-03-11'),
(3, 6, 5, 'Powerful photograph with great depth.', '2026-03-12'),
(4, 7, 4, 'Very modern and engaging.', '2026-04-06'),
(5, 5, 5, 'A monumental sculpture.', '2026-03-28'),
(6, 4, 4, 'Emotional and striking composition.', '2026-03-13');

INSERT INTO booking (workshop_id, member_id, booking_date, payment_status) VALUES
(1, 1, '2026-04-01 09:00:00', 'PAID'),
(1, 2, '2026-04-01 10:00:00', 'PENDING'),
(2, 3, '2026-04-02 11:15:00', 'PAID'),
(2, 5, '2026-04-02 12:00:00', 'PAID'),
(3, 4, '2026-04-03 14:20:00', 'PAID'),
(4, 6, '2026-04-04 16:00:00', 'PENDING'),
(4, 1, '2026-04-04 16:30:00', 'PAID');

USE artconnect;

CREATE OR REPLACE VIEW vw_artwork_details AS
SELECT
    a.artwork_id,
    a.title,
    a.type,
    a.medium,
    a.price,
    a.status,
    ar.name AS artist_name,
    ar.city AS artist_city
FROM artwork a
JOIN artist ar ON a.artist_id = ar.artist_id;

CREATE OR REPLACE VIEW vw_workshop_booking_summary AS
SELECT
    w.workshop_id,
    w.title,
    w.date,
    w.max_participants,
    COUNT(b.booking_id) AS total_bookings,
    SUM(CASE WHEN b.payment_status = 'PAID' THEN 1 ELSE 0 END) AS paid_bookings
FROM workshop w
LEFT JOIN booking b ON w.workshop_id = b.workshop_id
GROUP BY w.workshop_id, w.title, w.date, w.max_participants;

CREATE OR REPLACE VIEW vw_exhibition_catalog AS
SELECT
    e.exhibition_id,
    e.title AS exhibition_title,
    e.theme,
    g.name AS gallery_name,
    a.title AS artwork_title,
    ar.name AS artist_name
FROM exhibition e
JOIN gallery g ON e.gallery_id = g.gallery_id
JOIN exhibition_artwork ea ON e.exhibition_id = ea.exhibition_id
JOIN artwork a ON ea.artwork_id = a.artwork_id
JOIN artist ar ON a.artist_id = ar.artist_id;

USE artconnect;

CREATE INDEX idx_artist_name ON artist(name);
CREATE INDEX idx_artwork_title ON artwork(title);
CREATE INDEX idx_booking_workshop_status ON booking(workshop_id, payment_status);

USE artconnect;

DELIMITER $$

CREATE TRIGGER trg_workshop_check_capacity_before_insert
BEFORE INSERT ON booking
FOR EACH ROW
BEGIN
    DECLARE v_count INT;
    DECLARE v_max INT;

    SELECT COUNT(*)
    INTO v_count
    FROM booking
    WHERE workshop_id = NEW.workshop_id;

    SELECT max_participants
    INTO v_max
    FROM workshop
    WHERE workshop_id = NEW.workshop_id;

    IF v_count >= v_max THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Booking refused: workshop is full.';
    END IF;
END$$

CREATE TRIGGER trg_review_check_rating_before_insert
BEFORE INSERT ON review
FOR EACH ROW
BEGIN
    IF NEW.rating < 1 OR NEW.rating > 5 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Rating must be between 1 and 5.';
    END IF;
END$$

CREATE TRIGGER trg_exhibition_check_dates_before_insert
BEFORE INSERT ON exhibition
FOR EACH ROW
BEGIN
    IF NEW.end_date IS NOT NULL AND NEW.end_date < NEW.start_date THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Exhibition end_date must be greater than or equal to start_date.';
    END IF;
END$$

DELIMITER ;

USE artconnect;

DELIMITER $$

CREATE PROCEDURE sp_add_booking (
    IN p_workshop_id INT,
    IN p_member_id INT,
    IN p_payment_status VARCHAR(30)
)
BEGIN
    INSERT INTO booking (workshop_id, member_id, booking_date, payment_status)
    VALUES (p_workshop_id, p_member_id, NOW(), p_payment_status);
END$$

CREATE PROCEDURE sp_create_exhibition (
    IN p_title VARCHAR(150),
    IN p_start_date DATE,
    IN p_end_date DATE,
    IN p_description TEXT,
    IN p_curator_name VARCHAR(100),
    IN p_theme VARCHAR(150),
    IN p_gallery_id INT
)
BEGIN
    INSERT INTO exhibition (title, start_date, end_date, description, curator_name, theme, gallery_id)
    VALUES (p_title, p_start_date, p_end_date, p_description, p_curator_name, p_theme, p_gallery_id);
END$$

CREATE FUNCTION fn_count_paid_bookings (p_workshop_id INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE v_total INT;

    SELECT COUNT(*)
    INTO v_total
    FROM booking
    WHERE workshop_id = p_workshop_id
      AND payment_status = 'PAID';

    RETURN v_total;
END$$

DELIMITER ;


USE artconnect;

SELECT * FROM vw_artwork_details;
SELECT * FROM vw_workshop_booking_summary;
SELECT * FROM vw_exhibition_catalog;

CALL sp_add_booking(2, 6, 'PAID');

CALL sp_create_exhibition(
    'Photography and Light',
    '2026-06-10',
    '2026-07-05',
    'A curated exhibition around photography and light.',
    'Olivia Stone',
    'Visual Atmospheres',
    3
);

SELECT fn_count_paid_bookings(2) AS paid_bookings_for_workshop_2;

USE artconnect;

START TRANSACTION;

INSERT INTO booking (workshop_id, member_id, booking_date, payment_status)
VALUES (1, 3, NOW(), 'PAID');

INSERT INTO booking (workshop_id, member_id, booking_date, payment_status)
VALUES (4, 3, NOW(), 'PAID');

COMMIT;

USE artconnect;

START TRANSACTION;

INSERT INTO booking (workshop_id, member_id, booking_date, payment_status)
VALUES (3, 2, NOW(), 'PAID');

INSERT INTO booking (workshop_id, member_id, booking_date, payment_status)
VALUES (999, 2, NOW(), 'PAID');

SELECT *
FROM community_member
WHERE name = 'Mendougance';

ROLLBACK;

