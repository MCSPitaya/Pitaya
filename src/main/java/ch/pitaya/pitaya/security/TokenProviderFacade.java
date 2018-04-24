package ch.pitaya.pitaya.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TokenProviderFacade {

	@Autowired
	private TokenProvider accessTokenProvider;

	@Autowired
	private TokenProvider refreshTokenProvider;

	public Token generateTokenPair(Authentication auth) {
		return new Token( //
				accessTokenProvider.generateToken(auth), //
				refreshTokenProvider.generateToken(auth));
	}

}
