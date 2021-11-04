package ir.sbpro.springdb.plat_modules.sale_dlc_items;

import ir.sbpro.models.DLCSaleItem;
import ir.sbpro.springdb.plat_modules.sales.SaleItem;
import ir.sbpro.springdb.utils.PriceUtils;

import javax.persistence.*;

@Entity
@Table(name = "sale_dlc")
public class SaleDLCItem extends SaleItem {
    private String ParentGame;

    public String getParentGame() {
        return ParentGame;
    }

    public void setParentGame(String parentGame) {
        ParentGame = parentGame;
    }

    public void load(DLCSaleItem saleDLCFetcher){
        setPPID(saleDLCFetcher.PPID);
        setName(saleDLCFetcher.Name);
        setImg(saleDLCFetcher.Img);
        setBasePrice(saleDLCFetcher.BasePrice);
        setSalePrice(saleDLCFetcher.SalePrice);
        setPlusPrice(saleDLCFetcher.PlusPrice);
        setFormattedBasePrice(saleDLCFetcher.formattedBasePrice);
        setFormattedSalePrice(saleDLCFetcher.formattedSalePrice);
        setFormattedPlusPrice(saleDLCFetcher.formattedPlusPrice);
        setBasePriceValue(PriceUtils.getPriceValue(saleDLCFetcher.formattedBasePrice));
        setSalePriceValue(PriceUtils.getPriceValue(saleDLCFetcher.formattedSalePrice));
        setPlusPriceValue(PriceUtils.getPriceValue(saleDLCFetcher.formattedPlusPrice));
        setPlatPricesURL(saleDLCFetcher.PlatPricesURL);
        setDiscountPercent((basePriceValue - salePriceValue) / basePriceValue * 100);
        setPlusDiscountPercent((basePriceValue - plusPriceValue) / basePriceValue * 100);

        setParentGame(saleDLCFetcher.ParentGame);
    }
}
