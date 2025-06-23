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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.enth.ecomusic.model.dto.UserDTO;
import com.enth.ecomusic.util.AppContext;

/**
 * Servlet Filter implementation class UpdateUserSessionFilter
 */
@WebFilter("/*")
public class UpdateUserSessionFilter extends HttpFilter implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpFilter#HttpFilter()
	 */
	public UpdateUserSessionFilter() {
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
		// TODO Auto-generated method stub
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpReq = (HttpServletRequest) request;
			HttpSession session = httpReq.getSession(false);

			if (session != null) {
				UserDTO sessionUser = (UserDTO) session.getAttribute("user");
				Long pendingSince = (Long) session.getAttribute("updatePendingSince");

				boolean shouldReload = sessionUser != null
		                && pendingSince != null
		                && System.currentTimeMillis() - pendingSince > 5000;
		                
				if (shouldReload) {
					AppContext ctx = (AppContext) this.getServletContext().getAttribute("appContext");
					UserDTO fresh = ctx.getUserService().getUserDTOById(sessionUser.getUserId());
					session.setAttribute("user", fresh);
					session.removeAttribute("updatePendingSince");
				}
			}

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
