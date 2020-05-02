package lv.partner.restaurant;

import lv.partner.restaurant.model.Vote;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static lv.partner.restaurant.RestaurantTestData.RESTAURANT1;
import static lv.partner.restaurant.RestaurantTestData.RESTAURANT2;
import static lv.partner.restaurant.model.AbstractBaseEntity.START_SEQ;


public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsComparator(Vote.class, "user");

    public static final int VOTE1_ID = START_SEQ + 13;

    public static final Vote VOTE1 = new Vote(VOTE1_ID, of(2020, Month.MARCH, 5), RESTAURANT1);
    public static final Vote VOTE2 = new Vote(VOTE1_ID + 1, of(2020, Month.MARCH, 5), RESTAURANT1);
    public static final Vote VOTE3 = new Vote(VOTE1_ID + 2, of(2020, Month.MARCH, 5), RESTAURANT2);
    public static final Vote VOTE4 = new Vote(VOTE1_ID + 3, of(2020, Month.MARCH, 6), RESTAURANT1);
    public static final Vote VOTE5 = new Vote(VOTE1_ID + 4, of(2020, Month.MARCH, 6), RESTAURANT1);
    public static final List<Vote> VOTES = List.of(VOTE4, VOTE1);

}
