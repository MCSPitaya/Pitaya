package ch.pitaya.pitaya.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.model.TokenStore;
import ch.pitaya.pitaya.repository.TokenStoreRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.security.Token;
import ch.pitaya.pitaya.security.TokenProvider;

@Service
public class TokenService {

	@Autowired
	private TokenProvider accessTokenProvider;

	@Autowired
	private TokenProvider refreshTokenProvider;

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private TokenStoreRepository tokenStoreRepository;

	public Token generateTokenPair() {
		Authentication auth = securityFacade.getAuthentication();
		Token token = new Token( //
				accessTokenProvider.generateToken(auth), //
				refreshTokenProvider.generateToken(auth));
		TokenStore store = new TokenStore(token.getRefreshToken());
		tokenStoreRepository.saveAndFlush(store);
		return token;
	}

	public Token replaceToken() {
		Token res = generateTokenPair();
		revokeToken();
		return res;
	}

	public boolean revokeToken() {
		String token = securityFacade.getCurrentToken();
		Optional<TokenStore> tokenStore = tokenStoreRepository.findByToken(token);
		if (tokenStore.isPresent()) {
			tokenStoreRepository.delete(tokenStore.get());
			tokenStoreRepository.flush();
			return true;
		} else {
			return false;
		}
	}

	public boolean isTokenInStore(String token) {
		Optional<TokenStore> tokenStore = tokenStoreRepository.findByToken(token);
		return tokenStore.isPresent();
	}

}
