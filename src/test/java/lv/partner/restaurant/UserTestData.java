package lv.partner.restaurant;

import lv.partner.restaurant.model.Role;
import lv.partner.restaurant.model.User;
import lv.partner.restaurant.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static lv.partner.restaurant.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {

    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsComparator(User.class, "registered", "roles", "password");

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static final int USER1_ID = START_SEQ;
    public static final int USER2_ID = START_SEQ + 1;
    public static final int USER3_ID = START_SEQ + 2;
    public static final int ADMIN_ID = START_SEQ + 3;

    public static final User USER1 = new User(USER1_ID, "User1", "user1@mail.ru", "password1", Role.USER);
    public static final User USER2 = new User(USER2_ID, "User2", "user2@mail.ru", "password2", Role.USER);
    public static final User USER3 = new User(USER3_ID, "User3", "user3@mail.ru", "password3", Role.USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);

    public static final List<User> USERS = List.of(ADMIN, USER1, USER2, USER3);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(USER1);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }


}
