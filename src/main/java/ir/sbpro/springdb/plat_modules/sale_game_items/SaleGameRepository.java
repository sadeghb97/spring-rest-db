package ir.sbpro.springdb.plat_modules.sale_game_items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleGameRepository extends JpaRepository<SaleGameItem, Long> {

    @Query("SELECT g FROM SaleGameItem g WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') " +
            "ORDER BY g.basePriceValue ASC")
    Page<SaleGameItem> findBySOFinalPrice(Pageable pageable, SaleGameItem saleGame);

    @Query("SELECT g FROM SaleGameItem g WHERE g.Name LIKE concat('%', :#{#saleGame.Name}, '%') " +
            "ORDER BY g.discountPercent DESC")
    Page<SaleGameItem> findBySODiscountPercent(Pageable pageable, SaleGameItem saleGame);

}
