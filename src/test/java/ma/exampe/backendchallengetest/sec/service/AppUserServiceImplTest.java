package ma.exampe.backendchallengetest.sec.service;

import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.enums.Role;
import ma.exampe.backendchallengetest.sec.repo.AppUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AppUserServiceImplTest {
    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateUsers() {
        // Mocking count parameter
        int count = 5;

        // Calling the generateUsers method
        List<AppUser> generatedUsers = appUserService.generateUsers(count);

        // Verifying the size of generated users list
        assertEquals(count, generatedUsers.size());
    }

    @Test
    public void testSaveUser() {
        // Mocking behavior of passwordEncoder
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // Creating a sample user
        AppUser user = new AppUser();
        user.setUsername("user1");
        user.setEmail("user1@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        // Calling the saveUser method
        appUserService.saveUser(user);

        // Verifying that password is hashed before saving
        assertEquals("hashedPassword", user.getPassword());

        // Verifying the interaction with passwordEncoder and appUserRepository
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(appUserRepository, times(1)).save(user);
    }

    @Test
    public void testLoadUserByUsername() {
        // Mocking behavior of appUserRepository
        when(appUserRepository.findByUsername(anyString())).thenReturn(Optional.of(new AppUser()));

        // Calling the loadUserByUsername method
        Optional<AppUser> userOptional = appUserService.loadUserByUsername("user1");

        // Verifying the returned user
        assertEquals(true, userOptional.isPresent());

        // Verifying the interaction with appUserRepository
        verify(appUserRepository, times(1)).findByUsername("user1");
    }
}
