package lv.partner.restaurant.web.restaurant;

import lv.partner.restaurant.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static lv.partner.restaurant.RestaurantTestData.RESTAURANTS;
import static lv.partner.restaurant.RestaurantTestData.RESTAURANT_MATCHER;
import static lv.partner.restaurant.TestUtil.userHttpBasic;
import static lv.partner.restaurant.UserTestData.USER1;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantController.REST_URL + '/';

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANTS));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

}
