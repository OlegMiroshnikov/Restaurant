package lv.partner.restaurant.service;

import lv.partner.restaurant.model.Vote;
import lv.partner.restaurant.repository.UserRepository;
import lv.partner.restaurant.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static lv.partner.restaurant.util.DateTimeUtil.theEndDateOrMax;
import static lv.partner.restaurant.util.DateTimeUtil.theStartDateOrMin;
import static lv.partner.restaurant.util.ValidationUtil.checkNotFound;
import static lv.partner.restaurant.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantService restaurantService;

    public Vote create(Vote vote, int restaurantId, int userId) {
        Assert.notNull(vote, "vote must not be null");
        Vote previousVote = voteRepository.getByDate(vote.getDate(), userId);
        if (previousVote != null) {
            vote.setId(previousVote.getId());
        }
        vote.setRestaurant(restaurantService.get(restaurantId));
        vote.setUser(userRepository.getOne(userId));
        return voteRepository.save(vote);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(voteRepository.delete(id, userId) != 0, id);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(voteRepository.get(id, userId), id);
    }

    public void deleteByDate(LocalDate date, int userId) {
        Assert.notNull(date, "date must not be null");
        delete(getByDate(date, userId).getId(), userId);
    }

    public Vote getByDate(LocalDate date, int userId) {
        Assert.notNull(date, "date must not be null");
        return checkNotFound(voteRepository.getByDate(date, userId),
                String.format("date=%s, userId=%d", date.toString(), userId));
    }

    public List<Vote> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return voteRepository.getBetween(theStartDateOrMin(startDate), theEndDateOrMax(endDate), userId);
    }

}
