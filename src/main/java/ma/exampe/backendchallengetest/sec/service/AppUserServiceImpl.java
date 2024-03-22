package ma.exampe.backendchallengetest.sec.service;

import com.github.javafaker.Faker;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {
    private AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<AppUser> generateUsers(int count) {
        List<AppUser> users = new ArrayList<>();
        Faker faker = new Faker();
        for (int i = 0; i < count; i++) {
            AppUser user = new AppUser();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setBirthDate(faker.date().birthday());
            user.setCity(faker.address().city());
            user.setCountry(faker.address().countryCode());
            user.setAvatar(faker.internet().avatar());
            user.setCompany(faker.company().name());
            user.setJobPosition(faker.job().position());
            user.setMobile(faker.phoneNumber().cellPhone());
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(faker.internet().password(6, 10));
            user.setRole(faker.bool().bool() ? "admin" : "user");
            users.add(user);
        }
        return appUserRepository.saveAll(users);
    }
}
