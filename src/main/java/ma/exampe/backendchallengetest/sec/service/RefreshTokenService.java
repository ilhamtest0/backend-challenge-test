package ma.exampe.backendchallengetest.sec.service;

import jakarta.servlet.http.HttpServletRequest;
import ma.exampe.backendchallengetest.sec.entities.RefreshToken;
import ma.exampe.backendchallengetest.sec.payload.request.RefreshTokenRequest;
import ma.exampe.backendchallengetest.sec.payload.response.RefreshTokenResponse;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();
}
