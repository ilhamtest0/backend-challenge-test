package ma.exampe.backendchallengetest.sec.web;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.enums.Role;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import ma.exampe.backendchallengetest.sec.shared.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
/*
@Tag(name = "Authorization", description = "The Authorization API. Contains secure get user methods")
@SecurityRequirements() /*
    This API won't have any security requirements. Therefore, we need to override the default security requirement configuration
    with @SecurityRequirements()
    */
@RequiredArgsConstructor
public class AuthorizationController {

    private final AppUserRepository userRepository;

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
                String requesterUserRole = requesterUserProfile.getRole().toString();
                String requesterUserName = requesterUserProfile.getUsername();
                if(requesterUserRole.equals("ADMIN") || requesterUserName.equals(username)) { // ADMIN OR OWN
                    AppUser requestedUserProfile = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Requested User not found"));
                    //AppUser requestedUserProfile = userRepository.findByEmail(requesterUserEmail).orElseThrow(() -> new RuntimeException("Requested User not found"));
                    return ResponseEntity.ok().body(requestedUserProfile);
                } else  {
                    throw new RuntimeException("User does not have right privileges");
                }
            } else {
                throw new RuntimeException("Unable to get Requester data");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage().toString());
        }
    }

}
