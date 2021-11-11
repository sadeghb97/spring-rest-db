package ir.sbpro.springdb.plat_modules.sale_game_items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sbpro.models.GameSaleItem;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.sales.SaleItem;
import ir.sbpro.springdb.utils.PriceUtils;
import ir.sbpro.springdb.utils.TimeUtils;

import javax.persistence.*;

@Entity
@Table(name = "sale_games")
public class SaleGameItem extends SaleItem {
    private String Difficulty;
    private String HoursLow;
    private String HoursHigh;
    private boolean IsPS4;
    private boolean IsPS5;
    private String LastDiscounted;
    private String DiscountedUntil;
    private long discountedUntilUnix;
    private String PlatPricesURL;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private PlatinumGame platinumGame = null;

    public String getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(String difficulty) {
        Difficulty = difficulty;
    }

    public String getHoursLow() {
        return HoursLow;
    }

    public void setHoursLow(String hoursLow) {
        HoursLow = hoursLow;
    }

    public String getHoursHigh() {
        return HoursHigh;
    }

    public void setHoursHigh(String hoursHigh) {
        HoursHigh = hoursHigh;
    }

    public String getLastDiscounted() {
        return LastDiscounted;
    }

    public void setLastDiscounted(String lastDiscounted) {
        LastDiscounted = lastDiscounted;
    }

    public String getDiscountedUntil() {
        return DiscountedUntil;
    }

    public void setDiscountedUntil(String discountedUntil) {
        DiscountedUntil = discountedUntil;
    }

    public void setDiscountedUntilUnix(long discountedUntilUnix) {
        this.discountedUntilUnix = discountedUntilUnix;
    }

    public long getDiscountedUntilUnix(){
        return TimeUtils.ppTimeStrToUnix(getDiscountedUntil());
    }

    public String getPlatPricesURL() {
        return PlatPricesURL;
    }

    public void setPlatPricesURL(String platPricesURL) {
        PlatPricesURL = platPricesURL;
    }

    public boolean isPS4() {
        return IsPS4;
    }

    public void setPS4(boolean PS4) {
        IsPS4 = PS4;
    }

    public boolean isPS5() {
        return IsPS5;
    }

    public void setPS5(boolean PS5) {
        IsPS5 = PS5;
    }

    public PlatinumGame getPlatinumGame() {
        return platinumGame;
    }

    public void setPlatinumGame(PlatinumGame platinumGame) {
        this.platinumGame = platinumGame;
    }

    @JsonIgnore
    public String getDiscountedUntilSummary(){
        long duration = getDiscountedUntilUnix() - System.currentTimeMillis();
        if(duration <= 0) return "Expired";
        return TimeUtils.getDuration(duration, false);
    }

    public void load(GameSaleItem saleGameFetcher){
        setPPID(saleGameFetcher.PPID);
        setName(saleGameFetcher.Name);
        setImg(saleGameFetcher.Img);
        setDifficulty(saleGameFetcher.Difficulty);
        setHoursLow(saleGameFetcher.HoursLow);
        setHoursHigh(saleGameFetcher.HoursHigh);

        setPS4(saleGameFetcher.IsPS4 != null &&
                !saleGameFetcher.IsPS4.isEmpty() && Integer.parseInt(saleGameFetcher.IsPS4) > 0);

        setPS5(saleGameFetcher.IsPS5 != null &&
                !saleGameFetcher.IsPS5.isEmpty() && Integer.parseInt(saleGameFetcher.IsPS5) > 0);

        setLastDiscounted(saleGameFetcher.LastDiscounted);
        setDiscountedUntil(saleGameFetcher.DiscountedUntil);
        setDiscountedUntilUnix(TimeUtils.ppTimeStrToUnix(saleGameFetcher.DiscountedUntil));
        setBasePrice(saleGameFetcher.BasePrice);
        setSalePrice(saleGameFetcher.SalePrice);
        setPlusPrice(saleGameFetcher.PlusPrice);
        setFormattedBasePrice(saleGameFetcher.formattedBasePrice);
        setFormattedSalePrice(saleGameFetcher.formattedSalePrice);
        setFormattedPlusPrice(saleGameFetcher.formattedPlusPrice);

        setBasePriceValue(PriceUtils.getPriceValue(saleGameFetcher.formattedBasePrice));
        setSalePriceValue(PriceUtils.getPriceValue(saleGameFetcher.formattedSalePrice));
        setPlusPriceValue(PriceUtils.getPriceValue(saleGameFetcher.formattedPlusPrice));
        setDiscountPercent((basePriceValue - salePriceValue) / basePriceValue * 100);
        setPlusDiscountPercent((basePriceValue - plusPriceValue) / basePriceValue * 100);

        setPlatPricesURL(saleGameFetcher.PlatPricesURL);
    }
}
