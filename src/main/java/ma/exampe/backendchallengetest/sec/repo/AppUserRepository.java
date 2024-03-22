package ma.exampe.backendchallengetest.sec.repo;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

