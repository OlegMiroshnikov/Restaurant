package lv.partner.restaurant.web.dish;

import lv.partner.restaurant.model.Dish;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController extends AbstractDishController {

    static final String REST_URL = "/rest/admin/restaurants/{restaurantId}/dishes";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        Dish created = super.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        super.update(dish, restaurantId, id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        super.delete(id, restaurantId);
    }

    @Override
    @GetMapping("/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        return super.get(id, restaurantId);
    }

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
