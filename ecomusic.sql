-- Drop existing objects (order: triggers, tables, sequences) --

-- Drop Triggers
DROP TRIGGER trg_roles_bi;
DROP TRIGGER trg_users_bi;
DROP TRIGGER trg_users_bu;
DROP TRIGGER trg_subscription_plans_bi;
DROP TRIGGER trg_subscription_plans_bu;
DROP TRIGGER trg_subscriptions_bi;
DROP TRIGGER trg_subscriptions_bu;
DROP TRIGGER trg_music_bi;
DROP TRIGGER trg_music_bu;
DROP TRIGGER trg_playlists_bi;
DROP TRIGGER trg_playlists_bu;
DROP TRIGGER trg_genres_bi;
DROP TRIGGER trg_moods_bi;
DROP TRIGGER trg_playhistory_bi;
/

-- Drop Tables
DROP TABLE PlaylistMusic CASCADE CONSTRAINTS;
DROP TABLE Playlists CASCADE CONSTRAINTS;
DROP TABLE Likes CASCADE CONSTRAINTS;
DROP TABLE PlayHistory CASCADE CONSTRAINTS;
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
DROP SEQUENCE play_seq;
/

-- Drop the scheduler job (which also stops its scheduled runs)
BEGIN
    DBMS_SCHEDULER.DROP_JOB (
        job_name => 'MUSIC_CACHE_UPDATE_JOB',
        force    => TRUE
    );
END;
/

-- Drop the schedule (only if no other jobs use it)
BEGIN
    DBMS_SCHEDULER.DROP_SCHEDULE (
        schedule_name => 'MUSIC_CACHE_UPDATE_SCHEDULE',
        force         => TRUE
    );
END;
/

-- Drop the PL/SQL procedures
DROP PROCEDURE update_music_plays_cache;
DROP PROCEDURE update_music_likes_cache;

-- Repeat for your CTXSYS.CONTEXT related job and schedule if you drop the Music table entirely
BEGIN
    DBMS_SCHEDULER.DROP_JOB (
        job_name => 'SYNC_MUSIC_INDEX_JOB',
        force    => TRUE
    );
END;
/
BEGIN
    DBMS_SCHEDULER.DROP_SCHEDULE (
        schedule_name => 'SYNC_MUSIC_INDEX_SCHEDULE',
        force         => TRUE
    );
END;
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
    bio VARCHAR2(1000),
    username VARCHAR2(100) UNIQUE NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    image_url VARCHAR2(255),
    password VARCHAR2(255) NOT NULL,
    role_id NUMBER NOT NULL,
    is_premium NUMBER(1) DEFAULT 0 NOT NULL,
    is_artist NUMBER(1) DEFAULT 0 NOT NULL,
    created_at DATE DEFAULT CURRENT_TIMESTAMP,
    updated_at DATE DEFAULT CURRENT_TIMESTAMP,
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

CREATE OR REPLACE TRIGGER trg_users_bu
BEFORE UPDATE ON Users
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
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
    features CLOB,
    created_at DATE DEFAULT CURRENT_TIMESTAMP,
    updated_at DATE DEFAULT CURRENT_TIMESTAMP,
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

CREATE OR REPLACE TRIGGER trg_subscription_plans_bu
BEFORE UPDATE ON SubscriptionPlans
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- Subscriptions Table
CREATE TABLE Subscriptions (
    subscription_id NUMBER PRIMARY KEY,
    user_id NUMBER,
    start_date DATE,
    end_date DATE,
    amount_paid NUMBER(10, 2),
    payment_status VARCHAR2(10) CHECK (payment_status IN ('paid', 'failed', 'pending')),
    payment_gateway_ref VARCHAR2(255),
    created_at DATE DEFAULT CURRENT_TIMESTAMP,
    updated_at DATE DEFAULT CURRENT_TIMESTAMP,
    subscription_plan_id NUMBER,
    CONSTRAINT fk_subscription_plan FOREIGN KEY (subscription_plan_id) REFERENCES SubscriptionPlans(subscription_plan_id) ON DELETE CASCADE,
    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL
);

