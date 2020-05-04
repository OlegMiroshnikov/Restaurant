package lv.partner.restaurant.repository;

import lv.partner.restaurant.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id and d.restaurant.id = :restaurantId")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.restaurant.id = :restaurantId")
    Dish get(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @QueryHints(value = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("SELECT d FROM Dish  d WHERE d.restaurant.id = :restaurantId AND d.date = :date ORDER BY d.name ASC")
    List<Dish> getByDate(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d " +
            "WHERE d.restaurant.id = :restaurantId AND d.date >= :startDate AND d.date <= :endDate " +
            "ORDER BY d.date DESC, d.name ASC ")
    List<Dish> getBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("restaurantId") int restaurantId);

}
