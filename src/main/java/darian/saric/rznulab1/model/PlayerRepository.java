package darian.saric.rznulab1.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    //    @Query("from player where t_id = ?1")
//    List<Player> getAllByTeam(Long teamId);
    @Query("from player where team = ?1")
    List<Player> getAllByTeam(Team team);
}
