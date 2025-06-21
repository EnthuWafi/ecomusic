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

/**
 * Servlet Filter implementation class FlashFilter Purpose of this filter is
 * simply to destroy the flash message in session and extract all messages if it
 * exists
 */
@WebFilter("/*")
public class FlashFilter extends HttpFilter implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpFilter#HttpFilter()
	 */
	public FlashFilter() {
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

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpReq = (HttpServletRequest) request;
			HttpSession session = httpReq.getSession(false);

			// Only for GET requests serving HTML (not API calls, not static)
			String method = httpReq.getMethod();
			String acceptHeader = httpReq.getHeader("Accept");
			boolean isHtml = acceptHeader != null && acceptHeader.contains("text/html");

			if ("GET".equalsIgnoreCase(method) && isHtml && session != null) {
				@SuppressWarnings("unchecked")
				List<Map<String, String>> flashMessages =
					(List<Map<String, String>>) session.getAttribute("flash_messages");

				if (flashMessages != null) {
					request.setAttribute("flashMessages", flashMessages);
					session.removeAttribute("flash_messages");
					System.out.println(flashMessages);
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
