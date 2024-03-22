package ma.exampe.backendchallengetest.sec.web;

import com.google.gson.Gson;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.entities.ImportUsersSummary;
import ma.exampe.backendchallengetest.sec.service.AppUserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserRestService {
    private AppUserService appUserService;

    public AppUserRestService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/generate")
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

    @PostMapping("/batch")
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
