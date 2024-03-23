package ma.exampe.backendchallengetest.sec.service.impl;


import lombok.RequiredArgsConstructor;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.enums.TokenType;
import ma.exampe.backendchallengetest.sec.payload.request.AuthenticationRequest;
//import ma.exampe.backendchallengetest.sec.payload.request.RegisterRequest;
import ma.exampe.backendchallengetest.sec.payload.response.AuthenticationResponse;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import ma.exampe.backendchallengetest.sec.service.AuthenticationService;
import ma.exampe.backendchallengetest.sec.service.JwtService;
import ma.exampe.backendchallengetest.sec.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service @Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AppUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;


    /*
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = AppUser.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType( TokenType.BEARER.name())
                .build();
    } */

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        System.out.println(request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType( TokenType.BEARER.name())
                .build();
    }
}
