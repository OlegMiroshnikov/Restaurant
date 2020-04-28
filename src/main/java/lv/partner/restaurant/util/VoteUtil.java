package lv.partner.restaurant.util;

import java.time.LocalTime;

public class VoteUtil {

    private VoteUtil() {
    }

    public static final LocalTime VOTE_TIME = LocalTime.of(11, 00);
    public static final String VOTE_NOT_ACCEPTED = "Voting has ended today, vote is not accepted";

}
