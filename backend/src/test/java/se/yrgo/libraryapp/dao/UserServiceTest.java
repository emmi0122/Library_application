package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.services.UserService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @Test
    @SuppressWarnings("deprecation")
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash = "password";
        final LoginInfo info = new LoginInfo(id, passwordHash);
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));

        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    @SuppressWarnings("deprecation")
    void registerUser() {
        final String username = "newuser";
        final String realname = "New User";
        final String password = "password123";
        final String passwordHash = "password123";
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.register(username, realname, passwordHash)).thenReturn(true);

        UserService userService = new UserService(userDao, encoder);
        boolean result = userService.register(username, realname, password);

        assertThat(result).isTrue();
    }

    @Test
    @SuppressWarnings("deprecation")
    void isNameAvailableAndValid() {
        final String name = "validuser";
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.isNameAvailable(name)).thenReturn(true);

        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.isNameAvailable(name)).isTrue();
    }

    @Test
    @SuppressWarnings("deprecation")
    void isNameAvailableAndTooShort() {
        final String name = "ab";
        final PasswordEncoder endoer = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        UserService userService = new UserService(userDao, endoer);
        assertThat(userService.isNameAvailable(name)).isFalse();
    }

    @Test
    @SuppressWarnings("deprecation")
    void isNameAvailableAndIsNull() {
        final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.isNameAvailable(null)).isFalse();
    }

    @Test
    @SuppressWarnings("deprecation")
    void isNameAvailableAndAlreadyTaken() {
        final String name = "takenuser";
        final PasswordEncoder endoer = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

        when(userDao.isNameAvailable(name)).thenReturn(false);

        UserService userService = new UserService(userDao, endoer);
        assertThat(userService.isNameAvailable(name)).isFalse();
    }
}
