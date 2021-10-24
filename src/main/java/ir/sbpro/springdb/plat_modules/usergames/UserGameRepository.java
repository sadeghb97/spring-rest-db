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

    @Query("SELECT g FROM UserModel u INNER JOIN u.psnGames g WHERE u.pk = :#{#userPk} AND " +
            "g.platinumGame.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.status DESC")
    Page<UserGame> findLibraryGamesByStatus(Pageable pageable, Long userPk, PlatinumGame platGame);

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
            "ORDER BY g.platinumGame.createdAt DESC")
    Page<UserGame> findBySOCreationTime(Pageable pageable, Long userPk, PlatinumGame platGame);

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
}

