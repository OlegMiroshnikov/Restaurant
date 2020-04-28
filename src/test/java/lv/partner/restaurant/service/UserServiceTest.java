package lv.partner.restaurant.service;

import lv.partner.restaurant.model.Role;
import lv.partner.restaurant.model.User;
import lv.partner.restaurant.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.validation.ConstraintViolationException;
import java.util.Set;

import static lv.partner.restaurant.TestUtil.validateRootCause;
import static lv.partner.restaurant.UserTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void create() {
        User newUser = getNew();
        User created = userService.create(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class,
                () -> userService.create(new User(null, "Duplicate", "user1@mail.ru", "newPass", Role.USER)));
    }

    @Test
    void createWithException() {
        validateRootCause(() -> userService.create(new User(null, "  ", "user1@mail.ru", "password1", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> userService.create(new User(null, "User1", "  ", "password1", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> userService.create(new User(null, "User1", "user1@mail.ru", "  ", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> userService.create(new User(null, "User1", "user1 mail.ru", "password1", Role.USER)), ConstraintViolationException.class);
        validateRootCause(() -> userService.create(new User(null, "User1", "user1@mail.ru", "password1", true, null, Set.of())), ConstraintViolationException.class);
    }

    @Test
    void update() {
        User updated = getUpdated();
        userService.update(updated);
        USER_MATCHER.assertMatch(userService.get(USER1_ID), updated);
    }

    @Test
    void delete() {
        userService.delete(USER1_ID);
        assertThrows(NotFoundException.class,
                () -> userService.delete(USER1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class,
                () -> userService.delete(100000));
    }

    @Test
    void get() {
        USER_MATCHER.assertMatch(userService.get(ADMIN_ID), ADMIN);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> userService.get(100000));
    }

    @Test
    void getByEmail() {
        USER_MATCHER.assertMatch(userService.getByEmail("admin@gmail.com"), ADMIN);
    }

    @Test
    void getAll() {
        USER_MATCHER.assertMatch(userService.getAll(), ADMIN, USER1, USER2, USER3);
    }

}