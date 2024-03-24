package ma.exampe.backendchallengetest.sec.web;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.enums.Role;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import ma.exampe.backendchallengetest.sec.shared.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "Authorization", description = "The Authorization API. Contains a secure hello method")
public class AuthorizationController {

    private final AppUserRepository userRepository;

    public AuthorizationController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/admin/resource")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE') and hasRole('ADMIN')")
    @Operation(
            description = "This endpoint require a valid JWT, ADMIN role with READ_PRIVILEGE",
            summary = "Hello secured endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "401"
                    )
            }
    )
    public ResponseEntity<String> sayHelloWithRoleAdminAndReadAuthority() {
        return ResponseEntity.ok("Hello, you have access to a protected resource that requires admin role and read authority.");
    }

    @GetMapping("/users/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            description = "This endpoint require a valid JWT, user logged in",
            summary = "user profile",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "401"
                    )
            }
    )
    public ResponseEntity<?> getOwnUser(@RequestHeader("Authorization") String authorizationHeader)
    {
        try {
            String token = Utils.extractToken(authorizationHeader);
            Claims claims = Utils.decodeToken(token);
            String userEmail = claims.get("sub", String.class);
            if (userEmail != null) {
                AppUser userProfile = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
                return ResponseEntity.ok().body(userProfile);
            } else {
                throw new RuntimeException("User email is null. Unable to get the profile.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage().toString());
        }
    }


    @GetMapping("/users/{username}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            description = "This endpoint require a valid JWT, ADMIN role or same authenticated user",
            summary = "user profile",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "401"
                    )
            }
    )
    public ResponseEntity<?> getOtherUser(@PathVariable("username") String username, @RequestHeader("Authorization") String authorizationHeader)
    {
        try {
            String token = Utils.extractToken(authorizationHeader);
            Claims claims = Utils.decodeToken(token);
            String requesterUserEmail = claims.get("sub", String.class);
            if (requesterUserEmail != null) {
                AppUser requesterUserProfile = userRepository.findByEmail(requesterUserEmail).orElseThrow(() -> new RuntimeException("Requester User not found"));
                String requesterUserProfileRole = requesterUserProfile.getRole().toString();
                if(requesterUserProfileRole == "ADMIN" ||  requesterUserProfile.getUsername() == username) {
                    AppUser requestedUserProfile = userRepository.findByUsername(requesterUserEmail).orElseThrow(() -> new RuntimeException("Requested User not found"));
                    return ResponseEntity.ok().body(requestedUserProfile);
                } else  {
                    throw new RuntimeException("User does not have right privileges");
                }
            } else {
                throw new RuntimeException("Requester not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage().toString());
        }
    }


    @DeleteMapping("/admin/resource")
    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE') and hasRole('ADMIN')")
    public ResponseEntity<String> sayHelloWithRoleAdminAndDeleteAuthority() {
        return ResponseEntity.ok("Hello, you have access to a protected resource that requires admin role and delete authority.");
    }
    @PostMapping("/user/resource")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE') and hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> sayHelloWithRoleUserAndCreateAuthority() {
        return ResponseEntity.ok("Hello, you have access to a protected resource that requires user role and write authority.");
    }
    @PutMapping("/user/resource")
    @PreAuthorize("hasAuthority('UPDATE_PRIVILEGE') and hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> sayHelloWithRoleUserAndUpdateAuthority() {
        return ResponseEntity.ok("Hello, you have access to a protected resource that requires user role and update authority.");
    }

}
