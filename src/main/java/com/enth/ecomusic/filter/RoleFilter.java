package com.enth.ecomusic.filter;

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
@WebFilter({ "/admin/*", "/artist/*", "/user/*", "/become-artist", "/choose-plan"})
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
		
		if (session != null && (session.getAttribute("user") != null)) {
			UserDTO user = (UserDTO) session.getAttribute("user");
			
			String contextPath = httpRequest.getContextPath();
			String uri = httpRequest.getRequestURI();
			String path = uri.substring(contextPath.length());  // <- key fix

			// role flags
			boolean isSuperadmin = user.isSuperAdmin();
			boolean isAdmin = user.isAdmin();
			boolean isArtist = user.isArtist();
			boolean isUser = user.isUser();
			boolean isPremiumUser = user.isPremiumUser();

			if (path.startsWith("/admin")) {
			    if (!(isSuperadmin || isAdmin)) {
			        CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Admins only");
			        httpResponse.sendRedirect(contextPath + "/home");
			        return;
			    }
			}

			else if (path.startsWith("/artist")) {
			    if (!isArtist) {
			        CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Artists only");
			        httpResponse.sendRedirect(contextPath + "/home");
			        return;
			    }
			}

			else if (path.startsWith("/user")) {
			    if (!(isUser || isPremiumUser || isArtist)) {
			        CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Users only");
			        httpResponse.sendRedirect(contextPath + "/home");
			        return;
			    }
			}
			
			else if (path.startsWith("/become-artist")) {
			    if (isSuperadmin || isAdmin || isArtist) {
			        CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Admin and artist denied!");
			        httpResponse.sendRedirect(contextPath + "/home");
			        return;
			    }
			}
			
			else if (path.startsWith("/choose-plan")) {
			    if (isSuperadmin || isAdmin || isPremiumUser) {
			        CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Admin and premium user denied!");
			        httpResponse.sendRedirect(contextPath + "/home");
			        return;
			    }
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
