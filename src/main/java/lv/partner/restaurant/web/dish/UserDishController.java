package lv.partner.restaurant.web.dish;

import lv.partner.restaurant.model.Dish;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserDishController extends AbstractDishController {

    static final String REST_URL = "/rest/restaurants/{restaurantId}/dishes";

    @Override
    @GetMapping
    public List<Dish> getByNow(@PathVariable int restaurantId) {
        return super.getByNow(restaurantId);
    }

    @Override
    @GetMapping(value = "/filter")
    public List<Dish> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate,
            @PathVariable int restaurantId) {
        return super.getBetween(startDate, endDate, restaurantId);
    }

}
