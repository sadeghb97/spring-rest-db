package ir.sbpro.springdb.plat_modules.psngames;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "psngames")
public class PSNGame {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

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
