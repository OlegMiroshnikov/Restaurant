package lv.partner.restaurant.web.vote;

import lv.partner.restaurant.model.Vote;
import lv.partner.restaurant.service.VoteService;
import lv.partner.restaurant.util.exception.NotFoundException;
import lv.partner.restaurant.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static lv.partner.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static lv.partner.restaurant.TestUtil.readFromJson;
import static lv.partner.restaurant.TestUtil.userHttpBasic;
import static lv.partner.restaurant.UserTestData.USER1;
import static lv.partner.restaurant.UserTestData.USER1_ID;
import static lv.partner.restaurant.VoteTestData.VOTES;
import static lv.partner.restaurant.VoteTestData.VOTE_MATCHER;
import static lv.partner.restaurant.util.VoteUtil.VOTE_NOT_ACCEPTED;
import static lv.partner.restaurant.util.VoteUtil.VOTE_TIME;
import static lv.partner.restaurant.util.exception.ErrorType.VALIDATION_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {

    private final String REST_URL = UserVoteController.REST_URL + "/";

    @Autowired
    private VoteService voteService;

    @Test
    void createOrUpdate() throws Exception {
        if (!LocalTime.now().isAfter(VOTE_TIME)) {
            Vote newVote = new Vote(LocalDate.now());
            ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT1_ID + "/votes/")
                    .with(userHttpBasic(USER1))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated());
            Vote created = readFromJson(action, Vote.class);
            int newId = created.getId();
            newVote.setId(newId);
            newVote.setRestaurant(created.getRestaurant());
            VOTE_MATCHER.assertMatch(created, newVote);
            VOTE_MATCHER.assertMatch(voteService.get(newId, USER1_ID), newVote);
        }
    }

    @Test
    void notAccepted() throws Exception {
        if (LocalTime.now().isAfter(VOTE_TIME)) {
            perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT1_ID + "/votes/")
                    .with(userHttpBasic(USER1))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(errorType(VALIDATION_ERROR))
                    .andExpect(detailMessage(VOTE_NOT_ACCEPTED));
        }
    }

    @Test
    void deleteByNow() throws Exception {
        Vote newVote = new Vote(LocalDate.now());
        Vote created = voteService.create(newVote, RESTAURANT1_ID, USER1_ID);
        newVote.setId(created.getId());
        newVote.setRestaurant(created.getRestaurant());
        perform(MockMvcRequestBuilders.delete(REST_URL + "votes/")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.get(newVote.getId(), USER1_ID));
    }


    @Test
    void getByNow() throws Exception {
        Vote newVote = new Vote(LocalDate.now());
        Vote created = voteService.create(newVote, RESTAURANT1_ID, USER1_ID);
        newVote.setId(created.getId());
        newVote.setRestaurant(created.getRestaurant());
        perform(MockMvcRequestBuilders.get(REST_URL + "votes/")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(newVote));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "votes/filter/")
                .param("startDate", "2020-03-05")
                .param("endDate", "2020-03-06")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTES));
    }

    @Test
    void getBetweenWithNullDates() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "votes/filter?startDate=&endDate=")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTES));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "votes/"))
                .andExpect(status().isUnauthorized());
    }

}