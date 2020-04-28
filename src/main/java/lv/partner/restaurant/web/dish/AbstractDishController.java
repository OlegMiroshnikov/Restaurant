package lv.partner.restaurant.web.dish;

import lv.partner.restaurant.model.Dish;
import lv.partner.restaurant.service.DishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;

import static lv.partner.restaurant.util.ValidationUtil.assureIdConsistent;
import static lv.partner.restaurant.util.ValidationUtil.checkNew;

public abstract class AbstractDishController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishService dishService;

    public Dish create(Dish dish, int restaurantId) {
        log.info("create {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        return dishService.create(dish, restaurantId);
    }

    public void update(Dish dish, int restaurantId, int id) {
        log.info("update {} for restaurant {}", dish, restaurantId);
        assureIdConsistent(dish, id);
        dishService.update(dish, restaurantId);
    }

    public void delete(int id, int restaurantId) {
        log.info("delete dish {} for restaurant {}", id, restaurantId);
        dishService.delete(id, restaurantId);
    }

    public Dish get(int id, int restaurantId) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        return dishService.get(id, restaurantId);
    }

    public List<Dish> getByNow(int restaurantId) {
        log.info("get dishes by now {} for restaurant {}", LocalDate.now(), restaurantId);
        return dishService.getByDate(LocalDate.now(), restaurantId);
    }

    public List<Dish> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int restaurantId) {
        log.info("get dishes between dates({} - {}) for restaurant {}", startDate, endDate, restaurantId);
        return dishService.getBetween(startDate, endDate, restaurantId);
    }

}