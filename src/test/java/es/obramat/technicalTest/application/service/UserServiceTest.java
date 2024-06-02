package es.obramat.technicalTest.application.service;

import es.obramat.technicalTest.domain.model.security.User;
import es.obramat.technicalTest.infrastructure.persistence.security.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testLoadUserByUsername() {
        // Arrange
        String username = "user1";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userService.loadUserByUsername(username);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        // Arrange
        String username = "user1";

        // Act
        try {
            userService.loadUserByUsername(username);
            fail("UsernameNotFoundException should be thrown");
        } catch (UsernameNotFoundException e) {
            assertEquals(username, "user1");
        }
    }

    @Test
    void testCreateUser() {
        // Arrange
        String username = "user1";
        String password = "password";

        // Act
        userService.createUser(username, password);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testEncodePassword() {
        // Arrange
        String password = "password";
        String encodedPassword = "encoded_password";

        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

        // Act
        String result = userService.encodePassword(password);

        // Assert
        assertNotNull(result);
        assertEquals(encodedPassword, result);
    }
}