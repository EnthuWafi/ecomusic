package com.enth.ecomusic.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.enth.ecomusic.dao.UserDAO;
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
	public boolean registerUserAccount(User user, Part imagePart, RoleType roleType, UserDTO currentUser) {
		if (currentUser != null) {
			if (!canCreateUser(currentUser)) {
				return false;
			}
		} else {
			// if current user is null, then that means its anon
			roleType = RoleType.USER;
		}

		String hashedPassword = CommonUtil.hashPassword(user.getPassword());
		user.setPassword(hashedPassword);
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

	public boolean updateUser(User user, Part imagePart, RoleType requestedRole, UserDTO currentUser) {
		if (currentUser == null) {
			return false;
		}

		User existingUser = userDAO.getUserById(user.getUserId());
		if (existingUser == null) {
			return false;
		}

		boolean canModify = canModifyUser(existingUser, currentUser);
		if (!canModify) {
			return false;
		}

		int finalRole;
		if (currentUser.isSuperAdmin() && requestedRole != null) {
			finalRole = roleCacheService.getByType(requestedRole).getRoleId();
		} else {
			finalRole = existingUser.getRoleId();
		}

		// if user modify is current user account
		boolean isDemotingSelf = existingUser.getUserId() == currentUser.getUserId() && currentUser.isSuperAdmin()
				&& finalRole != roleCacheService.getByType(RoleType.SUPERADMIN).getRoleId();

		if (isDemotingSelf) {
			int superAdminCount = this.getSuperAdminCount();
			if (superAdminCount <= 1) {
				System.err.println("Cannot demote the only superadmin.");
				return false;
			}
		}

		// Update password if provided
		if (user.getPassword() != null && !user.getPassword().isBlank()) {
			String hashedPassword = CommonUtil.hashPassword(user.getPassword());
			existingUser.setPassword(hashedPassword);
		}

		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setBio(user.getBio());
		existingUser.setUsername(user.getUsername());
		existingUser.setEmail(user.getEmail());
		existingUser.setRoleId(finalRole);

		try {
			if (imagePart != null && imagePart.getSize() > 0) {
				String contentType = imagePart.getContentType();
				if (!contentType.startsWith("image/")) {
					throw new IllegalArgumentException("Invalid image file");
				}

				String imageDir = AppConfig.get("userImageFilePath");
				
				String oldImageFileName = existingUser.getImageUrl();
				
				if (oldImageFileName != null && !oldImageFileName.isEmpty()) {
					Path oldImagePath = Paths.get(imageDir, oldImageFileName);
					Path oldThumbPath = Paths.get(imageDir, "thumb_" + oldImageFileName);

					if (Files.exists(oldImagePath)) Files.delete(oldImagePath);
					if (Files.exists(oldThumbPath)) Files.delete(oldThumbPath);
				}
				
				String imgExt = FileTypeUtil.getImageExtension(contentType);
				String imageFileName = UUID.randomUUID().toString() + imgExt;
				String imagePath = imageDir + File.separator + imageFileName;

				Files.createDirectories(Paths.get(imageDir));

				File imageFile = new File(imagePath);
				imagePart.write(imageFile.getAbsolutePath());

				File thumbnailFile = new File(imageDir + File.separator + "thumb_" + imageFileName);
				Thumbnails.of(imageFile).size(300, 300).outputFormat(imgExt.replace(".", "")).toFile(thumbnailFile);

				existingUser.setImageUrl(imageFileName);
			}

			return userDAO.updateUser(existingUser); // make sure this method saves all updated fields
		} catch (IOException | IllegalArgumentException e) {
			System.err.println("Error updating user: " + e.getMessage());
			return false;
		}
	}

	private boolean createUserWithRoleName(User user, RoleType roleType) {
		Role role = roleCacheService.getByType(roleType);
		if (role != null) {
			user.setRoleId(role.getRoleId());
			return userDAO.insertUser(user);
		}
		return false;
	}

	public UserDTO getUserDTOById(int userId) {
		User user = userDAO.getUserById(userId);
		fetchRole(user);
		UserDTO dto = UserMapper.INSTANCE.toDTO(user);
		return dto;
	}

	public UserDTO getUserDTOByUsernameOrEmail(String identifier) {
		User user = userDAO.getUserByUsernameOrEmail(identifier);
		if (user != null) {
			fetchRole(user);
		}
		
		UserDTO dto = UserMapper.INSTANCE.toDTO(user);
		return dto;
	}

	public UserDTO getAuthenticatedUser(String usernameOrEmail, String password) {
		User user = userDAO.getUserByUsernameOrEmail(usernameOrEmail);

		if (user != null && CommonUtil.checkPassword(password, user.getPassword())) {
			fetchRole(user);
			UserDTO dto = UserMapper.INSTANCE.toDTO(user);
			return dto;
		}
		;
		return null;
	}

	public List<UserDTO> getAllUserDTO(int offset, int limit) {
		List<User> userList = userDAO.getAllUserWithOffsetLimit(offset, limit);

		return userList.stream().map(user -> {
			fetchRole(user);
			UserDTO dto = UserMapper.INSTANCE.toDTO(user);
			return dto;
		}).collect(Collectors.toList());
	}

	public boolean updateUserSetRole(int userId, RoleType role, UserDTO currentUser) {

		User existingUser = userDAO.getUserById(userId);
		if (existingUser == null) {
			return false;
		}

		boolean canModify = canModifyUser(existingUser, currentUser);
		if (!canModify) {
			return false;
		}

		int finalRole;
		if (currentUser.isSuperAdmin() && role != null) {
			finalRole = roleCacheService.getByType(role).getRoleId();
		} else {
			return false;
		}

		// if user modify is current user account
		boolean isDemotingSelf = existingUser.getUserId() == currentUser.getUserId() && currentUser.isSuperAdmin()
				&& finalRole != roleCacheService.getByType(RoleType.SUPERADMIN).getRoleId();

		if (isDemotingSelf) {
			int superAdminCount = this.getSuperAdminCount();
			if (superAdminCount <= 1) {
				System.err.println("Cannot demote the only superadmin.");
				return false;
			}
		}
		
		return userDAO.updateUserRole(userId, finalRole);
	}
	
	public boolean updateUserSetPremium(int userId, boolean premium, Connection conn) {
		return userDAO.updateUserSetPremium(userId, premium, conn);
	}

	public boolean updateUserSetArtist(int userId, boolean artist, Connection conn) {
		return userDAO.updateUserSetArtist(userId, artist, conn);
	}

	public boolean deleteUser(User user, UserDTO currentUser) {
		if (!canDeleteUser(user, currentUser)) {
			return false;
		}

		try {
			String imageDir = AppConfig.get("userImageFilePath");

			// === DELETE IMAGE + THUMBNAIL ===
			String imageFileName = user.getImageUrl();
			if (imageFileName != null && !imageFileName.isEmpty()) {
				File imageFile = new File(imageDir + File.separator + imageFileName);
				File thumbFile = new File(imageDir + File.separator + "thumb_" + imageFileName);

				if (imageFile.exists()) {
					Files.delete(imageFile.toPath());
				}

				if (thumbFile.exists()) {
					Files.delete(thumbFile.toPath());
				}
			}

			// === DELETE DB RECORD ===
			return userDAO.deleteUser(user.getUserId());

		} catch (IOException e) {
			System.err.println("Error deleting user files: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private void fetchRole(User user) {
		user.setRole(roleCacheService.getById(user.getRoleId()));
	}

	public User getUserById(int userId) {
		User user = userDAO.getUserById(userId);
		fetchRole(user);
		return user;
	}

	public List<User> getAllUsers() {
		List<User> userList = userDAO.getAllUsers();
		for (User user : userList) {
			fetchRole(user);
		}
		return userList;
	}

	public int getUserCount() {
		return userDAO.countAllUser();
	}

	public int getAdminCount() {
		return userDAO.countUserByRoleId(roleCacheService.getByType(RoleType.ADMIN).getRoleId());
	}

	public int getSuperAdminCount() {
		return userDAO.countUserByRoleId(roleCacheService.getByType(RoleType.SUPERADMIN).getRoleId());
	}

	public int getNormalUserCount() {
		return userDAO.countUserByRoleId(roleCacheService.getByType(RoleType.USER).getRoleId());
	}

	public int getArtistCount() {
		return userDAO.countAllArtist();
	}

	public int getPremiumCount() {
		return userDAO.countAllPremium();
	}

	public int getRegisteredUserTodayCount() {
		return userDAO.countRegisteredUserToday();
	}

	public boolean canCreateUser(UserDTO user) {
		return user != null && user.isSuperAdmin();
	}

	// allow user to modify own account
	public boolean canModifyUser(User user, UserDTO currentUser) {
		return user != null && currentUser != null
				&& (user.getUserId() == currentUser.getUserId() || currentUser.isSuperAdmin());
	}

	public boolean canDeleteUser(User user, UserDTO currentUser) {
		if (user == null || currentUser == null)
			return false;

		if (user.getUserId() == currentUser.getUserId())
			return false;

		return currentUser.isSuperAdmin();
	}

	public List<Role> getRoles() {
		return roleCacheService.getAll();
	}
	
	public Role getRoleById(int roleId) {
		return roleCacheService.getById(roleId);
	}
}