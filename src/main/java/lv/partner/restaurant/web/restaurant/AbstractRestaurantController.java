package lv.partner.restaurant.web.restaurant;

import lv.partner.restaurant.model.Restaurant;
import lv.partner.restaurant.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static lv.partner.restaurant.util.ValidationUtil.assureIdConsistent;
import static lv.partner.restaurant.util.ValidationUtil.checkNew;

public abstract class AbstractRestaurantController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService restaurantService;

    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return restaurantService.create(restaurant);
    }

    public void update(Restaurant restaurant, int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        restaurantService.delete(id);
    }

    public Restaurant get(int id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

}
