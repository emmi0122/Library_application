package se.yrgo.libraryapp.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.jooby.Context;
import io.jooby.Cookie;
import io.jooby.StatusCode;
import se.yrgo.libraryapp.dao.*;
import se.yrgo.libraryapp.entities.*;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleDao roleDao;

    @Mock
    private SessionDao sessionDao;

    @Mock
    private Context context;

    private LoginController controller;

    @BeforeEach
    void setup() {
        controller = new LoginController(userService, roleDao, sessionDao);
    }

    @Test
    void successfulLogin() {
        String username = "testuser";
        String password = "testpass";
        LoginData loginData = new LoginData(username, password);
        UserId userId = UserId.of(1);
        UUID sessionId = UUID.randomUUID();
        List<Role> roles = List.of(Role.USER);

        when(userService.validate(username, password)).thenReturn(Optional.of(userId));
        when(sessionDao.create(userId)).thenReturn(sessionId);
        when(roleDao.get(userId)).thenReturn(roles);

        List<Role> result = controller.login(context, null, loginData);

        assertThat(result).containsExactly(Role.USER);
        verify(context).setResponseCookie(any(Cookie.class));
        verify(sessionDao).create(userId);
        verify(context, never()).setResponseCode(StatusCode.UNAUTHORIZED);
    }

    @Test
    void failedLoginWithWrongPassword() {
        String username = "testuser";
        String password = "wrongpassword";
        LoginData loginData = new LoginData(username, password);

        when(userService.validate(username, password)).thenReturn(Optional.empty());

        List<Role> result = controller.login(context, null, loginData);

        assertThat(result).isEmpty();
        verify(context).setResponseCode(StatusCode.UNAUTHORIZED);
        verify(context, never()).setResponseCookie(any(Cookie.class));
        verify(sessionDao, never()).create(any());
    }

    @Test
    void failedLoginWithNonExistentUser() {
        String username = "nonexistentuser";
        String password = "password";
        LoginData loginData = new LoginData(username, password);

        when(userService.validate(username, password)).thenReturn(Optional.empty());

        List<Role> result = controller.login(context, null, loginData);

        assertThat(result).isEmpty();
        ;
        verify(context).setResponseCode(StatusCode.UNAUTHORIZED);
        verify(sessionDao, never()).create(any());
    }

    @Test
    void loginWhenAlreadyLoggedIn() {
        String username = "testuser";
        String password = "testpassword";
        LoginData loginData = new LoginData(username, password);
        UUID existingSession = UUID.randomUUID();
        UserId userId = UserId.of(1);

        when(sessionDao.validate(existingSession)).thenReturn(userId);

        List<Role> result = controller.login(context, existingSession.toString(), loginData);

        assertThat(result).isEmpty();
        verify(userService, never()).validate(any(), any());
        verify(context, never()).setResponseCookie(any());
    }

    @Test
    void checkLoginWithValidSession() {
        UUID sessionId = UUID.randomUUID();
        UserId userId = UserId.of(1);
        List<Role> roles = List.of(Role.USER, Role.ADMIN);

        when(sessionDao.validate(sessionId)).thenReturn(userId);
        when(roleDao.get(userId)).thenReturn(roles);

        List<Role> result = controller.isLoggedIn(sessionId.toString());

        assertThat(result).containsExactlyInAnyOrder(Role.USER, Role.ADMIN);
    }

    @Test
    void CheckLoginWithInvalidSession() {
        UUID sessionId = UUID.randomUUID();

        when(sessionDao.validate(sessionId)).thenThrow(new IllegalArgumentException());

        List<Role> result = controller.isLoggedIn(sessionId.toString());

        assertThat(result).isEmpty();
    }

    @Test
    void checkLoginWithNullSession() {
        List<Role> result = controller.isLoggedIn(null);

        assertThat(result).isEmpty();
    }

    @Test
    void checkLoginWithMalformedSession() {
        List<Role> result = controller.isLoggedIn("not-a-valid-uuid");

        assertThat(result).isEmpty();
    }

    @Test
    void loginCreatesHttpOnlyCookie() {
        String username = "testuser";
        String password = "testpassword";
        LoginData loginData = new LoginData(username, password);
        UserId userId = UserId.of(1);
        UUID sessionId = UUID.randomUUID();

        when(userService.validate(username, password)).thenReturn(Optional.of(userId));
        when(sessionDao.create(userId)).thenReturn(sessionId);
        when(roleDao.get(userId)).thenReturn(List.of(Role.USER));

        controller.login(context, null, loginData);

        verify(context).setResponseCookie(argThat(cookie ->
            cookie.isHttpOnly() &&
            cookie.getName().equals("session") &&
            cookie.getValue().equals(sessionId.toString())
        ));
    }
}