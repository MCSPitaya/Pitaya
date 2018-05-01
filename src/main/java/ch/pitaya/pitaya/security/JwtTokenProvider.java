package ch.pitaya.pitaya.security;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtTokenProvider implements TokenProvider {

	private final String secret;
	private final long expiration;

	public JwtTokenProvider(String secret, long expiration) {
		this.secret = secret;
		this.expiration = expiration;
	}

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	public RawToken generateToken(Authentication authentication) {

		UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expiration);

		String token = Jwts.builder() //
				.setSubject(Long.toString(userDetails.getId())) //
				.setIssuedAt(new Date()) //
				.setExpiration(expiryDate) //
				.signWith(SignatureAlgorithm.HS512, secret) //
				.compact();
		return new RawToken(token, new Timestamp(now.getTime()), new Timestamp(expiryDate.getTime()));
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser() //
				.setSigningKey(secret) //
				.parseClaimsJws(token) //
				.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser() //
					.setSigningKey(secret) //
					.parseClaimsJws(authToken);
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
