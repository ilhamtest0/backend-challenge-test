package ma.exampe.backendchallengetest.sec;

import ma.exampe.backendchallengetest.sec.entities.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder();
    }
}
