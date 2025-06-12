package com.enth.ecomusic.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.dao.MusicDAO;
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
	private final UserService userService;
	private final GenreCacheService genreCacheService;
	private final MoodCacheService moodCacheService;

	public MusicService(UserService userService, GenreCacheService genreCacheService, MoodCacheService moodCacheService) {
		this.musicDAO = new MusicDAO();
		this.userService = userService;
		this.genreCacheService = genreCacheService != null ? genreCacheService : new GenreCacheService();
		this.moodCacheService = moodCacheService != null ? moodCacheService : new MoodCacheService();
	}

	public boolean uploadMusic(Music music, Part audioPart, Part imagePart) {
		try {
			if (audioPart == null || audioPart.getSize() == 0
					|| FileTypeUtil.getAudioExtension((audioPart.getContentType())) == null) {
				throw new IllegalArgumentException("Invalid audio file");
			}

			// === AUDIO FILE HANDLING ===

			String audioExt = FileTypeUtil.getAudioExtension((audioPart.getContentType()));
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
				Thumbnails.of(imageFile).size(300, 300).outputFormat(imgExt.replace(".", "")).toFile(thumbnailFile);

				music.setImageUrl(imageFileName);
			}

			return musicDAO.insertMusic(music);

		} catch (IOException | IllegalArgumentException e) {
			System.err.println("Error uploading music: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateMusic(Music music, Part audioPart, Part imagePart) {
		try {

			String oldAudioFileName = music.getAudioFileUrl();
			String oldImageFileName = music.getImageUrl();

			// === AUDIO FILE HANDLING ===
			if (audioPart != null && audioPart.getSize() > 0) {
				// New audio file provided
				String audioExt = FileTypeUtil.getAudioExtension(audioPart.getContentType());
				if (audioExt == null) {
					throw new IllegalArgumentException("Invalid audio file format.");
				}

				String audioDir = AppConfig.get("audioFilePath");
				String newAudioFileName = UUID.randomUUID().toString() + audioExt;
				String newAudioPath = audioDir + File.separator + newAudioFileName;

				Files.createDirectories(Paths.get(audioDir));
				audioPart.write(newAudioPath);
				music.setAudioFileUrl(newAudioFileName); // Update music object with new URL

				// Delete old audio file if it exists and a new one was uploaded
				if (oldAudioFileName != null && !oldAudioFileName.isEmpty()) {
					File oldAudioFile = new File(audioDir + File.separator + oldAudioFileName);
					if (oldAudioFile.exists()) {
						Files.delete(oldAudioFile.toPath());
					}
				}
			}

			// === OPTIONAL IMAGE FILE HANDLING ===
			if (imagePart != null && imagePart.getSize() > 0) {
				// New image file provided
				String contentType = imagePart.getContentType();
				if (!contentType.startsWith("image/")) {
					throw new IllegalArgumentException("Invalid image file format.");
				}

				String imageDir = AppConfig.get("musicImageFilePath");
				String imgExt = FileTypeUtil.getImageExtension(contentType);
				String newImageFileName = UUID.randomUUID().toString() + imgExt;
				String newImagePath = imageDir + File.separator + newImageFileName;

				Files.createDirectories(Paths.get(imageDir));
				File newImageFile = new File(newImagePath);
				imagePart.write(newImageFile.getAbsolutePath());

				// Generate thumbnail for the new image
				File newThumbnailFile = new File(imageDir + File.separator + "thumb_" + newImageFileName);
				Thumbnails.of(newImageFile).size(300, 300).outputFormat(imgExt.replace(".", ""))
						.toFile(newThumbnailFile);

				music.setImageUrl(newImageFileName); // Update music object with new URL

				// Delete old image files if they exist and a new one was uploaded
				if (oldImageFileName != null && !oldImageFileName.isEmpty()) {
					File oldImageFile = new File(imageDir + File.separator + oldImageFileName);
					File oldThumbnailFile = new File(imageDir + File.separator + "thumb_" + oldImageFileName);
					if (oldImageFile.exists()) {
						Files.delete(oldImageFile.toPath());
					}
					if (oldThumbnailFile.exists()) {
						Files.delete(oldThumbnailFile.toPath());
					}
				}
			}

			return musicDAO.updateMusic(music);

		} catch (IOException | IllegalArgumentException e) {
			System.err.println("Error updating music: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}


	public MusicDTO getMusicDTOById(int musicId) {
		Music music = this.getMusicById(musicId);
		MusicDTO dto = MusicMapper.INSTANCE.toDTO(music);

		return dto;
	}

	public MusicDetailDTO getMusicDetailDTOById(int musicId) {
		Music music = this.getMusicById(musicId);
		User user = userService.getUserById(music.getArtistId());
		music.setArtist(user);

		MusicDetailDTO dto = MusicMapper.INSTANCE.toDetailDTO(music);
		return dto;
	}

	public List<MusicDTO> getAllMusicDTOsByArtistId(int artistId) {
		List<Music> musicList = this.getAllMusicByArtistId(artistId);

		return musicList.stream().map(music -> {
			MusicDTO dto = MusicMapper.INSTANCE.toDTO(music);
			return dto;
		}).collect(Collectors.toList());
	}

	//special case
	public List<MusicDetailDTO> getPaginatedMusicDetailDTO(int page, int pageSize) {
		List<MusicDetailDTO> musicList = musicDAO.getRelevantPaginatedMusicWithDetail(page, pageSize);

		return musicList;
	}

	public List<MusicDetailDTO> getPaginatedMusicDetailDTOLikeKeyword(String keyword, List<Integer> genreIds,
			List<Integer> moodIds, int page, int pageSize) {
		List<MusicDetailDTO> musicList = musicDAO.getRelevantPaginatedMusicWithDetailByKeyword(keyword, genreIds,
				moodIds, page, pageSize);

		return musicList;
	}

	public int getMusicCount() {
		return musicDAO.countMusic();
	}

	public int getMusicCountByArtist(int artistId) {
		return musicDAO.countMusicByArtist(artistId);
	}

	public List<MusicDTO> getPaginatedMusicDTOByArtistId(int artistId, int page, int pageSize) {
		List<MusicDTO> musicList = musicDAO.getPaginatedMusicByArtistId(artistId, page, pageSize);

		return musicList;
	}

	public int getMusicCountLikeKeyword(String keyword, List<Integer> genreIdList, List<Integer> moodIdList) {
		return musicDAO.countMusicByKeyword(keyword, genreIdList, moodIdList);
	}
	
	private void setGenreMood(Music music) {
		if (music != null) {
			music.setGenre(genreCacheService.getById(music.getGenreId()));
			music.setMood(moodCacheService.getById(music.getMoodId()));
		}
	}

	public Music getMusicById(int musicId) {
		Music music = musicDAO.getMusicById(musicId);
		setGenreMood(music);
		return music;
	}
	
	public List<Music> getAllMusicByArtistId(int artistId) {
		List<Music> musicList = musicDAO.getAllMusicByArtistId(artistId);
		for (Music music : musicList) {
			setGenreMood(music);
		}
		return musicList;
	}


}