package ma.exampe.backendchallengetest.sec.service;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.entities.ImportUsersSummary;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    List<AppUser> generateUsers(int count);
    ImportUsersSummary createUsersFromJson(String jsonContent);
    Optional<AppUser> loadUserByUsername(String username);
    void saveUser(AppUser user);
}

