package ma.exampe.backendchallengetest.sec.web;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.service.AppUserService;
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
    public List<AppUser> generateUsers(@RequestParam int count) {
        return appUserService.generateUsers(count);
    }
}
