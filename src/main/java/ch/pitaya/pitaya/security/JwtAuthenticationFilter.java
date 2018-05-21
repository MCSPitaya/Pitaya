package ch.pitaya.pitaya.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import ch.pitaya.pitaya.service.LogService;
import ch.pitaya.pitaya.service.Logger;
import ch.pitaya.pitaya.service.TokenService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider accessTokenProvider;

	@Autowired
	private TokenProvider refreshTokenProvider;

	@Autowired
	private LogService tracing;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private Logger logger;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			logger.get().info("authenticating a request for " + request.getServletPath() + " from "
					+ request.getRemoteAddr() + ":" + request.getRemotePort());
			boolean refreshToken = request.getServletPath().startsWith("/auth/");

			TokenProvider tokenProvider = refreshToken ? refreshTokenProvider : accessTokenProvider;

			String token = getTokenFromRequest(request);

			if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
				logger.get().info("token looks fine");
				if (!refreshToken || tokenService.isTokenInStore(token)) {
					Long userId = tokenProvider.getUserIdFromToken(token);
					UserPrincipal userDetails = customUserDetailsService.loadUserById(userId, token);
					logger.get().info("user authenticated: " + userDetails);
					if (userDetails.isActive()) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
					tracing.logRequest(request.getRemoteAddr(), request.getServletPath(), true, userId);
				} else {
					tracing.logRequest(request.getRemoteAddr(), request.getServletPath(), true, null);
				}
			} else {
				logger.get().warn("invalid authentication: " + token);
				tracing.logRequest(request.getRemoteAddr(), request.getServletPath(), token != null, null);
			}
		} catch (Exception ex) {
			logger.get().error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}
