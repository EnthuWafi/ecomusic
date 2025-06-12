package com.enth.ecomusic.controller.stream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.enth.ecomusic.model.dto.StreamRangeDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.FileStreamingService;
import com.enth.ecomusic.service.RoleCacheService;
import com.enth.ecomusic.service.UserService;
import com.enth.ecomusic.util.CommonUtil;

/**
 * Servlet implementation class ImageStreamServlet
 */
@WebServlet("/stream/image/user/*")
public class UserImageStreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;
	private FileStreamingService fileStreamingService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		RoleCacheService roleCache = (RoleCacheService) getServletContext().getAttribute("roleCacheService");
		userService = new UserService(roleCache);
		fileStreamingService = new FileStreamingService();
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

		String pathInfo = request.getPathInfo(); 
		int userId = CommonUtil.extractIdFromPath(pathInfo);

		if (userId == -1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
			return;
		}

		UserDTO user = userService.getUserDTOById(userId);

		String imageUrl = user.getImageUrl();
		if (imageUrl != null && imageUrl.startsWith("http")) {
		    response.sendRedirect(imageUrl);
		    return;
		}

		
		String basePath = getServletContext().getAttribute("userImageFilePath").toString();
		File imageFile = new File(basePath + imageUrl);

		if (!imageFile.exists()) {
			response.sendRedirect(request.getContextPath() + "/assets/images/default.jpg");
			return;
		}

		String mimeType = getServletContext().getMimeType(imageFile.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		String rangeHeader = request.getHeader("Range");

		StreamRangeDTO range = fileStreamingService.parseRangeHeader(rangeHeader, imageFile.length());
		

		response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(mimeType);
        response.setContentLengthLong(range.getContentLength());

        if (range.getStart() > 0 || range.getEnd() < range.getTotalLength() - 1) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + range.getStart() + "-" + range.getEnd() + "/" + range.getTotalLength());
        }
        
        try (OutputStream out = response.getOutputStream()) {
            fileStreamingService.streamFile(imageFile, range, out);
        }
	}

}
