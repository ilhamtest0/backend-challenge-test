package ma.exampe.backendchallengetest.sec.web;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.exampe.backendchallengetest.sec.entities.AppUser;
import ma.exampe.backendchallengetest.sec.entities.ImportUsersSummary;
import ma.exampe.backendchallengetest.sec.service.AppUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AppUserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AppUserRestControllerTest {

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private AppUserRestController appUserRestController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateUsers() {
        // Mocking the behavior of appUserService.generateUsers()
        int count = 5;
        List<AppUser> users = Arrays.asList(
                new AppUser(),
                new AppUser(),
                new AppUser(),
                new AppUser(),
                new AppUser()
        );
        when(appUserService.generateUsers(count)).thenReturn(users);

        // Calling the endpoint
        ResponseEntity<Resource> response = appUserRestController.generateUsers(count);

        // Verifying the response
        assertEquals(200, response.getStatusCodeValue());

        // Verifying the content type
        assertEquals("application/json", response.getHeaders().getContentType().toString());

        // Verifying the content disposition
        // Assert
        String expectedHeaderValue = "attachment; filename=users.json";
        String actualHeaderValue = response.getHeaders().getContentDisposition().toString();
        assertTrue(actualHeaderValue.equals(expectedHeaderValue) || actualHeaderValue.equals(expectedHeaderValue.replace("=", "=\"") + "\""));


        // Verifying the content
        ObjectMapper objectMapper = new ObjectMapper();
        List<AppUser> responseUsers;
        try {
            responseUsers = objectMapper.readValue(response.getBody().getInputStream(), new TypeReference<List<AppUser>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            responseUsers = Collections.emptyList();
        }
        assertEquals(users.size(), responseUsers.size());
        for (int i = 0; i < users.size(); i++) {
            assertEquals(users.get(i).getUsername(), responseUsers.get(i).getUsername());
        }
    }

    @Test
    public void testUploadUsersBatch() throws IOException {
        // Mocking the file
        String jsonContent = "[{\"username\":\"user1\"},{\"username\":\"user2\"},{\"username\":\"user3\"}]";
        MockMultipartFile file = new MockMultipartFile("file", "users.json", "application/json", jsonContent.getBytes());

        // Mocking the behavior of appUserService.createUsersFromJson()
        ImportUsersSummary importSummary = new ImportUsersSummary(3, 0, 0);
        when(appUserService.createUsersFromJson(jsonContent)).thenReturn(importSummary);

        // Calling the endpoint
        ResponseEntity<ImportUsersSummary> response = appUserRestController.uploadUsersBatch(file);

        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(importSummary, response.getBody());
    }

}
