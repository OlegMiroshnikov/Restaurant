package lv.partner.restaurant.repository;

import lv.partner.restaurant.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id = :userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.id = :id AND v.user.id = :userId")
    Vote get(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.date = :date AND v.user.id = :userId")
    Vote getByDate(@Param("date") LocalDate date, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.user.id = :userId AND" +
            " v.date >= :startDate AND v.date <= :endDate ORDER BY v.date DESC, v.restaurant.name ASC ")
    List<Vote> getBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                          @Param("userId") int userId);

}
