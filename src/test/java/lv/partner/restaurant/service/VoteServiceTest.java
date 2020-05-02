package lv.partner.restaurant.service;

import lv.partner.restaurant.model.Vote;
import lv.partner.restaurant.repository.VoteRepository;
import lv.partner.restaurant.util.exception.NotFoundException;
import lv.partner.restaurant.util.exception.NotPossibleVoteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static java.time.LocalDate.of;
import static lv.partner.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static lv.partner.restaurant.TestUtil.validateRootCause;
import static lv.partner.restaurant.UserTestData.*;
import static lv.partner.restaurant.VoteTestData.*;
import static lv.partner.restaurant.util.VoteUtil.VOTE_TIME;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class VoteServiceTest {
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteRepository voteRepository;

    @Test
    void createFirstTimeVote() {
        Vote newVote = new Vote(LocalDate.now());
        Vote created = voteService.create(newVote, RESTAURANT1_ID, USER1_ID);
        Integer newId = created.getId();
        newVote.setId(newId);
        newVote.setRestaurant(created.getRestaurant());
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.get(newId, USER1_ID), newVote);
    }

    @Test
    void updateVote() {
        voteService.create(new Vote(LocalDate.now()), RESTAURANT1_ID, USER1_ID);
        Vote repeatedVote = new Vote(LocalDate.now());
        if (LocalTime.now().isAfter(VOTE_TIME)) {
            assertThrows(NotPossibleVoteException.class,
                    () -> voteService.create(repeatedVote, RESTAURANT1_ID + 1, USER1_ID));
        } else {
            Vote created = voteService.create(repeatedVote, RESTAURANT1_ID + 1, USER1_ID);
            Integer newId = created.getId();
            repeatedVote.setId(newId);
            repeatedVote.setRestaurant(created.getRestaurant());
            VOTE_MATCHER.assertMatch(created, repeatedVote);
            VOTE_MATCHER.assertMatch(voteService.get(newId, USER1_ID), repeatedVote);
        }
    }

    @Test
    void createWithException() {
        validateRootCause(() -> voteService.create(new Vote(null), RESTAURANT1_ID, USER1_ID), ConstraintViolationException.class);
        validateRootCause(() -> voteService.create(new Vote(LocalDate.now().plusDays(1)), RESTAURANT1_ID, USER1_ID), ConstraintViolationException.class);
    }


    @Test
    void delete() {
        voteService.delete(VOTE1_ID, USER1_ID);
        assertNull(voteRepository.get(VOTE1_ID, USER1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> voteService.delete(100000, USER1_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class,
                () -> voteService.delete(VOTE1_ID, USER2_ID));
    }

    @Test
    void deleteByDate() {
        voteService.deleteByDate(of(2020, Month.MARCH, 5), USER1_ID);
        assertNull(voteRepository.get(VOTE1_ID, USER1_ID));
    }

    @Test
    void deleteByDateNotFound() {
        assertThrows(NotFoundException.class,
                () -> voteService.deleteByDate(of(2020, Month.MARCH, 7), USER1_ID));
    }

    @Test
    void deleteByNullDate() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> voteService.deleteByDate(null, USER1_ID));
        assertEquals("date must not be null", ex.getMessage());
    }


    @Test
    void get() {
        VOTE_MATCHER.assertMatch(voteService.get(VOTE1_ID, USER1_ID), VOTE1);
    }

    @Test
    void getAdminAsUserRoleVotes() {
        Vote actual = voteService.get(VOTE5.getId(), ADMIN_ID);
        VOTE_MATCHER.assertMatch(actual, VOTE5);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> voteService.get(100000, USER1_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class,
                () -> voteService.get(VOTE1_ID, USER2_ID));
    }

    @Test
    void getByDate() {
        VOTE_MATCHER.assertMatch(
                voteService.getByDate(of(2020, Month.MARCH, 5), USER1_ID), VOTE1);
    }

    @Test
    void getByDateNotFound() {
        assertThrows(NotFoundException.class,
                () -> voteService.getByDate(of(2020, Month.MARCH, 7), USER1_ID));
    }

    @Test
    void getByNullDate() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> voteService.getByDate(null, USER1_ID));
        assertEquals("date must not be null", ex.getMessage());
    }

    @Test
    void getBetween() {
        VOTE_MATCHER.assertMatch(voteService.getBetween(of(2020, 3, 5), LocalDate.of(2020, 3, 6), USER1_ID), VOTES);
    }

    @Test
    void getBetweenWithNullDates() throws Exception {
        VOTE_MATCHER.assertMatch(voteService.getBetween(null, null, USER1_ID), VOTES);
    }

}