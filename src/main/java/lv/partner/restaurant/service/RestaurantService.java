package lv.partner.restaurant.service;

import lv.partner.restaurant.model.Restaurant;
import lv.partner.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static lv.partner.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.getAll();
    }

}
