package ch.pitaya.pitaya.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${pitaya.auth.access.secret}")
    private String accessSecret;

    @Value("${pitaya.auth.access.expiration}")
    private int accessExpiration;
    
    @Value("${pitaya.auth.refresh.secret}")
    private String refreshSecret;

    @Value("${pitaya.auth.refresh.expiration}")
    private int refreshExpiration;

    public String generateAccessToken(Authentication authentication) {
    	return generateToken(authentication, false);
    }
    
    public String generateRefreshToken(Authentication authentication) {
    	return generateToken(authentication, true);
    }
    
    private String generateToken(Authentication authentication, boolean refreshToken) {

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (refreshToken
        		? refreshExpiration
        		: accessExpiration));

        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, refreshToken
                		? refreshSecret
                		: accessSecret)
                .compact();
    }

    public Long getUserIdFromJWT(String token, boolean refreshToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(refreshToken ? refreshSecret : accessSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken, boolean refreshToken) {
        try {
            Jwts.parser().setSigningKey(refreshToken ? refreshSecret : accessSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
