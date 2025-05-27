-- Drop existing objects (order: triggers, tables, sequences) --

-- Drop Triggers
DROP TRIGGER trg_roles_bi;
DROP TRIGGER trg_users_bi;
DROP TRIGGER trg_subscription_plans_bi;
DROP TRIGGER trg_subscriptions_bi;
DROP TRIGGER trg_music_bi;
DROP TRIGGER trg_playlists_bi;
DROP TRIGGER trg_genres_bi;
DROP TRIGGER trg_moods_bi;
/

-- Drop Tables
DROP TABLE PlaylistMusic CASCADE CONSTRAINTS;
DROP TABLE Playlists CASCADE CONSTRAINTS;
DROP TABLE Likes CASCADE CONSTRAINTS;
DROP TABLE UserMusicDailyStats CASCADE CONSTRAINTS;
DROP TABLE Music CASCADE CONSTRAINTS;
DROP TABLE Subscriptions CASCADE CONSTRAINTS;
DROP TABLE SubscriptionPlans CASCADE CONSTRAINTS;
DROP TABLE Users CASCADE CONSTRAINTS;
DROP TABLE Roles CASCADE CONSTRAINTS;
DROP TABLE Genres CASCADE CONSTRAINTS;
DROP TABLE Moods CASCADE CONSTRAINTS;
/

-- Drop Sequences
DROP SEQUENCE role_seq;
DROP SEQUENCE user_seq;
DROP SEQUENCE subscription_plan_seq;
DROP SEQUENCE subscription_seq;
DROP SEQUENCE music_seq;
DROP SEQUENCE playlist_seq;
DROP SEQUENCE genre_seq;
DROP SEQUENCE mood_seq;
/

-- Create Tables, Sequences, and Triggers --

-- Roles Table
CREATE TABLE Roles (
    role_id NUMBER PRIMARY KEY,
    role_name VARCHAR2(50) UNIQUE NOT NULL
);

CREATE SEQUENCE role_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_roles_bi
BEFORE INSERT ON Roles
FOR EACH ROW
BEGIN
    SELECT role_seq.NEXTVAL INTO :NEW.role_id FROM dual;
END;
/

