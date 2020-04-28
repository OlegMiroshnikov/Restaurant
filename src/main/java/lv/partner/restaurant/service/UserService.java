package lv.partner.restaurant.service;

import lv.partner.restaurant.AuthorizedUser;
import lv.partner.restaurant.model.User;
import lv.partner.restaurant.repository.UserRepository;
import lv.partner.restaurant.to.UserTo;
import lv.partner.restaurant.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static lv.partner.restaurant.util.UserUtil.prepareToSave;
import static lv.partner.restaurant.util.ValidationUtil.checkNotFound;
import static lv.partner.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        prepareAndSave(user);
    }

    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.getId());
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id) != 0, id);
    }

    public User get(int id) {
        return checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    public List<User> getAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(userRepository.getByEmail(email), "email=" + email);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    @Transactional
    public void enable(int id, boolean enabled) {
        User user = checkNotFoundWithId(get(id), id);
        user.setEnabled(enabled);
    }

    private User prepareAndSave(User user) {
        return userRepository.save(prepareToSave(user, passwordEncoder));
    }

}