package ma.exampe.backendchallengetest;

import ma.exampe.backendchallengetest.sec.entities.PasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendChallengeTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendChallengeTestApplication.class, args);
    }

    /* @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */
}
