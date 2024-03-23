package ma.exampe.backendchallengetest.sec.service;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.entities.ImportUsersSummary;
//import ma.exampe.backendchallengetest.sec.entities.PasswordEncoder;
import ma.exampe.backendchallengetest.sec.enums.Role;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
            user.setRole(Role.valueOf(faker.bool().bool() ? "ADMIN" : "USER"));
            users.add(user);
        }
        return users;
    }

    @Override
    public ImportUsersSummary createUsersFromJson(String jsonContent) {
        System.out.println(jsonContent);
        ImportUsersSummary importSummary = new ImportUsersSummary();
        Gson gson = new Gson();

        try {
            AppUser[] usersArray = gson.fromJson(jsonContent, AppUser[].class);
            List<AppUser> users = Arrays.asList(usersArray);

            int totalRecords = users.size();
            int importedRecords = 0;
            int failedRecords = 0;

            for (AppUser user : users) {
                System.out.println(user);
                if (!appUserRepository.existsByEmail(user.getEmail()) && !appUserRepository.existsByUsername(user.getUsername())) {
                    saveUser(user);
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

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void saveUser(AppUser user) {
        // Encoder le mot de passe avant de sauvegarder l'utilisateur
        // TODO: remove logs
        System.out.println(user.getEmail());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        appUserRepository.save(user);
    }
}
