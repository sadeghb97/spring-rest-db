package ir.sbpro.springdb.plat_modules.sale_game_items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleGameRepository extends JpaRepository<SaleGameItem, Long> {

    @Query("SELECT g FROM SaleGameItem g WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') " +
            "ORDER BY g.salePriceValue ASC")
    Page<SaleGameItem> findBySOFinalPrice(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g " +
            "WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') AND g.salePriceValue > g.plusPriceValue " +
            "ORDER BY g.plusPriceValue ASC")
    Page<SaleGameItem> findBySOPlusPrice(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') " +
            "ORDER BY g.discountPercent DESC")
    Page<SaleGameItem> findBySODiscountPercent(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g " +
            "WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') AND g.salePriceValue > g.plusPriceValue " +
            "ORDER BY g.plusDiscountPercent DESC")
    Page<SaleGameItem> findBySOPlusDiscountPercent(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') " +
            "ORDER BY g.discountedUntilUnix ASC")
    Page<SaleGameItem> findBySOTimeRemained(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') AND g.platinumGame IS NOT NULL " +
            "ORDER BY g.salePriceValue ASC")
    Page<SaleGameItem> findPlatsBySOFinalPrice(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g " +
            "WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') AND g.platinumGame IS NOT NULL AND g.salePriceValue > g.plusPriceValue " +
            "ORDER BY g.plusPriceValue ASC")
    Page<SaleGameItem> findPlatsBySOPlusPrice(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') AND g.platinumGame IS NOT NULL " +
            "ORDER BY g.discountPercent DESC")
    Page<SaleGameItem> findPlatsBySODiscountPercent(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g " +
            "WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') AND g.platinumGame IS NOT NULL " +
            "AND g.salePriceValue > g.plusPriceValue " +
            "ORDER BY g.plusDiscountPercent DESC")
    Page<SaleGameItem> findPlatsBySOPlusDiscountPercent(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g " +
            "WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') AND g.platinumGame IS NOT NULL " +
            "ORDER BY g.discountedUntilUnix ASC")
    Page<SaleGameItem> findPlatsBySOTimeRemained(Pageable pageable, SaleGameItem saleGame);

}
