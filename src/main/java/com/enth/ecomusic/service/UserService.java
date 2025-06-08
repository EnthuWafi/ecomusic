package com.enth.ecomusic.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.enth.ecomusic.model.dao.UserDAO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.model.entity.Role;
import com.enth.ecomusic.model.entity.User;
import com.enth.ecomusic.model.enums.RoleType;
import com.enth.ecomusic.model.mapper.UserMapper;
import com.enth.ecomusic.util.AppConfig;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.FileTypeUtil;

import jakarta.servlet.http.Part;
import net.coobird.thumbnailator.Thumbnails;

public class UserService {

	private final UserDAO userDAO;
	private final RoleCacheService roleCacheService;

	public UserService(RoleCacheService roleCacheService) {
		this.userDAO = new UserDAO();
		this.roleCacheService = roleCacheService != null ? roleCacheService : new RoleCacheService();
	}

	// Register new user
	public boolean registerUserAccount(String fname, String lname, String username, String bio, String email,
			String password, Part imagePart, RoleType roleType) {

		String hashedPassword = CommonUtil.hashPassword(password);
		User user = new User(fname, lname, username, bio, email, hashedPassword);

		try {
			if (imagePart != null && imagePart.getSize() > 0) {
				String contentType = imagePart.getContentType();
				if (!contentType.startsWith("image/")) {
					throw new IllegalArgumentException("Invalid image file");
				}

				String imageDir = AppConfig.get("userImageFilePath");
				String imgExt = FileTypeUtil.getImageExtension((imagePart.getContentType()));
				String imageFileName = UUID.randomUUID().toString() + imgExt;
				String imagePath = imageDir + File.separator + imageFileName;

				Files.createDirectories(Paths.get(imageDir));

				File imageFile = new File(imagePath);
				imagePart.write(imageFile.getAbsolutePath());

				File thumbnailFile = new File(imageDir + File.separator + "thumb_" + imageFileName);
				Thumbnails.of(imageFile).size(300, 300).outputFormat(imgExt.replace(".", "")).toFile(thumbnailFile);

				user.setImageUrl(imageFileName);
			}
			return createUserWithRoleName(user, roleType);
		} catch (IOException | IllegalArgumentException e) {
			System.err.println("Error creating user: " + e.getMessage());
			return false;
		}

	}

	public boolean createUserWithRoleName(User user, RoleType roleType) {
		Role role = roleCacheService.getByType(roleType);
		if (role != null) {
			user.setRoleId(role.getRoleId());
			return userDAO.insertUser(user);
		}
		return false;
	}

	public UserDTO getUserDTOById(int userId) {
		User user = userDAO.getUserById(userId);
		UserDTO dto = UserMapper.INSTANCE.toDTO(user);
		if (dto != null) {
			fetchRoleName(dto);
		}
		return dto;
	}

	public UserDTO getUserDTOByUsernameOrEmail(String identifier) {
		User user = userDAO.getUserByUsernameOrEmail(identifier);
		UserDTO dto = UserMapper.INSTANCE.toDTO(user);
		if (dto != null) {
			fetchRoleName(dto);
		}
		return dto;
	}

	public UserDTO authenticateUser(String usernameOrEmail, String password) {
		User user = userDAO.getUserByUsernameOrEmail(usernameOrEmail);
		if (user != null && CommonUtil.checkPassword(password, user.getPassword())) {
			UserDTO dto = UserMapper.INSTANCE.toDTO(user);
			if (dto != null) {
				fetchRoleName(dto);
			}
			return dto;
		}
		;
		return null;
	}

	public List<UserDTO> getAllUsers() {
		List<User> userList = userDAO.getAllUsers();

		return userList.stream().map(user -> {
			UserDTO dto = UserMapper.INSTANCE.toDTO(user);
			fetchRoleName(dto);
			return dto;
		}).collect(Collectors.toList());
	}

	public boolean updateUser(User user) {
		return userDAO.updateUser(user);
	}

	public boolean updateUserWithRoleName(User user, RoleType roleType) {
		Role role = roleCacheService.getByType(roleType);
		if (role != null) {
			user.setRoleId(role.getRoleId());
			return userDAO.updateUser(user);
		}
		return false;
	}

	public boolean deleteUser(int userId) {
		return userDAO.deleteUser(userId);
	}

	private void fetchRoleName(UserDTO dto) {
		dto.setRoleName(roleCacheService.getById((dto.getRoleId())).getRoleName());
	}
}