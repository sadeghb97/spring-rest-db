package ir.sbpro.springdb.plat_modules.usergames;

import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.rate ASC")
    Page<UserGame> findLibraryGamesByRate(Pageable pageable, Long userPk, PlatinumGame platGame);

}

