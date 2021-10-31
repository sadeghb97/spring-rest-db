package ir.sbpro.springdb.plat_modules.sale_game_items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SaleGameService {
    public SaleGameRepository saleGameRepository;

    @Autowired
    SaleGameService(SaleGameRepository saleGameRepository){
        this.saleGameRepository = saleGameRepository;
    }

    public Page<SaleGameItem> findBySearchQuery(Pageable pageable, SaleGameItem saleGame, String sort) {
        if (saleGame.getName() == null) saleGame.setName("");
        if(sort == null || sort.equals("dis")) return saleGameRepository.findBySODiscountPercent(pageable, saleGame);
        if(sort.equals("fp")) return saleGameRepository.findBySOFinalPrice(pageable, saleGame);
        if(sort.equals("pgfp")) return saleGameRepository.findPlatsBySOFinalPrice(pageable, saleGame);
        if(sort.equals("pgdis")) return saleGameRepository.findPlatsBySODiscountPercent(pageable, saleGame);
        return saleGameRepository.findBySODiscountPercent(pageable, saleGame);
    }
}
