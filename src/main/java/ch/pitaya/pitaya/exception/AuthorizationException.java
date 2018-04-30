package ch.pitaya.pitaya.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import ch.pitaya.pitaya.authorization.AuthCode;

/**
 * Exception for failed authorization (HTTP 401 - UNAUTHORIZED)
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4179206092536008913L;

	public AuthorizationException(AuthCode... requiredCodes) {
		super("Insufficient Authorization. Required Authorization Codes: " + Arrays.toString(requiredCodes));
	}

}