CREATE SEQUENCE subscription_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_subscriptions_bi
BEFORE INSERT ON Subscriptions
FOR EACH ROW
BEGIN
    SELECT subscription_seq.NEXTVAL INTO :NEW.subscription_id FROM dual;
END;
/
CREATE OR REPLACE TRIGGER trg_subscriptions_bu
BEFORE UPDATE ON Subscriptions
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- For subscription queries
CREATE INDEX idx_subscriptions_user ON Subscriptions(user_id);
CREATE INDEX idx_subscriptions_active ON Subscriptions(user_id, end_date);

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
    description VARCHAR2(4000),
    updated_at DATE DEFAULT CURRENT_TIMESTAMP,
    upload_date DATE DEFAULT CURRENT_TIMESTAMP,
    audio_file_url VARCHAR2(255),
    image_url VARCHAR2(255),
    premium_content NUMBER(1) DEFAULT 0 NOT NULL,
    genre_id NUMBER,
    mood_id NUMBER,
    like_count_cache NUMBER DEFAULT 0,
    total_plays_cache NUMBER DEFAULT 0,
    visibility VARCHAR2(50) DEFAULT 'public' CHECK (visibility IN ('public', 'private')),
    CONSTRAINT fk_music_artist FOREIGN KEY (artist_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_music_genre FOREIGN KEY (genre_id) REFERENCES Genres(genre_id) ON DELETE SET NULL,
    CONSTRAINT fk_music_mood FOREIGN KEY (mood_id) REFERENCES Moods(mood_id) ON DELETE SET NULL,
    CONSTRAINT chk_premium_content CHECK (premium_content IN (0, 1))
);

CREATE SEQUENCE music_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_music_bi
BEFORE INSERT ON Music
FOR EACH ROW
BEGIN
    SELECT music_seq.NEXTVAL INTO :NEW.music_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_music_bu
BEFORE UPDATE ON Music
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- For Music table queries
CREATE INDEX idx_music_artist_upload_date ON Music(artist_id, upload_date DESC);
CREATE INDEX idx_music_likes ON Music(like_count_cache DESC);
CREATE INDEX idx_music_plays ON Music(total_plays_cache DESC);
CREATE INDEX idx_music_genre ON Music(genre_id);
CREATE INDEX idx_music_mood ON Music(mood_id);
CREATE INDEX idx_music_upload_date ON Music(upload_date DESC);
CREATE INDEX idx_music_text ON Music(title)
  INDEXTYPE IS CTXSYS.CONTEXT;


-- Playlists Table
CREATE TABLE Playlists (
    playlist_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    name VARCHAR2(100),
    updated_at DATE DEFAULT CURRENT_TIMESTAMP,
    created_at DATE DEFAULT CURRENT_TIMESTAMP,
    visibility VARCHAR2(50) DEFAULT 'public' CHECK (visibility IN ('public', 'private')),
    CONSTRAINT fk_playlist_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE playlist_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_playlists_bi
BEFORE INSERT ON Playlists
FOR EACH ROW
BEGIN
    SELECT playlist_seq.NEXTVAL INTO :NEW.playlist_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_playlists_bu
BEFORE UPDATE ON Playlists
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE INDEX idx_playlists_user ON Playlists(user_id);

-- PlaylistMusic Table (Join Table)
CREATE TABLE PlaylistMusic (
    playlist_id NUMBER,
    music_id NUMBER,
    position NUMBER NOT NULL,
    updated_at DATE DEFAULT SYSDATE,
    added_at DATE DEFAULT SYSDATE,
    PRIMARY KEY (playlist_id, music_id),
    CONSTRAINT unq_playlist_position UNIQUE (playlist_id, position) deferrable initially deferred,
    CONSTRAINT fk_pm_playlist FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id) ON DELETE CASCADE,
    CONSTRAINT fk_pm_music FOREIGN KEY (music_id) REFERENCES Music(music_id) ON DELETE CASCADE
);

CREATE SEQUENCE play_seq START WITH 1 INCREMENT BY 1;

