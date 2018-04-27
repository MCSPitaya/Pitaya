package ch.pitaya.pitaya.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import ch.pitaya.pitaya.service.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider accessTokenProvider;
    
    @Autowired
    private TokenProvider refreshTokenProvider;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
        	boolean refreshToken = request.getServletPath().startsWith("/auth/");
        	
        	TokenProvider tokenProvider = refreshToken
        			? refreshTokenProvider
        			: accessTokenProvider;
        	
        	String token = getTokenFromRequest(request);

            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            	if (!refreshToken || tokenService.isTokenInStore(token)) {
            		Long userId = tokenProvider.getUserIdFromToken(token);
            		UserDetails userDetails = customUserDetailsService.loadUserById(userId, token);
            		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            		SecurityContextHolder.getContext().setAuthentication(authentication);
            	}
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
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
