package lv.partner.restaurant.service;

import lv.partner.restaurant.DishTestData;
import lv.partner.restaurant.model.Dish;
import lv.partner.restaurant.repository.DishRepository;
import lv.partner.restaurant.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDate.of;
import static lv.partner.restaurant.DishTestData.*;
import static lv.partner.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static lv.partner.restaurant.TestUtil.validateRootCause;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class DishServiceTest {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishRepository dishRepository;

    @Test
    void create() {
        Dish newDish = DishTestData.getNew();
        Dish created = dishService.create(newDish, RESTAURANT1_ID);
        Integer newId = created.getId();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishService.get(newId, RESTAURANT1_ID), newDish);
    }

    @Test
    void createWithException() {
        validateRootCause(() -> dishService.create(new Dish("  ", new BigDecimal("100.00"), LocalDate.now()), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish(null, new BigDecimal("100.00"), LocalDate.now()), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish("Dish1_rest1", new BigDecimal("-1.00"), LocalDate.now()), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish("Dish1_rest1", new BigDecimal("1000000.00"), LocalDate.now()), RESTAURANT1_ID), ConstraintViolationException.class);
        validateRootCause(() -> dishService.create(new Dish("Dish1_rest1", new BigDecimal("500.00"), null), RESTAURANT1_ID), ConstraintViolationException.class);
    }

    @Test
    void duplicateDateCreate() {
        assertThrows(DataAccessException.class,
                () -> dishService.create(new Dish("dish1_rest1", new BigDecimal("10.00"), of(2020, Month.MARCH, 5)), RESTAURANT1_ID));
    }

    @Test
    void update() {
        Dish updated = DishTestData.getUpdated();
        dishService.update(updated, RESTAURANT1_ID);
        DISH_MATCHER.assertMatch(dishService.get(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void updateNotFound() {
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> dishService.update(DISH1, RESTAURANT1_ID + 1));
        assertEquals("Not found entity with id=" + DISH1_ID, ex.getMessage());
    }

    @Test
    void delete() {
        dishService.delete(DISH1_ID, RESTAURANT1_ID);
        assertNull(dishRepository.get(DISH1_ID, RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> dishService.delete(100000, RESTAURANT1_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class,
                () -> dishService.delete(DISH1_ID, RESTAURANT1_ID + 1));
    }

    @Test
    void get() {
        DISH_MATCHER.assertMatch(dishService.get(DISH1_ID, RESTAURANT1_ID), DISH1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> dishService.get(100000, RESTAURANT1_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class,
                () -> dishService.get(DISH1_ID, RESTAURANT1_ID + 1));
    }

    @Test
    void getByDate() {
        DISH_MATCHER.assertMatch(dishService.getByDate(of(2020, 3, 5), RESTAURANT1_ID), DISH2, DISH1);
    }

    @Test
    void getBetween() {
        DISH_MATCHER.assertMatch(dishService.getBetween(of(2020, 3, 5), LocalDate.of(2020, 3, 6), RESTAURANT1_ID), DISHES);
    }

    @Test
    void getBetweenWithNullDates() {
        DISH_MATCHER.assertMatch(dishService.getBetween(null, null, RESTAURANT1_ID), DISHES);
    }


}