-- PlayHistory table
CREATE TABLE PlayHistory (
    play_id NUMBER PRIMARY KEY,
    user_id NUMBER,
    music_id NUMBER NOT NULL,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    listen_duration NUMBER DEFAULT 0,
    was_skipped NUMBER(1) DEFAULT 0,
    CONSTRAINT fk_playhistory_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL,
    CONSTRAINT fk_playhistory_music FOREIGN KEY (music_id) REFERENCES Music(music_id) ON DELETE CASCADE,
    CONSTRAINT chk_was_skipped CHECK (was_skipped IN (0, 1))
);

CREATE OR REPLACE TRIGGER trg_playhistory_bi
BEFORE INSERT ON PlayHistory
FOR EACH ROW
BEGIN
     SELECT play_seq.NEXTVAL INTO :NEW.play_id FROM dual;
END;
/

-- Essential indexes
CREATE INDEX idx_playhistory_user_time ON PlayHistory(user_id, played_at);
CREATE INDEX idx_playhistory_music_time ON PlayHistory(music_id, played_at);

-- UserMusicDailyStats Table (Rejected)
-- Daily aggregated stats (populated via batch job or trigger)
-- CREATE TABLE UserMusicDailyStats (
--     user_id     NUMBER NOT NULL,
--     music_id    NUMBER NOT NULL,
--     stat_date   DATE NOT NULL,
--     total_plays NUMBER DEFAULT 0,
--     total_skips NUMBER DEFAULT 0,
--     total_listen_time NUMBER DEFAULT 0,
--     PRIMARY KEY (user_id, music_id, stat_date),
--     CONSTRAINT fk_dailystats_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
--     CONSTRAINT fk_dailystats_music FOREIGN KEY (music_id) REFERENCES Music(music_id) ON DELETE CASCADE
-- );

-- -- Indexes for performance on UserMusicDailyStats
-- CREATE INDEX idx_dailystats_user_date ON UserMusicDailyStats(user_id, stat_date);
-- CREATE INDEX idx_dailystats_music_date ON UserMusicDailyStats(music_id, stat_date);
-- CREATE INDEX idx_dailystats_music ON UserMusicDailyStats(music_id);


-- Likes Table
CREATE TABLE Likes (
    user_id NUMBER NOT NULL,
    music_id NUMBER NOT NULL,
    liked_at DATE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, music_id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_music FOREIGN KEY (music_id) REFERENCES Music(music_id) ON DELETE CASCADE
);

CREATE INDEX idx_likes_music ON Likes(music_id);


-- Create a schedule that runs every 5 minutes (adjust FREQ and INTERVAL as needed)
BEGIN
  DBMS_SCHEDULER.CREATE_SCHEDULE(
    schedule_name   => 'SYNC_MUSIC_INDEX_SCHEDULE',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'FREQ=MINUTELY;INTERVAL=5',
    end_date        => NULL,
    comments        => 'Schedule for synchronizing idx_music_text every 5 minutes');
END;
/

-- Create a job that uses this schedule
BEGIN
  DBMS_SCHEDULER.CREATE_JOB (
    job_name        => 'SYNC_MUSIC_INDEX_JOB',
    schedule_name   => 'SYNC_MUSIC_INDEX_SCHEDULE',
    job_action      => 'BEGIN CTX_DDL.SYNC_INDEX(''idx_music_text''); END;',
    job_type        => 'PLSQL_BLOCK',
    enabled         => TRUE,
    comments        => 'Synchronize music full-text index');
END;
/

-- Procedure to update total_plays_cache
CREATE OR REPLACE PROCEDURE update_music_plays_cache IS
BEGIN
    -- This MERGE statement efficiently updates existing rows
    -- and potentially handles new music_ids in PlayHistory if needed (though FKs should prevent this)
    MERGE INTO Music M
    USING (
        SELECT music_id, COUNT(*) AS calculated_plays
        FROM PlayHistory
        GROUP BY music_id
    ) PH_AGG
    ON (M.music_id = PH_AGG.music_id)
    WHEN MATCHED THEN
        UPDATE SET M.total_plays_cache = PH_AGG.calculated_plays;

    COMMIT; -- Commit the changes made by MERGE
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK; -- Rollback if any error occurs
        RAISE;    -- Re-raise the exception
