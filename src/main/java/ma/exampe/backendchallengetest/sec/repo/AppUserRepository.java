package ma.exampe.backendchallengetest.sec.repo;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