-- Users Table
CREATE TABLE Users (
    user_id NUMBER PRIMARY KEY,
    first_name VARCHAR2(100),
    last_name VARCHAR2(100),
    bio VARCHAR2(255),
    username VARCHAR2(100) UNIQUE NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    image_url VARCHAR2(255),
    password VARCHAR2(255) NOT NULL,
    role_id NUMBER,
    created_at DATE DEFAULT SYSDATE,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_users_bi
BEFORE INSERT ON Users
FOR EACH ROW
BEGIN
    SELECT user_seq.NEXTVAL INTO :NEW.user_id FROM dual;
END;
/

-- SubscriptionPlans Table
CREATE TABLE SubscriptionPlans (
    subscription_plan_id NUMBER PRIMARY KEY,
    name VARCHAR2(50) NOT NULL,
    stripe_price_id VARCHAR2(100) NOT NULL UNIQUE,
    billing_cycle VARCHAR2(20) DEFAULT 'monthly' CHECK (billing_cycle IN ('monthly', 'yearly')),
    price NUMBER(10, 2) NOT NULL,
    description VARCHAR2(255),
    features CLOB, -- JSON string for features
    created_at DATE DEFAULT SYSDATE,
    plan_type VARCHAR2(20) NOT NULL CHECK (plan_type IN ('listener', 'creator'))
);

CREATE SEQUENCE subscription_plan_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_subscription_plans_bi
BEFORE INSERT ON SubscriptionPlans
FOR EACH ROW
BEGIN
    SELECT subscription_plan_seq.NEXTVAL INTO :NEW.subscription_plan_id FROM dual;
END;
/

-- Subscriptions Table
CREATE TABLE Subscriptions (
    subscription_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    start_date DATE,
    end_date DATE,
    amount_paid NUMBER(10, 2),
    payment_status VARCHAR2(10) CHECK (payment_status IN ('paid', 'failed', 'pending')),
    payment_gateway_ref VARCHAR2(255),
    created_at DATE DEFAULT SYSDATE,
    subscription_plan_id NUMBER,
    CONSTRAINT fk_subscription_plan FOREIGN KEY (subscription_plan_id) REFERENCES SubscriptionPlans(subscription_plan_id),
    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE SEQUENCE subscription_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_subscriptions_bi
BEFORE INSERT ON Subscriptions
FOR EACH ROW
BEGIN
    SELECT subscription_seq.NEXTVAL INTO :NEW.subscription_id FROM dual;
END;
/

-- Genres Table
CREATE TABLE Genres (
    genre_id NUMBER PRIMARY KEY,
    name VARCHAR2(50) UNIQUE NOT NULL
);

CREATE SEQUENCE genre_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_genres_bi
BEFORE INSERT ON Genres
FOR EACH ROW
BEGIN
    SELECT genre_seq.NEXTVAL INTO :NEW.genre_id FROM dual;
END;
/

-- Moods Table
CREATE TABLE Moods (
    mood_id NUMBER PRIMARY KEY,
    name VARCHAR2(50) UNIQUE NOT NULL
);

CREATE SEQUENCE mood_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_moods_bi
BEFORE INSERT ON Moods
FOR EACH ROW
BEGIN
    SELECT mood_seq.NEXTVAL INTO :NEW.mood_id FROM dual;
END;
/

-- Music Table
CREATE TABLE Music (
    music_id NUMBER PRIMARY KEY,
    artist_id NUMBER NOT NULL,
    title VARCHAR2(100),
    description VARCHAR2(255),
    upload_date DATE DEFAULT SYSDATE,
    audio_file_url VARCHAR2(255),
    image_url VARCHAR2(255),
    premium_content NUMBER(1) DEFAULT 0 NOT NULL,
    genre_id NUMBER,
    mood_id NUMBER,
    CONSTRAINT fk_music_artist FOREIGN KEY (artist_id) REFERENCES Users(user_id),
    CONSTRAINT fk_music_genre FOREIGN KEY (genre_id) REFERENCES Genres(genre_id),
    CONSTRAINT fk_music_mood FOREIGN KEY (mood_id) REFERENCES Moods(mood_id)
);

CREATE SEQUENCE music_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_music_bi
BEFORE INSERT ON Music
FOR EACH ROW
BEGIN
    SELECT music_seq.NEXTVAL INTO :NEW.music_id FROM dual;
END;
/

-- Playlists Table
CREATE TABLE Playlists (
    playlist_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    name VARCHAR2(100),
    created_at DATE DEFAULT SYSDATE,
    CONSTRAINT fk_playlist_user FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE SEQUENCE playlist_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_playlists_bi
BEFORE INSERT ON Playlists
FOR EACH ROW
BEGIN
    SELECT playlist_seq.NEXTVAL INTO :NEW.playlist_id FROM dual;
END;
/

-- PlaylistMusic Table (Join Table)
CREATE TABLE PlaylistMusic (
    playlist_id NUMBER,
    music_id NUMBER,
    added_at DATE DEFAULT SYSDATE,
    PRIMARY KEY (playlist_id, music_id),
    CONSTRAINT fk_pm_playlist FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id) ON DELETE CASCADE,
    CONSTRAINT fk_pm_music FOREIGN KEY (music_id) REFERENCES Music(music_id) ON DELETE CASCADE
);

-- UserMusicDailyStats Table
CREATE TABLE UserMusicDailyStats (
    user_id     NUMBER NOT NULL,
    music_id    NUMBER NOT NULL,
    stat_date   DATE DEFAULT TRUNC(SYSDATE) NOT NULL,
    plays       NUMBER DEFAULT 0,
    skips       NUMBER DEFAULT 0,
    repeats     NUMBER DEFAULT 0,
    created_at  DATE DEFAULT SYSDATE,
    PRIMARY KEY (user_id, music_id, stat_date),
    CONSTRAINT fk_dailystats_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_dailystats_music FOREIGN KEY (music_id) REFERENCES Music(music_id) ON DELETE CASCADE
);

-- Indexes for performance on UserMusicDailyStats
CREATE INDEX idx_dailystats_user_date ON UserMusicDailyStats(user_id, stat_date);
CREATE INDEX idx_dailystats_music_date ON UserMusicDailyStats(music_id, stat_date); -- Renamed for clarity
-- CREATE INDEX idx_dailystats_user ON UserMusicDailyStats(user_id);
CREATE INDEX idx_dailystats_music ON UserMusicDailyStats(music_id);


-- Likes Table
CREATE TABLE Likes (
    user_id NUMBER NOT NULL,
    music_id NUMBER NOT NULL,
    liked_at DATE DEFAULT SYSDATE,
    PRIMARY KEY (user_id, music_id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_music FOREIGN KEY (music_id) REFERENCES Music(music_id) ON DELETE CASCADE
);

-- Test Inserts --

-- === Insert Roles ===
INSERT INTO Roles (role_name) VALUES ('admin');
INSERT INTO Roles (role_name) VALUES ('superadmin');
INSERT INTO Roles (role_name) VALUES ('artist');
INSERT INTO Roles (role_name) VALUES ('user');
INSERT INTO Roles (role_name) VALUES ('premiumuser');
COMMIT;

-- === Insert Genres ===
INSERT INTO Genres (name) VALUES ('Lo-fi');
INSERT INTO Genres (name) VALUES ('Electronic');
INSERT INTO Genres (name) VALUES ('Ambient');
INSERT INTO Genres (name) VALUES ('Classical');
INSERT INTO Genres (name) VALUES ('Jazz');
INSERT INTO Genres (name) VALUES ('Hip Hop');
INSERT INTO Genres (name) VALUES ('Chillout');
COMMIT;

-- === Insert Moods ===
INSERT INTO Moods (name) VALUES ('Relaxing');
INSERT INTO Moods (name) VALUES ('Upbeat');
INSERT INTO Moods (name) VALUES ('Melancholic');
INSERT INTO Moods (name) VALUES ('Focus');
INSERT INTO Moods (name) VALUES ('Energetic');
INSERT INTO Moods (name) VALUES ('Peaceful');
INSERT INTO Moods (name) VALUES ('Productive');
COMMIT;

-- === Insert Subscription Plans ===
INSERT INTO SubscriptionPlans (name, stripe_price_id, billing_cycle, price, description, features, plan_type)
VALUES (
    'Creator Plan',
    'price_1ROFrkK0WXRjNyZGqtoNOWV3',
    'monthly',
    9.99,
    'Enables content uploads and premium features',
    '["Upload unlimited music","Access premium analytics","Monetize your tracks"]',
    'creator'
);

INSERT INTO SubscriptionPlans (name, stripe_price_id, billing_cycle, price, description, features, plan_type)
VALUES (
    'Listener Plan - Monthly',
    'price_1RPaYPK0WXRjNyZGJp1ZuTtz',
    'monthly',
    4.99,
    'Ad-free listening and playlist features',
    '["Unlimited streaming","No ads","Playlist creation","Offline downloads"]',
    'listener'
);

INSERT INTO SubscriptionPlans (name, stripe_price_id, billing_cycle, price, description, features, plan_type)
VALUES (
    'Listener Plan - Yearly',
    'price_1RTOHxK0WXRjNyZGr6W9xeVS',
    'yearly',
    49.99,
    'Ad-free listening and playlist features for a whole year at a discount',
    '["Unlimited streaming","No ads","Playlist creation","Offline downloads","Yearly discount"]',
    'listener'
);
COMMIT;

-- === Insert Users ===
-- Password for all users: 'password123' (hashed: ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f)
-- User: Admin
INSERT INTO Users (first_name, last_name, bio, username, email, password, role_id, image_url)
VALUES (
    'Admin', 'User', 'Lead admin and system overseer', 'admin_user', 'admin@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', -- password123
    (SELECT role_id FROM Roles WHERE role_name = 'admin'),
    'https://placehold.co/150x150/E0E0E0/B0B0B0?text=Admin'
);

-- User: Artist 1
INSERT INTO Users (first_name, last_name, bio, username, email, password, role_id, image_url)
VALUES (
    'Rena', 'Saito', 'Indie electronic artist and lo-fi producer', 'lofi_rena', 'rena.saito@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', -- password123
    (SELECT role_id FROM Roles WHERE role_name = 'artist'),
    'https://placehold.co/150x150/DDEBF7/6C7A89?text=Rena'
);

-- User: Artist 2
INSERT INTO Users (first_name, last_name, bio, username, email, password, role_id, image_url)
VALUES (
    'Kenji', 'Tanaka', 'Ambient soundscape creator', 'ambient_kenji', 'kenji.tanaka@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', -- password123
    (SELECT role_id FROM Roles WHERE role_name = 'artist'),
    'https://placehold.co/150x150/F0E68C/A08D5F?text=Kenji'
);


-- User: Listener (Regular User)
INSERT INTO Users (first_name, last_name, bio, username, email, password, role_id, image_url)
VALUES (
    'Mira', 'Kobayashi', 'Just a casual listener who loves ambient music', 'mira_k', 'mira.kobayashi@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', -- password123
    (SELECT role_id FROM Roles WHERE role_name = 'user'),
    'https://placehold.co/150x150/FFDAB9/D291BC?text=Mira'
);

-- User: Listener (Premium User)
INSERT INTO Users (first_name, last_name, bio, username, email, password, role_id, image_url)
VALUES (
    'Yuki', 'Honda', 'Premium listener, enjoys ad-free experience.', 'yuki_premium', 'yuki.honda@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', -- password123
    (SELECT role_id FROM Roles WHERE role_name = 'premiumuser'),
    'https://placehold.co/150x150/ADD8E6/607B8B?text=Yuki'
);
COMMIT;

-- === Insert Sample Music ===
INSERT INTO Music (artist_id, title, description, audio_file_url, image_url, premium_content, genre_id, mood_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'lofi_rena'),
    'Chill Vibes',
    'Relaxing lo-fi beat to study or relax to.',
    'lofi_chill_vibes.mp3',
    'lofi_chill_vibes.png',
    0,
    (SELECT genre_id FROM Genres WHERE name = 'Lo-fi'),
    (SELECT mood_id FROM Moods WHERE name = 'Relaxing')
);

INSERT INTO Music (artist_id, title, description, audio_file_url, image_url, premium_content, genre_id, mood_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'lofi_rena'),
    'Midnight Study',
    'Focused beats for late night productivity.',
    'lofi_midnight_study.mp3',
    'lofi_midnight_study.png',
    1, -- Premium Content
    (SELECT genre_id FROM Genres WHERE name = 'Lo-fi'),
    (SELECT mood_id FROM Moods WHERE name = 'Focus')
);

INSERT INTO Music (artist_id, title, description, audio_file_url, image_url, premium_content, genre_id, mood_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'ambient_kenji'),
    'Forest Whispers',
    'Ethereal ambient sounds from a mystical forest.',
    'ambient_forest_whispers.mp3',
    'ambient_forest_whispers.png',
    0,
    (SELECT genre_id FROM Genres WHERE name = 'Ambient'),
    (SELECT mood_id FROM Moods WHERE name = 'Peaceful')
);

