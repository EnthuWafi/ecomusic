package com.enth.ecomusic.controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.model.dao.UserDAO;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class ImageStreamServlet
 */
@WebServlet("/stream/image/user/*")
public class UserImageStreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserDAO userDAO;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		userDAO = new UserDAO();
	}
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserImageStreamServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo(); // e.g., "/123"
		int userId = CommonUtil.extractIdFromPath(pathInfo);

		if (userId == -1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
			return;
		}
		
		User user = userDAO.getUserById(userId);

		// Get image base path from context
		String basePath = getServletContext().getAttribute("imageFilePath").toString();
		String imagePath = basePath + "user/" + user.getImageUrl(); // or .png/.webp/etc

		File imageFile = new File(imagePath);
		
		// no image found?
		if (!imageFile.exists()) {
			response.sendRedirect(request.getContextPath() + "/assets/images/default.jpg");
			return;
		}

		// Infer MIME type (better than hardcoding)
		String mimeType = getServletContext().getMimeType(imageFile.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream"; // fallback
		}

		response.setContentType(mimeType);
		response.setContentLengthLong(imageFile.length());

		try (FileInputStream fis = new FileInputStream(imageFile); OutputStream out = response.getOutputStream()) {
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
	}

}
