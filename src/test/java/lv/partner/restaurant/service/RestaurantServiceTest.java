package lv.partner.restaurant.service;

import lv.partner.restaurant.RestaurantTestData;
import lv.partner.restaurant.model.Restaurant;
import lv.partner.restaurant.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static lv.partner.restaurant.RestaurantTestData.*;
import static lv.partner.restaurant.TestUtil.validateRootCause;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class RestaurantServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void create() {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        Restaurant created = restaurantService.create(newRestaurant);
        Integer newId = created.getId();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    void duplicateRestaurantCreate() {
        assertThrows(DataAccessException.class,
                () -> restaurantService.create(new Restaurant(null, "restaurant1")));
    }

    @Test
    void createWithException() {
        validateRootCause(() -> restaurantService.create(new Restaurant("")), ConstraintViolationException.class);
    }

    @Test
    void update() {
        Restaurant updated = RestaurantTestData.getUpdated();
        restaurantService.update(updated);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT1_ID), updated);
    }

    @Test
    void delete() {
        restaurantService.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class,
                () -> restaurantService.delete(RESTAURANT1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class,
                () -> restaurantService.delete(100000));
    }

    @Test
    void get() {
        Restaurant restaurant = restaurantService.get(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, RESTAURANT1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> restaurantService.get(100000));
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(restaurantService.getAll(), RESTAURANTS);
    }

}