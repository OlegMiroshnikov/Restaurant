package lv.partner.restaurant.service;

import lv.partner.restaurant.model.Dish;
import lv.partner.restaurant.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static lv.partner.restaurant.util.DateTimeUtil.theEndDateOrMax;
import static lv.partner.restaurant.util.DateTimeUtil.theStartDateOrMin;
import static lv.partner.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private RestaurantService restaurantService;

    @Transactional
    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        dish.setRestaurant(restaurantService.get(restaurantId));
        return dishRepository.save(dish);
    }

    @Transactional
    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        dish.setRestaurant(restaurantService.get(restaurantId));
        checkNotFoundWithId(dishRepository.save(dish), dish.getId());
    }

    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(dishRepository.delete(id, restaurantId) != 0, id);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(dishRepository.get(id, restaurantId), id);
    }

    public List<Dish> getByDate(LocalDate date, int restaurantId) {
        Assert.notNull(date, "date must not be null");
        return dishRepository.getByDate(date, restaurantId);
    }

    public List<Dish> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int restaurantId) {
        return dishRepository.getBetween(theStartDateOrMin(startDate), theEndDateOrMax(endDate), restaurantId);
    }


}
