package ma.exampe.backendchallengetest.sec.service;

import ma.exampe.backendchallengetest.sec.entities.AppUser;

import java.util.List;

public interface AppUserService {
    List<AppUser> generateUsers(int count);
}

