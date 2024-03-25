package ma.exampe.backendchallengetest.sec.repo;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.service.AppUserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppUserRepositoryTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByUsername() {
        // Mocking behavior of appUserRepository.findByUsername
        String username = "testuser";
        AppUser user = new AppUser();
        user.setUsername(username);
        when(appUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Calling the findByUsername method
        Optional<AppUser> userOptional = appUserRepository.findByUsername(username);

        // Verifying the returned user
        assertEquals(true, userOptional.isPresent());
        assertEquals(username, userOptional.get().getUsername());

        // Verifying the interaction with appUserRepository
        verify(appUserRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testFindByEmail() {
        // Mocking behavior of appUserRepository.findByEmail
        String email = "test@example.com";
        AppUser user = new AppUser();
        user.setEmail(email);
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Calling the findByEmail method
        Optional<AppUser> userOptional = appUserRepository.findByEmail(email);

        // Verifying the returned user
        assertEquals(true, userOptional.isPresent());
        assertEquals(email, userOptional.get().getEmail());

        // Verifying the interaction with appUserRepository
        verify(appUserRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindByEmailOrUsername() {
        // Mocking behavior of appUserRepository.findByEmailOrUsername
        String emailOrUsername = "testuser";
        AppUser user = new AppUser();
        user.setUsername(emailOrUsername);
        when(appUserRepository.findByEmailOrUsername(emailOrUsername)).thenReturn(Optional.of(user));

        // Calling the findByEmailOrUsername method
        Optional<AppUser> userOptional = appUserRepository.findByEmailOrUsername(emailOrUsername);

        // Verifying the returned user
        assertEquals(true, userOptional.isPresent());
        assertEquals(emailOrUsername, userOptional.get().getUsername());

        // Verifying the interaction with appUserRepository
        verify(appUserRepository, times(1)).findByEmailOrUsername(emailOrUsername);
    }

    @Test
    public void testExistsByEmail() {
        // Mocking behavior of appUserRepository.existsByEmail
        String email = "test@example.com";
        when(appUserRepository.existsByEmail(email)).thenReturn(true);

        // Calling the existsByEmail method
        boolean exists = appUserRepository.existsByEmail(email);

        // Verifying the existence of user
        assertEquals(true, exists);

        // Verifying the interaction with appUserRepository
        verify(appUserRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void testExistsByUsername() {
        // Mocking behavior of appUserRepository.existsByUsername
        String username = "testuser";
        when(appUserRepository.existsByUsername(username)).thenReturn(true);

        // Calling the existsByUsername method
        boolean exists = appUserRepository.existsByUsername(username);

        // Verifying the existence of user
        assertEquals(true, exists);

        // Verifying the interaction with appUserRepository
        verify(appUserRepository, times(1)).existsByUsername(username);
    }
}
