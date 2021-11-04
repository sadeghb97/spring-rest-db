package ir.sbpro.springdb.plat_modules.sale_dlc_items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleDLCRepository extends JpaRepository<SaleDLCItem, Long> {

    @Query("SELECT g FROM SaleDLCItem g WHERE g.Name LIKE concat('%', :#{#saleDLC.Name}, '%') " +
            "ORDER BY g.salePriceValue ASC")
    Page<SaleDLCItem> findBySOFinalPrice(Pageable pageable, SaleDLCItem saleDLC);

    @Query("SELECT g FROM SaleDLCItem g WHERE g.Name LIKE concat('%', :#{#saleDLC.Name}, '%') " +
            "AND g.salePriceValue > g.plusPriceValue " +
            "ORDER BY g.plusPriceValue ASC")
    Page<SaleDLCItem> findBySOPlusPrice(Pageable pageable, SaleDLCItem saleDLC);

    @Query("SELECT g FROM SaleDLCItem g WHERE g.Name LIKE concat('%', :#{#saleDLC.Name}, '%') " +
            "ORDER BY g.discountPercent DESC")
    Page<SaleDLCItem> findBySODiscountPercent(Pageable pageable, SaleDLCItem saleDLC);

    @Query("SELECT g FROM SaleDLCItem g WHERE g.Name LIKE concat('%', :#{#saleDLC.Name}, '%') " +
            "AND g.salePriceValue > g.plusPriceValue " +
            "ORDER BY g.plusDiscountPercent DESC")
    Page<SaleDLCItem> findBySOPlusDiscountPercent(Pageable pageable, SaleDLCItem saleDLC);

}
