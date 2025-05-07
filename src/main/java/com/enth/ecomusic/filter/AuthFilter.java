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

import com.enth.ecomusic.model.User;
import com.enth.ecomusic.util.CommonUtil;
import com.enth.ecomusic.util.ToastrType;

/**
 * Servlet Filter implementation class AuthFilter
 */
@WebFilter(urlPatterns = {"/user/*", "/admin/*", "/artist/*"})
public class AuthFilter extends HttpFilter implements Filter {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public AuthFilter() {
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
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
		HttpSession session = httpRequest.getSession(false);
		String uri = httpRequest.getRequestURI();
		
        // Check if user is logged in
        boolean loggedIn = session != null && session.getAttribute("user") != null;

        if (!loggedIn) {

            session = httpRequest.getSession();
            CommonUtil.addMessage(session, ToastrType.WARNING, "You must log in to access " + uri);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String userType = user.getUserType();
        
        //Role based filtering
        if (uri.startsWith(httpRequest.getContextPath() + "/admin") && !"admin".equals(userType) ) {
            CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Admins only");
            httpResponse.sendRedirect(httpRequest.getContextPath());
            return;
        }

        if (uri.startsWith(httpRequest.getContextPath() + "/artist") && !"artist".equals(userType) ) {
            CommonUtil.addMessage(session, ToastrType.ERROR, "Access denied: Artists only");
            httpResponse.sendRedirect(httpRequest.getContextPath());
            return;
        }
        
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