END;
/

-- Procedure to update like_count_cache (assuming a 'Likes' table)
CREATE OR REPLACE PROCEDURE update_music_likes_cache IS
BEGIN
    MERGE INTO Music M
    USING (
        SELECT music_id, COUNT(*) AS calculated_likes
        FROM Likes
        GROUP BY music_id
    ) L_AGG
    ON (M.music_id = L_AGG.music_id)
    WHEN MATCHED THEN
        UPDATE SET M.like_count_cache = L_AGG.calculated_likes;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

BEGIN
  DBMS_SCHEDULER.CREATE_SCHEDULE(
    schedule_name   => 'MUSIC_CACHE_UPDATE_SCHEDULE',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'FREQ=MINUTELY;INTERVAL=5', 
    end_date        => NULL,
    comments        => 'Schedule for updating music play and like caches every 5 minutes');
END;
/

-- Create a job that executes both cache update procedures
BEGIN
  DBMS_SCHEDULER.CREATE_JOB (
    job_name        => 'MUSIC_CACHE_UPDATE_JOB',
    schedule_name   => 'MUSIC_CACHE_UPDATE_SCHEDULE',
    job_action      => 'BEGIN update_music_plays_cache; update_music_likes_cache; END;', -- Calls both procedures
    job_type        => 'PLSQL_BLOCK',
    enabled         => TRUE,
    comments        => 'Job to update music play and like counts in Music table cache');
END;
/

-- Test Inserts --

-- === Insert Roles ===
INSERT INTO Roles (role_name) VALUES ('admin');
INSERT INTO Roles (role_name) VALUES ('superadmin');
INSERT INTO Roles (role_name) VALUES ('user');
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
INSERT INTO Users (
    first_name, last_name, bio, username, email, password,
    role_id, image_url, is_premium, is_artist
) VALUES (
    'Admin', 'User', 'Lead admin and system overseer', 'admin_user', 'admin@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', -- password123
    (SELECT role_id FROM Roles WHERE role_name = 'superadmin'),
    'https://placehold.co/150x150/E0E0E0/B0B0B0?text=Admin',
    0, 0
);

-- User: Artist 1
INSERT INTO Users (
    first_name, last_name, bio, username, email, password,
    role_id, image_url, is_premium, is_artist
) VALUES (
    'Rena', 'Saito', 'Indie electronic artist and lo-fi producer', 'lofi_rena', 'rena.saito@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',
    (SELECT role_id FROM Roles WHERE role_name = 'user'),
    'https://placehold.co/150x150/DDEBF7/6C7A89?text=Rena',
    0, 1
);

-- User: Artist 2
INSERT INTO Users (
    first_name, last_name, bio, username, email, password,
    role_id, image_url, is_premium, is_artist
) VALUES (
    'Kenji', 'Tanaka', 'Ambient soundscape creator', 'ambient_kenji', 'kenji.tanaka@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',
    (SELECT role_id FROM Roles WHERE role_name = 'user'),
    'https://placehold.co/150x150/F0E68C/A08D5F?text=Kenji',
    0, 1
);

-- User: Listener (Regular User)
INSERT INTO Users (
    first_name, last_name, bio, username, email, password,
    role_id, image_url, is_premium, is_artist
) VALUES (
    'Mira', 'Kobayashi', 'Just a casual listener who loves ambient music', 'mira_k', 'mira.kobayashi@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',
    (SELECT role_id FROM Roles WHERE role_name = 'user'),
    'https://placehold.co/150x150/FFDAB9/D291BC?text=Mira',
    0, 0
);

-- User: Listener (Premium User)
INSERT INTO Users (
    first_name, last_name, bio, username, email, password,
    role_id, image_url, is_premium, is_artist
) VALUES (
    'Yuki', 'Honda', 'Premium listener, enjoys ad-free experience.', 'yuki_premium', 'yuki.honda@example.com',
    'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f',
    (SELECT role_id FROM Roles WHERE role_name = 'user'),
    'https://placehold.co/150x150/ADD8E6/607B8B?text=Yuki',
    1, 0
);
COMMIT;

