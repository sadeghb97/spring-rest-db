package ir.sbpro.springdb.plat_modules.sale_dlc_items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SaleDLCService {
    public SaleDLCRepository saleDLCRepository;

    @Autowired
    SaleDLCService(SaleDLCRepository saleDLCRepository){
        this.saleDLCRepository = saleDLCRepository;
    }

    public Page<SaleDLCItem> findBySearchQuery(Pageable pageable, SaleDLCItem saleDLC, String sort) {
        if (saleDLC.getName() == null) saleDLC.setName("");
        if(sort == null || sort.equals("dis")) return saleDLCRepository.findBySODiscountPercent(pageable, saleDLC);
        if(sort.equals("fp")) return saleDLCRepository.findBySOFinalPrice(pageable, saleDLC);
        if(sort.equals("pp")) return saleDLCRepository.findBySOPlusPrice(pageable, saleDLC);
        if(sort.equals("plusdis")) return saleDLCRepository.findBySOPlusDiscountPercent(pageable, saleDLC);
        return saleDLCRepository.findBySODiscountPercent(pageable, saleDLC);
    }
}
