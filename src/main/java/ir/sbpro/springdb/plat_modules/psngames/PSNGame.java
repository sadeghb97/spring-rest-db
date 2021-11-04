package ir.sbpro.springdb.plat_modules.psngames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sbpro.models.GameSaleItem;
import ir.sbpro.springdb.utils.PriceUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "psngames")
public class PSNGame {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String ppid;
    private String name;
    private String storeClassificationType = "";
    private String storeClassificationDisplay = "";

    private String basePrice = "";
    private String discountedPrice = "";
    private String discountText = "";
    private double basePriceValue = 0;
    private double discountedPriceValue = 0;
    private double discountTextValue = 0;
    private boolean discounted = false;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "psngames_also_included")
    private List<String> alsoIncluded;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "psngames_platforms")
    private List<String> platforms;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "psngames_images")
    private List<String> images;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "psngames_videos")
    private List<String> videos;

    private long upTime = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreClassificationType() {
        return storeClassificationType;
    }

    public void setStoreClassificationType(String storeClassificationType) {
        this.storeClassificationType = storeClassificationType;
    }

    public String getStoreClassificationDisplay() {
        return storeClassificationDisplay;
    }

    public void setStoreClassificationDisplay(String storeClassificationDisplay) {
        this.storeClassificationDisplay = storeClassificationDisplay;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getDiscountText() {
        return discountText;
    }

    public void setDiscountText(String discountText) {
        this.discountText = discountText;
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public List<String> getAlsoIncluded() {
        return alsoIncluded;
    }

    public void setAlsoIncluded(List<String> alsoIncluded) {
        this.alsoIncluded = alsoIncluded;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public String getPpid() {
        return ppid;
    }

    public void setPpid(String ppid) {
        this.ppid = ppid;
    }

    public double getBasePriceValue() {
        return basePriceValue;
    }

    public void setBasePriceValue(double basePriceValue) {
        this.basePriceValue = basePriceValue;
    }

    public double getDiscountedPriceValue() {
        return discountedPriceValue;
    }

    public void setDiscountedPriceValue(double discountedPriceValue) {
        this.discountedPriceValue = discountedPriceValue;
    }

    public double getDiscountTextValue() {
        return discountTextValue;
    }

    public void setDiscountTextValue(double discountTextValue) {
        this.discountTextValue = discountTextValue;
    }

    public void load(ir.sbpro.models.PSNGame psnGame){
        setId(psnGame.id);
        setPpid(psnGame.ppid);
        setName(psnGame.name);

        if(psnGame.storeClassificationType != null && !psnGame.storeClassificationType.isEmpty())
            setStoreClassificationType(psnGame.storeClassificationType);

        if(psnGame.storeClassificationDisplay != null && !psnGame.storeClassificationDisplay.isEmpty())
            setStoreClassificationDisplay(psnGame.storeClassificationDisplay);

        setPlatforms(psnGame.platforms);
        setImages(psnGame.images);
        setVideos(psnGame.videos);
        setBasePrice(psnGame.gamePrice.basePrice);
        setDiscountedPrice(psnGame.gamePrice.discountedPrice);
        setDiscountText(psnGame.gamePrice.discountText);
        setBasePriceValue(PriceUtils.getPriceValue(psnGame.gamePrice.basePrice));
        setDiscountedPriceValue(PriceUtils.getPriceValue(psnGame.gamePrice.discountedPrice));
        setDiscountTextValue(
                getDiscountPercentValue(psnGame.gamePrice.discountText));
        setDiscounted(psnGame.gamePrice.discounted);
        setAlsoIncluded(psnGame.gamePrice.alsoIncluded);
        setUpTime(psnGame.upTime);
    }

    public void load(GameSaleItem saleGameFetcher){
        setPpid(saleGameFetcher.PPID);
        setBasePrice(saleGameFetcher.formattedBasePrice);
        setDiscountedPrice(saleGameFetcher.formattedSalePrice);
        setBasePriceValue(PriceUtils.getPriceValue(saleGameFetcher.formattedBasePrice));
        setDiscountedPriceValue(PriceUtils.getPriceValue(saleGameFetcher.formattedSalePrice));

        setDiscountTextValue((basePriceValue - discountedPriceValue) / basePriceValue * 100);
        setDiscountText("-" + discountTextValue + "%");
        setUpTime(System.currentTimeMillis());
    }

    private double getDiscountPercentValue(String discountText){
        discountText = discountText.trim();
        try {
            return Double.parseDouble(discountText.substring(1, discountText.length() - 1));
        }
        catch (Exception ex){
            return 0;
        }
    }

    @JsonIgnore
    public String getPriceSummary(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Price: ");
        stringBuilder.append(basePrice);

        if(discounted){
            stringBuilder.append(" | ");
            stringBuilder.append(discountedPrice);
            stringBuilder.append(" | ");
            stringBuilder.append(discountText);
        }

        return stringBuilder.toString();
    }
}
