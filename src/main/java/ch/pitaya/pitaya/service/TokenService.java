package ch.pitaya.pitaya.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.model.Token;
import ch.pitaya.pitaya.repository.TokenRepository;
import ch.pitaya.pitaya.security.RawToken;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.security.TokenPair;
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
	private TokenRepository tokenStoreRepository;
	
	@Autowired
	private Logger logger;

	public TokenPair generateTokenPair() {
		Authentication auth = securityFacade.getAuthentication();
		RawToken accessToken = accessTokenProvider.generateToken(auth);
		RawToken refreshToken = refreshTokenProvider.generateToken(auth);
		Token store = new Token(
				securityFacade.getCurrentUser(),
				refreshToken.getToken(),
				refreshToken.getCreationDate(),
				refreshToken.getExpirationDate());
		tokenStoreRepository.saveAndFlush(store);
		return new TokenPair(accessToken, refreshToken);
	}

	public TokenPair replaceToken() {
		TokenPair res = generateTokenPair();
		revokeToken();
		return res;
	}

	public void revokeToken() {
		String token = securityFacade.getCurrentToken();
		Optional<Token> tokenStore = tokenStoreRepository.findByToken(token);
		if (tokenStore.isPresent()) {
			tokenStoreRepository.delete(tokenStore.get());
			tokenStoreRepository.flush();
		}
	}

	public boolean isTokenInStore(String token) {
		Optional<Token> tokenStore = tokenStoreRepository.findByToken(token);
		return tokenStore.isPresent();
	}

	@Scheduled(fixedRateString = "${pitaya.auth.refresh.deletionRate}")
	public void cleanTokenTable() {
		List<Token> tokens = tokenStoreRepository.findExpiredTokens();
		tokenStoreRepository.deleteAll(tokens);
		logger.get().info("deleted " + tokens.size() + " tokens");
	}

}
