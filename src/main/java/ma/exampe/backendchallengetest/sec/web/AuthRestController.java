package ma.exampe.backendchallengetest.sec.web;


//import ma.exampe.backendchallengetest.sec.entities.PasswordEncoder;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private AppUserRepository appUserRepository;

    public AuthRestController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, AppUserRepository appUserRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
    }

    /*
    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    } */

}
