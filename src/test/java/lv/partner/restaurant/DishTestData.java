package lv.partner.restaurant;

import lv.partner.restaurant.model.Dish;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static lv.partner.restaurant.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingFieldsComparator(Dish.class, "restaurant");

    public static final int DISH1_ID = START_SEQ + 8;

    public static final Dish DISH1 = new Dish(DISH1_ID, "dish1_rest1", new BigDecimal("10.00"), of(2020, Month.MARCH, 5));
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "Dish2_rest1", new BigDecimal("15.00"), of(2020, Month.MARCH, 5));
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "Dish3_rest1", new BigDecimal("15.00"), of(2020, Month.MARCH, 6));
    public static final Dish DISH4 = new Dish(DISH1_ID + 5, "dish4_rest1", new BigDecimal("20.70"), LocalDate.now());
    public static final Dish DISH5 = new Dish(DISH1_ID + 6, "Dish5_rest1", new BigDecimal("50.00"), LocalDate.now());
    public static final List<Dish> DISHES = List.of(DISH5, DISH4, DISH3, DISH2, DISH1);
    public static final List<Dish> DISHES_NOW = List.of(DISH5, DISH4);

    public static Dish getNew() {
        return new Dish(null, "New dish", new BigDecimal("100.00"), of(2020, Month.MARCH, 5));
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Updated dish", new BigDecimal("100.00"), DISH1.getDate());
    }
}
