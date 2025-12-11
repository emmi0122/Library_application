package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;

import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class UserDaoIntegrationTest {
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        //This way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }

    @Test
    void getUserById() {
        //This data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);

        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void getNonExistentUser() {
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get("99999");

        assertThat(maybeUser).isEmpty();
    }

    @Test
    void getLoginInfo() {
        final String username = "test";
        final UserId expectedUserId = UserId.of(1);

        UserDao userDao = new UserDao(ds);
        Optional<LoginInfo> maybeInfo = userDao.getLoginInfo(username);

        assertThat(maybeInfo).isPresent();
        assertThat(maybeInfo.get().getUserId()).isEqualTo(expectedUserId);
        assertThat(maybeInfo.get().getPasswordHash()).isNotNull();
    }

    @Test
    void getLoginInfoNonExistent() {
        UserDao userDao = new UserDao(ds);
        Optional<LoginInfo> maybeInfo = userDao.getLoginInfo("doesnotexist");

        assertThat(maybeInfo).isEmpty();
    }

    @Test
    void isNameAvaliable() {
        UserDao userDao = new UserDao(ds);

        assertThat(userDao.isNameAvailable("test")).isFalse();
        assertThat(userDao.isNameAvailable("newuser")).isTrue();
        assertThat(userDao.isNameAvailable("ab")).isFalse(); //Too short
    }

    @Test
    void registerNewUser() {
        UserDao userDao = new UserDao(ds);
        String username = "newuser";
        String realname = "New User";
        String password = "testpassword";

        boolean result = userDao.register(username, realname, password);
        assertThat(result).isTrue();

        //Verify the user was created
        Optional<User> maybeUser = userDao.get("3"); //Third user in test data
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
    }
}
