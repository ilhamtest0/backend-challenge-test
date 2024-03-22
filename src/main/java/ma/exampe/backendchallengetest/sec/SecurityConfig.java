package ma.exampe.backendchallengetest.sec;

import ma.exampe.backendchallengetest.sec.entities.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // TODO: enable csrf
                .headers((headers) -> headers.disable())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/api/users/generate", "/api/users/batch").permitAll()
                        .anyRequest().authenticated()
                )
                //.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /*.oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults())
                )*/
                .httpBasic(Customizer.withDefaults())
                .getOrBuild();
    }



}
