package ir.sbpro.springdb.plat_modules.usergames;

import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    public static String USER_GAME_QUERY = "SELECT * FROM users u INNER JOIN user_game g ON u.pk = g.user_pk " +
            "LEFT JOIN platinum_games pg ON g.platinum_game_id = pg.id " +
            "LEFT JOIN games ig ON g.indie_game_pk = ig.pk " +
            "WHERE g.user_pk = :#{#userPk} AND " +
            "(pg.name LIKE concat('%', :#{#platGame.name}, '%') OR ig.name LIKE concat('%', :#{#platGame.name}, '%')) " +
            "ORDER BY g.";

    @Query(value = USER_GAME_QUERY + "rate DESC", nativeQuery = true)
    Page<UserGame> findLibraryGamesByRate(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query(value = USER_GAME_QUERY + "status DESC", nativeQuery = true)
    Page<UserGame> findLibraryGamesByStatus(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query(value = USER_GAME_QUERY + "created_at DESC", nativeQuery = true)
    Page<UserGame> findBySOCreationTime(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query(value = USER_GAME_QUERY + "playtime DESC", nativeQuery = true)
    Page<UserGame> findBySOPlaytime(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query(value = USER_GAME_QUERY + "progress DESC", nativeQuery = true)
    Page<UserGame> findBySOProgress(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.platinumGame.owners DESC")
    Page<UserGame> findBySOOwners(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.platinumGame.points DESC")
    Page<UserGame> findBySOPoints(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.platinumGame.completionRate DESC")
    Page<UserGame> findBySOCompletionRate(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.platinumGame.platAchievers DESC")
    Page<UserGame> findBySOPlatAchievers(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.platinumGame.allTrophiesCount DESC")
    Page<UserGame> findBySOTrophiesCount(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.platinumGame.storeGame.basePriceValue DESC")
    Page<UserGame> findBySOPrice(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (CASE WHEN g.platinumGame.hlGame.mainDurValue > 0 THEN g.platinumGame.hlGame.mainDurValue ELSE 1000000 END) ASC")
    Page<UserGame> findBySOMainDuration(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (CASE WHEN g.platinumGame.hlGame.compDurValue > 0 THEN g.platinumGame.hlGame.compDurValue ELSE 1000000 END) ASC")
    Page<UserGame> findBySOCompletionistDuration(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (CASE WHEN g.platinumGame.metacriticGame.metaScore > 0 THEN g.platinumGame.metacriticGame.metaScore ELSE 0 END) DESC")
    Page<UserGame> findBySOMetaScore(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (CASE WHEN g.platinumGame.metacriticGame.userScore > 0 THEN g.platinumGame.metacriticGame.userScore ELSE 0 END) DESC")
    Page<UserGame> findBySOUserScore(Pageable pageable, Long userPk, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} " +
            "ORDER BY g.rank ASC")
    ArrayList<UserGame> findUserRankedGames(Long userPk);
}

