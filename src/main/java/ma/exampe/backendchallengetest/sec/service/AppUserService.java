package ma.exampe.backendchallengetest.sec.service;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.entities.ImportUsersSummary;

import java.util.List;

public interface AppUserService {
    List<AppUser> generateUsers(int count);
    ImportUsersSummary createUsersFromJson(String jsonContent);
    AppUser loadUserByUsername(String username);
    void saveUser(AppUser user);
}

