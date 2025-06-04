package com.enth.ecomusic.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.dao.LikeDAO;
import com.enth.ecomusic.model.dao.MusicDAO;
import com.enth.ecomusic.model.dao.UserDAO;
import com.enth.ecomusic.model.dao.UserMusicDailyStatsDAO;
import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.MusicDetailDTO;
import com.enth.ecomusic.model.entity.Music;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.mapper.MusicMapper;
import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.FileTypeUtil;

import jakarta.servlet.http.Part;
import net.coobird.thumbnailator.Thumbnails;

public class MusicService {

    private final MusicDAO musicDAO;
    private final UserDAO userDAO;
    private final LikeDAO likeDAO;
    private final UserMusicDailyStatsDAO userMusicDailyStatsDAO;
    private final GenreCacheService genreCacheService;
    private final MoodCacheService moodCacheService;
    
    public MusicService(GenreCacheService genreCacheService, MoodCacheService moodCacheService) {
        this.musicDAO = new MusicDAO();
        this.userDAO = new UserDAO();
        this.likeDAO = new LikeDAO();
        this.userMusicDailyStatsDAO = new UserMusicDailyStatsDAO();
        this.genreCacheService = genreCacheService != null ? genreCacheService : new GenreCacheService();
        this.moodCacheService = moodCacheService != null? moodCacheService : new MoodCacheService();
    }

    public boolean uploadMusic(Music music, Part audioPart, Part imagePart) {
        try {
            if (audioPart == null || audioPart.getSize() == 0 || 
            		FileTypeUtil.getAudioExtension((audioPart.getContentType())) == null) {
                throw new IllegalArgumentException("Invalid audio file");
            }

            // === AUDIO FILE HANDLING ===
            
            String audioExt = FileTypeUtil.getAudioExtension( (audioPart.getContentType()) );
            String audioDir = AppConfig.get("audioFilePath");
            String audioFileName = UUID.randomUUID().toString() + audioExt;
            String audioPath = audioDir + File.separator + audioFileName;

            Files.createDirectories(Paths.get(audioDir));
            audioPart.write(audioPath);
            music.setAudioFileUrl(audioFileName);

            // === OPTIONAL IMAGE FILE HANDLING ===
            if (imagePart != null && imagePart.getSize() > 0) {
                String contentType = imagePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("Invalid image file");
                }

                String imageDir = AppConfig.get("musicImageFilePath");
                String imgExt = FileTypeUtil.getImageExtension((imagePart.getContentType()));
                String imageFileName = UUID.randomUUID().toString() + imgExt;
                String imagePath = imageDir + File.separator + imageFileName;

                Files.createDirectories(Paths.get(imageDir));
                
                File imageFile = new File(imagePath);
                imagePart.write(imageFile.getAbsolutePath());
                
                File thumbnailFile = new File(imageDir + File.separator + "thumb_" + imageFileName);
                Thumbnails.of(imageFile)
                          .size(300, 300)
                          .outputFormat(imgExt.replace(".", ""))
                          .toFile(thumbnailFile);
                
                music.setImageUrl(imageFileName);
            }

            return musicDAO.insertMusic(music);

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error uploading music: " + e.getMessage());
            return false;
        }
    }
    
    public MusicDTO getMusicDTOById(int musicId) {
    	Music music = musicDAO.getMusicById(musicId);
    	
    	MusicDTO dto = MusicMapper.INSTANCE.toDTO(music);
    	dto.setGenreName(genreCacheService.getById(music.getGenreId()).getName());
    	dto.setMoodName(moodCacheService.getById(music.getMoodId()).getName());
    	return dto;
    }
    
    public MusicDetailDTO getMusicDetailDTOById(int musicId) {
    	Music music = musicDAO.getMusicById(musicId);
    	User user = userDAO.getUserById(music.getArtistId());
    	int likeCount = likeDAO.countLikeByMusicId(musicId);
    	int viewCount = userMusicDailyStatsDAO.sumViewsByMusicId(musicId);
    	
    	MusicDetailDTO dto = MusicMapper.INSTANCE.toDetailDTO(music, user, likeCount, viewCount);
    	setGenreAndMoodMusicDTO(dto.getMusic());
    	return dto;
    }
    
    public List<MusicDTO> getAllMusicDTOsByArtistId(int artistId) {
        List<Music> musicList = musicDAO.getAllMusicByArtistId(artistId);
        
        return musicList.stream().map(music -> {
            MusicDTO dto = MusicMapper.INSTANCE.toDTO(music);
            dto.setGenreName(genreCacheService.getById(music.getGenreId()).getName());
            dto.setMoodName(moodCacheService.getById(music.getMoodId()).getName());
            return dto;
        }).collect(Collectors.toList());
    }
    
    
    public List<MusicDetailDTO> getPaginatedMusicDetailDTO(int page, int pageSize) {
        List<MusicDetailDTO> musicList = musicDAO.getPaginatedMusicWithDetail(page, pageSize);

        for (MusicDetailDTO dto : musicList) {
        	setGenreAndMoodMusicDTO(dto.getMusic());
        }
        return musicList;
    }
    
    public List<MusicDetailDTO> getPaginatedMusicDetailDTOLikeKeyword(String keyword, int page, int pageSize) {
        List<MusicDetailDTO> musicList = musicDAO.getPaginatedMusicWithDetailByKeyword(keyword, page, pageSize);

        for (MusicDetailDTO dto : musicList) {
        	setGenreAndMoodMusicDTO(dto.getMusic());
        }
        return musicList;
    }
    
    public int getMusicCount() {
    	return musicDAO.countMusic();
    }
    
    public int getMusicCountByKeyword(String keyword) {
    	return musicDAO.countMusicByKeyword(keyword);
    }
    
    public int getMusicCountByArtist(int artistId) {
    	return musicDAO.countMusicByArtist(artistId);
    }

	public List<MusicDetailDTO> getPaginatedMusicDetailDTOByArtistId(int artistId, int page, int pageSize) {
		List<MusicDetailDTO> musicList = musicDAO.getPaginatedMusicWithDetailByArtistId(artistId, page, pageSize);

        for (MusicDetailDTO dto : musicList) {
        	setGenreAndMoodMusicDTO(dto.getMusic());
        }
        return musicList;
	}
	
    private void setGenreAndMoodMusicDTO(MusicDTO dto) {
    	dto.setGenreName(genreCacheService.getById(dto.getGenreId()).getName());
        dto.setMoodName(moodCacheService.getById(dto.getMoodId()).getName());
    }
    
    
}