package ir.sbpro.springdb.plat_modules.sales;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sbpro.models.GameSaleItem;
import ir.sbpro.springdb.plat_modules.active_sales.ActivePSNSale;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.utils.PriceUtils;

import javax.persistence.*;

@MappedSuperclass
public class SaleItem {
    @Id
    @GeneratedValue
    @Column(name = "pk", nullable = false)
    protected long pk;

    protected String PPID;
    protected String Name;
    protected String Img;
    protected String BasePrice;
    protected String SalePrice;
    protected String PlusPrice;
    protected String formattedBasePrice;
    protected String formattedSalePrice;
    protected String formattedPlusPrice;
    protected double basePriceValue;
    protected double salePriceValue;
    protected double plusPriceValue;
    protected double discountPercent;
    protected String PlatPricesURL;

    @ManyToOne
    @JoinColumn(referencedColumnName = "ID")
    protected ActivePSNSale sale;

    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public String getPPID() {
        return PPID;
    }

    public void setPPID(String PPID) {
        this.PPID = PPID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getBasePrice() {
        return BasePrice;
    }

    public void setBasePrice(String basePrice) {
        BasePrice = basePrice;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public String getPlusPrice() {
        return PlusPrice;
    }

    public void setPlusPrice(String plusPrice) {
        PlusPrice = plusPrice;
    }

    public String getFormattedBasePrice() {
        return formattedBasePrice;
    }

    public void setFormattedBasePrice(String formattedBasePrice) {
        this.formattedBasePrice = formattedBasePrice;
    }

    public String getFormattedSalePrice() {
        return formattedSalePrice;
    }

    public void setFormattedSalePrice(String formattedSalePrice) {
        this.formattedSalePrice = formattedSalePrice;
    }

    public String getFormattedPlusPrice() {
        return formattedPlusPrice;
    }

    public void setFormattedPlusPrice(String formattedPlusPrice) {
        this.formattedPlusPrice = formattedPlusPrice;
    }

    public double getBasePriceValue() {
        return basePriceValue;
    }

    public void setBasePriceValue(double basePriceValue) {
        this.basePriceValue = basePriceValue;
    }

    public double getSalePriceValue() {
        return salePriceValue;
    }

    public void setSalePriceValue(double salePriceValue) {
        this.salePriceValue = salePriceValue;
    }

    public double getPlusPriceValue() {
        return plusPriceValue;
    }

    public void setPlusPriceValue(double plusPriceValue) {
        this.plusPriceValue = plusPriceValue;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getPlatPricesURL() {
        return PlatPricesURL;
    }

    public void setPlatPricesURL(String platPricesURL) {
        PlatPricesURL = platPricesURL;
    }

    public ActivePSNSale getSale() {
        return sale;
    }

    public void setSale(ActivePSNSale sale) {
        this.sale = sale;
    }

    @JsonIgnore
    public String getPriceSummary(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Price: ");
        stringBuilder.append(formattedBasePrice);
        stringBuilder.append(" - ");
        stringBuilder.append(formattedSalePrice);

        if(plusPriceValue < salePriceValue){
            stringBuilder.append(" - ");
            stringBuilder.append(formattedPlusPrice);
        }

        return stringBuilder.toString();
    }

    @JsonIgnore
    public String getDiscountText(){
        int dp = (int) Math.round(discountPercent);
        return dp + "%";
    }

    public void load(SaleItem saleFetcher){
        setPPID(saleFetcher.PPID);
        setName(saleFetcher.Name);
        setImg(saleFetcher.Img);
        setBasePrice(saleFetcher.BasePrice);
        setSalePrice(saleFetcher.SalePrice);
        setPlusPrice(saleFetcher.PlusPrice);
        setFormattedBasePrice(saleFetcher.formattedBasePrice);
        setFormattedSalePrice(saleFetcher.formattedSalePrice);
        setFormattedPlusPrice(saleFetcher.formattedPlusPrice);

        setBasePriceValue(PriceUtils.getPriceValue(saleFetcher.formattedBasePrice));
        setSalePriceValue(PriceUtils.getPriceValue(saleFetcher.formattedSalePrice));
        setPlusPriceValue(PriceUtils.getPriceValue(saleFetcher.formattedPlusPrice));
        setDiscountPercent((basePriceValue - salePriceValue) / basePriceValue * 100);

        setPlatPricesURL(saleFetcher.PlatPricesURL);
    }
}
