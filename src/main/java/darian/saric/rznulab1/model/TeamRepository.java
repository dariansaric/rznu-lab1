package darian.saric.rznulab1.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("from team where name = ?1")
    Team findByTeamName(String name);
}
