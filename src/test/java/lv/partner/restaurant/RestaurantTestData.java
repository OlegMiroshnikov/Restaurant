package lv.partner.restaurant;

import lv.partner.restaurant.model.Restaurant;

import java.util.List;

import static lv.partner.restaurant.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {

    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsComparator(Restaurant.class, "");

    public static final int RESTAURANT1_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "restaurant1");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT1_ID + 1, "Restaurant2");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT1_ID + 2, "Restaurant3");
    public static final Restaurant RESTAURANT4 = new Restaurant(RESTAURANT1_ID + 3, "Restaurant4");
    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT2, RESTAURANT3, RESTAURANT4, RESTAURANT1);

    public static Restaurant getNew() {
        return new Restaurant(null, "New restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Updated restaurant");
    }

}