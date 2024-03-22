package ma.exampe.backendchallengetest.sec.web;

import com.google.gson.Gson;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.service.AppUserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppUserRestService {
    private AppUserService appUserService;

    public AppUserRestService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/api/users/generate")
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
}
