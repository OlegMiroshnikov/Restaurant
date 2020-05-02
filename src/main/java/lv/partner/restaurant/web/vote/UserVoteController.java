package lv.partner.restaurant.web.vote;

import lv.partner.restaurant.model.Vote;
import lv.partner.restaurant.service.VoteService;
import lv.partner.restaurant.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVoteController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest";

    @Autowired
    private VoteService voteService;

    @PutMapping(value = "/restaurants/{restaurantId}/votes")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@PathVariable int restaurantId) {
        int userId = SecurityUtil.authUserId();
        Vote vote = new Vote(LocalDate.now());
        log.info("create {} for restaurant {} by user {}", vote, restaurantId, userId);
        voteService.create(vote, restaurantId, userId);
    }

    @DeleteMapping(value = "/votes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByNow() {
        int userId = SecurityUtil.authUserId();
        log.info("delete vote for date {} by user {}", LocalDate.now(), userId);
        voteService.deleteByDate(LocalDate.now(), userId);
    }

    @GetMapping(value = "/votes")
    public Vote getByNow() {
        int userId = SecurityUtil.authUserId();
        log.info("get vote for date {} by user {}", LocalDate.now(), userId);
        return voteService.getByDate(LocalDate.now(), userId);
    }

    @GetMapping(value = "/votes/filter")
    public List<Vote> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate) {
        int userId = SecurityUtil.authUserId();
        log.info("get votes between dates({} - {}) for user {}", startDate, endDate, userId);
        return voteService.getBetween(startDate, endDate, userId);
    }

}
