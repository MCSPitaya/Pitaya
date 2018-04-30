package ch.pitaya.pitaya.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.model.TokenStore;
import ch.pitaya.pitaya.repository.TokenStoreRepository;
import ch.pitaya.pitaya.security.RawToken;
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
		RawToken accessToken = accessTokenProvider.generateToken(auth);
		RawToken refreshToken = refreshTokenProvider.generateToken(auth);
		Token token = new Token(accessToken.getToken(), refreshToken.getToken());
		TokenStore store = new TokenStore(refreshToken.getToken(), refreshToken.getExpirationDate());
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

	@Scheduled(fixedRateString = "${pitaya.auth.refresh.deletionRate}")
	public void cleanTokenTable() {
		List<TokenStore> tokens = tokenStoreRepository.findExpiredTokens();
		tokenStoreRepository.deleteAll(tokens);
		System.out.println("deleted " + tokens.size() + " tokens");
	}

}
