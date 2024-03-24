package ma.exampe.backendchallengetest.sec.repo;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    @Query("select appUser from AppUser appUser where appUser.email = ?1 OR appUser.username = ?1")
    Optional<AppUser> findByEmailOrUsername(String usernameOrEmail);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}