-- Yuki is a premium listener on monthly Listener Plan
INSERT INTO Subscriptions (user_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref, subscription_plan_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    SYSDATE,
    SYSDATE + INTERVAL '30' DAY,
    4.99,
    'paid',
    'STRIPE_TXN_001',
    (SELECT subscription_plan_id FROM SubscriptionPlans WHERE name = 'Listener Plan - Monthly')
);

-- Kenji is a creator on the Creator Plan
INSERT INTO Subscriptions (user_id, start_date, end_date, amount_paid, payment_status, payment_gateway_ref, subscription_plan_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'ambient_kenji'),
    SYSDATE,
    SYSDATE + INTERVAL '30' DAY,
    9.99,
    'paid',
    'STRIPE_TXN_002',
    (SELECT subscription_plan_id FROM SubscriptionPlans WHERE name = 'Creator Plan')
);
COMMIT;

-- Rena uploads a lo-fi track
INSERT INTO Music (artist_id, title, description, audio_file_url, image_url, genre_id, mood_id, premium_content)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'lofi_rena'),
    'Midnight Drive',
    'A smooth lo-fi beat perfect for late-night work sessions.',
    'https://cdn.example.com/audio/midnight_drive.mp3',
    'https://cdn.example.com/images/midnight_drive.jpg',
    (SELECT genre_id FROM Genres WHERE name = 'Lo-fi'),
    (SELECT mood_id FROM Moods WHERE name = 'Focus'),
    0
);

-- Kenji uploads an ambient soundscape
INSERT INTO Music (artist_id, title, description, audio_file_url, image_url, genre_id, mood_id, premium_content)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'ambient_kenji'),
    'Forest Echoes',
    'Immerse yourself in calming ambient forest sounds.',
    'https://cdn.example.com/audio/forest_echoes.mp3',
    'https://cdn.example.com/images/forest_echoes.jpg',
    (SELECT genre_id FROM Genres WHERE name = 'Ambient'),
    (SELECT mood_id FROM Moods WHERE name = 'Peaceful'),
    1
);
COMMIT;

-- Mira creates a public playlist
INSERT INTO Playlists (user_id, name, visibility)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'mira_k'),
    'Chill Ambience',
    'public'
);

-- Add music to playlist
INSERT INTO PlaylistMusic (playlist_id, music_id, position)
VALUES (
    (SELECT playlist_id FROM Playlists WHERE name = 'Chill Ambience'),
    (SELECT music_id FROM Music WHERE title = 'Midnight Drive'),
    1
);

INSERT INTO PlaylistMusic (playlist_id, music_id, position)
VALUES (
    (SELECT playlist_id FROM Playlists WHERE name = 'Chill Ambience'),
    (SELECT music_id FROM Music WHERE title = 'Forest Echoes'),
    2
);
COMMIT;

-- Yuki listens to 'Midnight Drive'
INSERT INTO PlayHistory (user_id, music_id, played_at, listen_duration, was_skipped)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    (SELECT music_id FROM Music WHERE title = 'Midnight Drive'),
    SYSDATE,
    180,
    0
);

-- Mira listens to 'Forest Echoes' and skips halfway
INSERT INTO PlayHistory (user_id, music_id, played_at, listen_duration, was_skipped)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'mira_k'),
    (SELECT music_id FROM Music WHERE title = 'Forest Echoes'),
    SYSDATE,
    60,
    1
);
COMMIT;

-- Mira likes Midnight Drive
INSERT INTO Likes (user_id, music_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'mira_k'),
    (SELECT music_id FROM Music WHERE title = 'Midnight Drive')
);

-- Yuki likes both tracks
INSERT INTO Likes (user_id, music_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    (SELECT music_id FROM Music WHERE title = 'Midnight Drive')
);

INSERT INTO Likes (user_id, music_id)
VALUES (
    (SELECT user_id FROM Users WHERE username = 'yuki_premium'),
    (SELECT music_id FROM Music WHERE title = 'Forest Echoes')
);
COMMIT;



SELECT 'Script execution completed. All tables created and populated with sample data.' AS status FROM dual;
