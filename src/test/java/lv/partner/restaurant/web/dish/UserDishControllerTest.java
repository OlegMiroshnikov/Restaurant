package lv.partner.restaurant.web.dish;

import lv.partner.restaurant.model.Dish;
import lv.partner.restaurant.service.DishService;
import lv.partner.restaurant.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static lv.partner.restaurant.DishTestData.DISHES;
import static lv.partner.restaurant.DishTestData.DISH_MATCHER;
import static lv.partner.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static lv.partner.restaurant.TestUtil.userHttpBasic;
import static lv.partner.restaurant.UserTestData.USER1;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserDishControllerTest extends AbstractControllerTest {

    private final String REST_URL = UserDishController.REST_URL
            .replace("{restaurantId}", String.valueOf(RESTAURANT1_ID)) + "/";

    @Autowired
    private DishService dishService;

    @Test
    void getByNow() throws Exception {
        Dish newDish1 = new Dish("New dish1", new BigDecimal("10.00"), LocalDate.now());
        Dish newDish2 = new Dish("New dish2", new BigDecimal("15.00"), LocalDate.now());
        Dish createdDish1 = dishService.create(newDish1, RESTAURANT1_ID);
        newDish1.setId(createdDish1.getId());
        Dish createdDish2 = dishService.create(newDish2, RESTAURANT1_ID);
        newDish2.setId(createdDish2.getId());
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(List.of(newDish1, newDish2)));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-03-05")
                .param("endDate", "2020-03-06")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISHES));
    }

    @Test
    void getBetweenWithNullDates() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endDate=")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISHES));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }


}
