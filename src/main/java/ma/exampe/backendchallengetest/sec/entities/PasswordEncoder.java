package ma.exampe.backendchallengetest.sec.entities;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matches(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
