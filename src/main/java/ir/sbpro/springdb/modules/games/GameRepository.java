package ir.sbpro.springdb.modules.games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameModel, Long> {

    /*@Query("select g from games g where g.pk=:gameid")
    GameModel findByQuery(@Param("gameid") String gameId);*/

}
