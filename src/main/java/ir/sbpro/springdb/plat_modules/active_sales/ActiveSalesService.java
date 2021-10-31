package ir.sbpro.springdb.plat_modules.active_sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveSalesService {
    public ActiveSalesRepository activeSalesRepository;

    @Autowired
    ActiveSalesService(ActiveSalesRepository activeSalesRepository){
        this.activeSalesRepository = activeSalesRepository;
    }
}
