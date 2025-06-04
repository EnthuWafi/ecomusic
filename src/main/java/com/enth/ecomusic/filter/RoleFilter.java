package com.enth.ecomusic.filter;

import jakarta.annotation.Priority;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet Filter implementation class RoleFilter
 */
@WebFilter({ "/admin/*", "/artist/*" })
@Priority(2)
public class RoleFilter extends HttpFilter implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpFilter#HttpFilter()
	 */
	public RoleFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession(false);
		UserDTO user = (UserDTO) session.getAttribute("user");
		String uri = httpRequest.getRequestURI();

		// role flags
		boolean isSuperadmin = user.isSuperAdmin();
		boolean isAdmin = user.isAdmin();
		boolean isArtist = user.isArtist();
		boolean isUser = user.isUser();

		if (uri.startsWith("/admin")) {
	        if (!(isSuperadmin || isAdmin)) {
	        	CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Admins only");
				httpResponse.sendRedirect(httpRequest.getContextPath());
				return;
	        }
	    }

		if (uri.startsWith("/artist")) {
	        if (!isArtist) {
	        	CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Artists only");
				httpResponse.sendRedirect(httpRequest.getContextPath());
				return;
	        }
	    }
	
		if (uri.startsWith("/user")) {
	        if (!(isUser || isArtist)) {
	        	CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Users only");
				httpResponse.sendRedirect(httpRequest.getContextPath());
				return;
	        }
	    }
	

		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