INSERT INTO Music (artist_id, title, description, audio_file_url, image_url, premium_content, genre_id, mood_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'ambient_kenji'),
    'Oceanic Dreams',
    'Deep and calming ocean soundscapes.',
    'ambient_oceanic_dreams.mp3',
    'ambient_oceanic_dreams.png',
    0,
    (SELECT genre_id FROM Genres WHERE name = 'Ambient'),
    (SELECT mood_id FROM Moods WHERE name = 'Relaxing')
);

INSERT INTO Music (artist_id, title, description, audio_file_url, image_url, premium_content, genre_id, mood_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'lofi_rena'),
    'Sunday Morning Jazz Hop',
    'Smooth jazz infused hip hop for a lazy Sunday.',
    'jazz_hop_sunday.mp3',
    'jazz_hop_sunday.png',
    0,
    (SELECT genre_id FROM Genres WHERE name = 'Jazz'), -- Or Hip Hop, or a new "Jazz Hop" genre
    (SELECT mood_id FROM Moods WHERE name = 'Relaxing')
);
COMMIT;

-- === Insert Sample Subscriptions ===
-- Yuki (premium user) subscribes to Listener Plan - Monthly
INSERT INTO Subscriptions (user_id, subscription_plan_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    (SELECT subscription_plan_id FROM SubscriptionPlans WHERE name = 'Listener Plan - Monthly'),
    SYSDATE - 30, -- Started 30 days ago
    SYSDATE + 0,  -- Ends today (for example, or SYSDATE + 30 for a typical monthly cycle)
    4.99,
    'paid',
    'stripe_pi_yuki_monthly_123'
);

