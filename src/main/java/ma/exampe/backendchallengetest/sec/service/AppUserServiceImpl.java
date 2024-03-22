package ma.exampe.backendchallengetest.sec.service;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.entities.ImportUsersSummary;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
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
        return users;
    }

    @Override
    public ImportUsersSummary createUsersFromJson(String jsonContent) {
        ImportUsersSummary importSummary = new ImportUsersSummary();
        Gson gson = new Gson();

        try {
            AppUser[] usersArray = gson.fromJson(jsonContent, AppUser[].class);
            List<AppUser> users = Arrays.asList(usersArray);

            int totalRecords = users.size();
            int importedRecords = 0;
            int failedRecords = 0;

            for (AppUser user : users) {
                if (!appUserRepository.existsByEmail(user.getEmail()) && !appUserRepository.existsByUsername(user.getUsername())) {
                    // Encodage du mot de passe avant l'enregistrement
                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    user.setPassword(encodedPassword);

                    appUserRepository.save(user);
                    importedRecords++;
                } else {
                    failedRecords++;
                }
            }

            importSummary.setTotalRecords(totalRecords);
            importSummary.setImportedRecords(importedRecords);
            importSummary.setFailedRecords(failedRecords);

        } catch (Exception e) {
            // En cas d'erreur lors de l'importation, retourner un ImportSummary vide avec tous les enregistrements marqués comme échoués
            e.printStackTrace();
            importSummary.setTotalRecords(0);
            importSummary.setImportedRecords(0);
            importSummary.setFailedRecords(0);
        }

        return importSummary;
    }
}
