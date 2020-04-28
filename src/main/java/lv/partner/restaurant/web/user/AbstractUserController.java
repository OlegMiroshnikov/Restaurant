package lv.partner.restaurant.web.user;

import lv.partner.restaurant.model.User;
import lv.partner.restaurant.service.UserService;
import lv.partner.restaurant.to.UserTo;
import lv.partner.restaurant.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static lv.partner.restaurant.util.ValidationUtil.assureIdConsistent;
import static lv.partner.restaurant.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        return userService.create(user);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        userService.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        userService.update(userTo);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        userService.delete(id);
    }

    public User get(int id) {
        log.info("get {}", id);
        return userService.get(id);
    }

    public List<User> getAll() {
        log.info("getAll");
        return userService.getAll();
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return userService.getByEmail(email);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        userService.enable(id, enabled);
    }

}