package com.enth.ecomusic.controller.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.enth.ecomusic.model.dto.ChartDTO;
import com.enth.ecomusic.model.dto.MusicDTO;
import com.enth.ecomusic.model.dto.ReportKPIDTO;
import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.service.MusicService;
import com.enth.ecomusic.service.ReportService;
import com.enth.ecomusic.util.AppContext;
import com.enth.ecomusic.util.ResponseUtil;

/**
 * Servlet implementation class ReportAPIServlet
 */
@WebServlet("/api/report/*")
public class ReportAPIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private ReportService reportService;
	private MusicService musicService;
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		AppContext ctx = (AppContext) getServletContext().getAttribute("appContext");
		this.musicService = ctx.getMusicService();
		this.reportService = ctx.getReportService();
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportAPIServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.equals("/")) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			return;
		}
		
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");

		if (currentUser == null) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			return;
		}
		
		if (!(currentUser.isAdmin() || currentUser.isSuperAdmin())) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_FORBIDDEN, "User forbidden!");
			return;
		}

		String[] pathParts = pathInfo.substring(1).split("/");

		try {
			if (pathParts.length == 1 && "kpis".equals(pathParts[0])) {
				handleFetchKPI(request, response);

			} else if (pathParts.length == 1 && "chart".equals(pathParts[0])) {
				handleFetchChart(request, response);
				
			} else {
				ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");
			}
		} catch (NumberFormatException e) {
			ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "IDs must be numeric");
		}
	}
	private void handleFetchChart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String typeStr = StringUtils.defaultIfBlank(request.getParameter("type"), "plays");
	    String dateTypeStr = StringUtils.defaultIfBlank(request.getParameter("dateType"), "daily");
	    String startStr = request.getParameter("start");
	    String endStr = request.getParameter("end");

	    LocalDate start = null;
	    LocalDate end = null;

	    try {
	        if (StringUtils.isNotBlank(startStr)) {
	            start = LocalDate.parse(startStr); // expect format: yyyy-MM-dd
	        }
	        if (StringUtils.isNotBlank(endStr)) {
	            end = LocalDate.parse(endStr);
	        }
	    } catch (DateTimeParseException e) {
	        ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid date format. Use yyyy-MM-dd.");
	        return;
	    }

	    ChartDTO chartDTO;

	    switch (typeStr.toLowerCase()) {
	        case "plays":
	            chartDTO = reportService.getUserPlayChartDTO(start, end, dateTypeStr);
	            break;
	        case "music_uploads":
	            chartDTO = reportService.getMusicUploadChartDTO(start, end, dateTypeStr);
	            break;
	        case "revenue":
	        	chartDTO = reportService.getRevenueSumChartDTO(start, end, dateTypeStr);
	        	break;
	        case "user_growth":
	        	chartDTO = reportService.getUserGrowthChartDTO(start, end, dateTypeStr);
	        	break;
	        default:
	            ResponseUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Unsupported chart type: " + typeStr);
	            return;
	    }

	    Map<String, Object> data = new HashMap<>();
	    data.put("type", typeStr);
	    data.put("dateType", dateTypeStr);
	    data.put("start", startStr);
	    data.put("end", endStr);
	    data.put("results", chartDTO);

	    ResponseUtil.sendJson(response, data);
	}

	private void handleFetchKPI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Map<String, Object> data = new HashMap<>();
		ReportKPIDTO reportDTO = reportService.getReportKPIDTO();
		data.put("results", reportDTO);

		ResponseUtil.sendJson(response, data);
		
		
	}


}
