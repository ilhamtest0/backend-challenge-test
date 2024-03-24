package ma.exampe.backendchallengetest.sec.web;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.entities.ImportUsersSummary;
import ma.exampe.backendchallengetest.sec.service.AppUserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "The user entry point contains diffrents APIs on user")
@SecurityRequirements() /*
    This API won't have any security requirements. Therefore, we need to override the default security requirement configuration
    with @SecurityRequirements()
    */
@RequiredArgsConstructor
public class AppUserRestController {
    private  final AppUserService appUserService;

    /*public AppUserRestController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }*/

    @GetMapping("/generate")
    @Operation(
            description = "This endpoint generates users giving a count parameter",
            summary = "generating users",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error",
                            responseCode = "500",
                            content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                            }
                    )
            }
    )
    public  ResponseEntity<Resource>  generateUsers(@RequestParam int count) {
        List<AppUser> users = appUserService.generateUsers(count);

        // Convert list of users to JSON
        String json = new Gson().toJson(users);

        // Créer un objet Resource à partir du contenu JSON
        ByteArrayResource resource = new ByteArrayResource(json.getBytes());

        // Définir les en-têtes de la réponse
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Retourner la réponse avec le fichier JSON en tant que contenu attaché
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }



    //@PostMapping("/batch")
    @RequestMapping(
            path = "/batch",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            description = "This endpoint uploads given users in a json file to db",
            summary = "upload users",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Error",
                            responseCode = "500",
                            content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                            }
                    )
            }
    )
    public ResponseEntity<ImportUsersSummary> uploadUsersBatch(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ImportUsersSummary(0, 0, 0));
        }

        try {
            byte[] bytes = file.getBytes();
            String jsonContent = new String(bytes);

            ImportUsersSummary importSummary = appUserService.createUsersFromJson(jsonContent);

            return ResponseEntity.ok().body(importSummary);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
