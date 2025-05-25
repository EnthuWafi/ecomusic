package com.enth.ecomusic.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import com.enth.ecomusic.model.Music;
import com.enth.ecomusic.model.dao.MusicDAO;
import com.enth.ecomusic.util.AppConfig;

import jakarta.servlet.http.Part;

public class MusicService {

    private final MusicDAO musicDAO;

    public MusicService() {
        this.musicDAO = new MusicDAO();
    }

    public boolean uploadMusic(int artistId, String title, String genre, String description, 
                               Part audioPart, boolean isPremium, String uploadDir) {
        try {
            // Validate file
            if (audioPart == null || audioPart.getSize() == 0 || !audioPart.getContentType().equals("audio/mpeg")) {
                throw new IllegalArgumentException("Invalid audio file");
            }
      
            // Generate unique filename
            String audioFileName = UUID.randomUUID().toString() + ".mp3";
            String audioPath = uploadDir + File.separator + audioFileName;

            Files.createDirectories(Paths.get(uploadDir));
            audioPart.write(audioPath);
            
            Music music = new Music();
            music.setArtistId(artistId);
            music.setTitle(title);
            music.setGenre(genre);
            music.setDescription(description);
            music.setAudioFileUrl(AppConfig.get("audioFilePath") + audioFileName); // Web-accessible path
            music.setImageUrl(null); // optional: handle later
            music.setPremiumContent(isPremium);

            return musicDAO.insertMusic(music);

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error uploading music: " + e.getMessage());
            return false;
        }
    }
}