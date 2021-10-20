package ir.sbpro.springdb.plat_modules.platgames;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatinumGameRepository extends JpaRepository<PlatinumGame, String> {

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') ORDER BY g.owners DESC")
    Page<PlatinumGame> findBySOOwners(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.points DESC")
    Page<PlatinumGame> findBySOPoints(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.completionRate DESC")
    Page<PlatinumGame> findBySOCompletionRate(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.allTrophiesCount DESC")
    Page<PlatinumGame> findBySOTrophiesCount(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (CASE WHEN g.storeGame.discountedPriceValue >= 0 THEN g.storeGame.discountedPriceValue ELSE 1000000 END) ASC")
    Page<PlatinumGame> findBySOPrice(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.storeGame.discountTextValue DESC")
    Page<PlatinumGame> findBySODiscountPercent(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (g.storeGame.basePriceValue - g.storeGame.discountedPriceValue) DESC")
    Page<PlatinumGame> findBySODiscount(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (CASE WHEN g.hlGame.mainDurValue > 0 THEN g.hlGame.mainDurValue ELSE 1000000 END) ASC")
    Page<PlatinumGame> findBySOMainDuration(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM PlatinumGame g WHERE g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY (CASE WHEN g.hlGame.compDurValue > 0 THEN g.hlGame.compDurValue ELSE 1000000 END) ASC")
    Page<PlatinumGame> findBySOCompletionistDuration(Pageable pageable, PlatinumGame platGame);

    @Query("SELECT g FROM UserModel u INNER JOIN u.wishlist g WHERE u.pk = :#{#userPk} AND " +
            "g.name LIKE concat('%', :#{#platGame.name}, '%') " +
            "ORDER BY g.allTrophiesCount ASC")
    Page<PlatinumGame> findWishListByPrice(Pageable pageable, Long userPk, PlatinumGame platGame);
}
