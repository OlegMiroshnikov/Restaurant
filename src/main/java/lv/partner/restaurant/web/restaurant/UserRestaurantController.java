package lv.partner.restaurant.web.restaurant;

import lv.partner.restaurant.model.Restaurant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantController extends AbstractRestaurantController {

    static final String REST_URL = "/rest/restaurants";

    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }

}
