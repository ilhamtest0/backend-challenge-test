package ma.exampe.backendchallengetest.sec.shared;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/***
 * @author ilham
 */
@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public String generatedValue(int length){
        StringBuilder returnValue = new StringBuilder();
        for(int i=0 ; i<length ; i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return  new String(returnValue);
    }
    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer ".length()
        }
        throw new IllegalArgumentException("Invalid or missing Bearer token");
    }

    public static Claims decodeToken(String token) {
        // Replace "your_secret_key_here" with your actual secret key used for signing the JWT
        return Jwts.parser().setSigningKey("586B633834416E396D7436753879382F423F4428482B4C6250655367566b5970").parseClaimsJws(token).getBody();
    }
}
