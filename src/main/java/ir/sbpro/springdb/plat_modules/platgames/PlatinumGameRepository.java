package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatinumGameRepository extends JpaRepository<PlatinumGame, String> {

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY g.owners DESC")
    Page<PlatinumGame> findBySOOwners(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY g.points DESC")
    Page<PlatinumGame> findBySOPoints(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
                "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY g.createdAt DESC")
    Page<PlatinumGame> findBySOCreationTime(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY g.completionRate DESC")
    Page<PlatinumGame> findBySOCompletionRate(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY g.platAchievers DESC")
    Page<PlatinumGame> findBySOPlatAchievers(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY g.allTrophiesCount DESC")
    Page<PlatinumGame> findBySOTrophiesCount(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY (CASE WHEN g.storeGame.discountedPriceValue >= 0 THEN g.storeGame.discountedPriceValue ELSE 1000000 END) ASC")
    Page<PlatinumGame> findBySOPrice(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "AND g.storeGame IS NOT NULL AND g.gbStoreGame IS NOT NULL AND g.storeGame.saleToman > g.gbStoreGame.saleToman " +
            "ORDER BY (CASE WHEN g.storeGame.discountedPriceValue >= 0 THEN g.storeGame.discountedPriceValue ELSE 1000000 END) ASC")
    Page<PlatinumGame> findBySOGBPrice(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY g.storeGame.discountTextValue DESC")
    Page<PlatinumGame> findBySODiscountPercent(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY (g.storeGame.basePriceValue - g.storeGame.discountedPriceValue) DESC")
    Page<PlatinumGame> findBySODiscount(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY (CASE WHEN g.hlGame.mainDurValue > 0 THEN g.hlGame.mainDurValue ELSE 1000000 END) ASC")
    Page<PlatinumGame> findBySOMainDuration(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY (CASE WHEN g.hlGame.compDurValue > 0 THEN g.hlGame.compDurValue ELSE 1000000 END) ASC")
    Page<PlatinumGame> findBySOCompletionistDuration(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY (CASE WHEN g.metacriticGame.metaScore > 0 THEN g.metacriticGame.metaScore ELSE 0 END) DESC")
    Page<PlatinumGame> findBySOMetaScore(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "AND (:#{#platGamesFilter.libIncluded ? 1 : 0} = 1 OR NOT EXISTS (" +
            "SELECT ug FROM UserModel u INNER JOIN u.psnGames ug WHERE u.pk = :#{#platGamesFilter.userPk} AND ug.platinumGame.id = g.id)) " +
            "ORDER BY (CASE WHEN g.metacriticGame.userScore > 0 THEN g.metacriticGame.userScore ELSE 0 END) DESC")
    Page<PlatinumGame> findBySOUserScore(Pageable pageable, PlatinumGame platGame, PlatGamesFilter platGamesFilter);

    @Query("SELECT g FROM UserModel u INNER JOIN u.wishlist g WHERE u.pk = :#{#userPk} AND " +
            "g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.allTrophiesCount ASC")
    Page<PlatinumGame> findWishListByPrice(Pageable pageable, Long userPk, PlatinumGame platGame);


    @Query("SELECT pg FROM PlatinumGame pg INNER JOIN pg.storeGame g WHERE g.ppid = :#{#ppid}")
    List<PlatinumGame> filterByPPID(String ppid);
}
