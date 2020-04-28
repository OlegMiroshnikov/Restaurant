package lv.partner.restaurant.web.vote;

import lv.partner.restaurant.model.Vote;
import lv.partner.restaurant.service.VoteService;
import lv.partner.restaurant.util.SecurityUtil;
import lv.partner.restaurant.util.exception.NotPossibleVoteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static lv.partner.restaurant.util.ValidationUtil.checkNew;
import static lv.partner.restaurant.util.VoteUtil.VOTE_NOT_ACCEPTED;
import static lv.partner.restaurant.util.VoteUtil.VOTE_TIME;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserVoteController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest";

    @Autowired
    private VoteService voteService;

    @PostMapping(value = "/restaurants/{restaurantId}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createOrUpdate(@PathVariable int restaurantId) {
        int userId = SecurityUtil.authUserId();
        Vote vote = new Vote(LocalDate.now());
        if (LocalTime.now().isAfter(VOTE_TIME)) {
            log.info("{} for restaurant {} by user {} not accepted", vote, restaurantId, userId);
            throw new NotPossibleVoteException(VOTE_NOT_ACCEPTED);
        }
        log.info("create {} for restaurant {} by user {}", vote, restaurantId, userId);
        checkNew(vote);
        Vote created = voteService.create(vote, restaurantId, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
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