-- Rena (artist) subscribes to Creator Plan
INSERT INTO Subscriptions (user_id, subscription_plan_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'lofi_rena'),
    (SELECT subscription_plan_id FROM SubscriptionPlans WHERE name = 'Creator Plan'),
    SYSDATE - 15, -- Started 15 days ago
    SYSDATE + 15, -- Ends in 15 days
    9.99,
    'paid',
    'stripe_pi_rena_creator_456'
);
COMMIT;

-- === Insert Sample Playlists ===
INSERT INTO Playlists (user_id, name)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'mira_k'),
    'My Chill Mix'
);

INSERT INTO Playlists (user_id, name)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    'Focus Zone'
);
COMMIT;

-- === Insert Sample PlaylistMusic ===
-- Mira's Chill Mix
INSERT INTO PlaylistMusic (playlist_id, music_id)
VALUES (
    (SELECT playlist_id FROM Playlists WHERE name = 'My Chill Mix' AND user_id = (SELECT user_id FROM Users WHERE username = 'mira_k')),
    (SELECT music_id FROM Music WHERE title = 'Chill Vibes')
);

INSERT INTO PlaylistMusic (playlist_id, music_id)
VALUES (
    (SELECT playlist_id FROM Playlists WHERE name = 'My Chill Mix' AND user_id = (SELECT user_id FROM Users WHERE username = 'mira_k')),
    (SELECT music_id FROM Music WHERE title = 'Forest Whispers')
);

