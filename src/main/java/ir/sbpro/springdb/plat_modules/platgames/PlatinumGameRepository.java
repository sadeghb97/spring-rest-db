package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.springdb.modules.games.GameModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatinumGameRepository extends JpaRepository<PlatinumGame, String> {

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') ORDER BY g.owners DESC")
    Page<PlatinumGame> findBySearchQuery(Pageable pageable, PlatinumGame platGame);

}
