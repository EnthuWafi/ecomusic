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

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet Filter implementation class RoleFilter
 */
@WebFilter({"/admin/*", "/artist/*"})
@Priority(2)
public class RoleFilter extends HttpFilter implements Filter {
       
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (User) session.getAttribute("user");
        String uri = httpRequest.getRequestURI();

        if (user == null) {
            // User not logged in — skip role check
            // AuthFilter should have handled this
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        if (uri.startsWith("/admin") && !CommonUtil.isAdmin(user)) {
            CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Admins only");
            httpResponse.sendRedirect(httpRequest.getContextPath());
            return;
        }

        if (uri.startsWith("/artist") && !CommonUtil.isArtist(user)) {
            CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Artists only");
            httpResponse.sendRedirect(httpRequest.getContextPath());
            return;
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
