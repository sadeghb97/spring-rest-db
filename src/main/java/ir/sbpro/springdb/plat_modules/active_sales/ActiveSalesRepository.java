package ir.sbpro.springdb.plat_modules.active_sales;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveSalesRepository extends JpaRepository<ActivePSNSale, String> {
}