-- Yuki's Focus Zone
INSERT INTO PlaylistMusic (playlist_id, music_id)
VALUES (
    (SELECT playlist_id FROM Playlists WHERE name = 'Focus Zone' AND user_id = (SELECT user_id FROM Users WHERE username = 'yuki_premium')),
    (SELECT music_id FROM Music WHERE title = 'Midnight Study')
);
COMMIT;

-- === Insert Sample Likes ===
INSERT INTO Likes (user_id, music_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'mira_k'),
    (SELECT music_id FROM Music WHERE title = 'Chill Vibes')
);

INSERT INTO Likes (user_id, music_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    (SELECT music_id FROM Music WHERE title = 'Midnight Study')
);

INSERT INTO Likes (user_id, music_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'admin_user'),
    (SELECT music_id FROM Music WHERE title = 'Forest Whispers')
);
COMMIT;

-- === Insert Sample UserMusicDailyStats ===
-- Mira listened to 'Chill Vibes' yesterday
INSERT INTO UserMusicDailyStats (user_id, music_id, stat_date, plays, skips, repeats)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'mira_k'),
    (SELECT music_id FROM Music WHERE title = 'Chill Vibes'),
    TRUNC(SYSDATE - 1), -- Yesterday
    5, 0, 1
);

-- Yuki listened to 'Midnight Study' today
INSERT INTO UserMusicDailyStats (user_id, music_id, stat_date, plays, skips, repeats)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    (SELECT music_id FROM Music WHERE title = 'Midnight Study'),
    TRUNC(SYSDATE), -- Today
    10, 1, 2
);
COMMIT;

SELECT 'Script execution completed. All tables created and populated with sample data.' AS status FROM dual;
