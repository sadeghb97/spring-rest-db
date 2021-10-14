package ir.sbpro.springdb.plat_modules.platgames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "platinum_games")
public class PlatinumGame {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private PSNGame storeGame;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private HLTBGame hlGame;

    private String link;
    private String image;
    private String name;
    private int owners = 0;
    private int recent = 0;
    private String store;
    private String platform;
    private int platCount = 0;
    private int goldCount = 0;
    private int silverCount = 0;
    private int bronzeCount = 0;
    private int allTrophiesCount = 0;
    private int points = 0;
    private double completionRate = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PSNGame getStoreGame() {
        return storeGame;
    }

    public void setStoreGame(PSNGame storeGame) {
        this.storeGame = storeGame;
    }

    public HLTBGame getHlGame() {
        return hlGame;
    }

    public void setHlGame(HLTBGame hlGame) {
        this.hlGame = hlGame;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwners() {
        return owners;
    }

    public void setOwners(int owners) {
        this.owners = owners;
    }

    public int getRecent() {
        return recent;
    }

    public void setRecent(int recent) {
        this.recent = recent;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getPlatCount() {
        return platCount;
    }

    public void setPlatCount(int platCount) {
        this.platCount = platCount;
    }

    public int getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(int goldCount) {
        this.goldCount = goldCount;
    }

    public int getSilverCount() {
        return silverCount;
    }

    public void setSilverCount(int silverCount) {
        this.silverCount = silverCount;
    }

    public int getBronzeCount() {
        return bronzeCount;
    }

    public void setBronzeCount(int bronzeCount) {
        this.bronzeCount = bronzeCount;
    }

    public int getAllTrophiesCount() {
        return allTrophiesCount;
    }

    public void setAllTrophiesCount(int allTrophiesCount) {
        this.allTrophiesCount = allTrophiesCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    @JsonIgnore
    public String getTrophiesSummary(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Trophies: ");
        stringBuilder.append(platCount);
        stringBuilder.append("-");
        stringBuilder.append(goldCount);
        stringBuilder.append("-");
        stringBuilder.append(silverCount);
        stringBuilder.append("-");
        stringBuilder.append(bronzeCount);
        stringBuilder.append("-");
        stringBuilder.append(allTrophiesCount);
        stringBuilder.append(" (");
        stringBuilder.append(points);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @JsonIgnore
    public String[] getSummary(){
        String lineDelimiter = "\n";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PSNP Link: " + link + lineDelimiter);
        stringBuilder.append("PSNP Name: " + name);
        if(store != null && !store.isEmpty()){
            stringBuilder.append(" (" + store + ")");
        }
        stringBuilder.append(lineDelimiter);

        stringBuilder.append(owners + " Owners | " + recent + " Recent" + lineDelimiter);
        stringBuilder.append("Trophies: " + platCount + " - " +
                goldCount + " - " + silverCount + " - " + bronzeCount + lineDelimiter);
        stringBuilder.append("Trophies Points: " + points + " (" + allTrophiesCount + ")" + lineDelimiter);
        stringBuilder.append("PSNP Platform: " + platform + lineDelimiter);
        stringBuilder.append("Completion Rate: " + completionRate + lineDelimiter);

        if(storeGame != null) {
            stringBuilder.append("-------------" + lineDelimiter);
            stringBuilder.append("Store ID: " + storeGame.getId() + lineDelimiter);
            stringBuilder.append("Store Name: " + storeGame.getName() + lineDelimiter);
            stringBuilder.append("Classification: " + storeGame.getStoreClassificationType() + " - " +
                    storeGame.getStoreClassificationDisplay() + lineDelimiter);
            stringBuilder.append("Price: " + storeGame.getBasePrice());
            if(storeGame.isDiscounted()){
                stringBuilder.append(" | " + storeGame.getDiscountedPrice() + " | " +
                        storeGame.getDiscountText());
            }
            stringBuilder.append(lineDelimiter);

            if(storeGame.getPlatforms().size() > 0){
                stringBuilder.append("Platforms: [");
                for(int i=0; storeGame.getPlatforms().size() > i; i++){
                    if(i != 0) stringBuilder.append(", ");
                    stringBuilder.append(storeGame.getPlatforms().get(i));
                }
                stringBuilder.append("]" + lineDelimiter);
            }

            if(storeGame.getAlsoIncluded().size() > 0){
                stringBuilder.append("Also Included: [");
                for(int i=0; storeGame.getAlsoIncluded().size() > i; i++){
                    if(i != 0) stringBuilder.append(", ");
                    stringBuilder.append(storeGame.getAlsoIncluded().get(i));
                }
                stringBuilder.append("]" + lineDelimiter);
            }
        }

        if(hlGame != null){
            stringBuilder.append("-------------" + lineDelimiter);
            stringBuilder.append("HLTB ID: " + hlGame.getId() + lineDelimiter);
            stringBuilder.append("HLTB Name: " + hlGame.getName() + lineDelimiter);
            stringBuilder.append("HLTB Link: " + hlGame.getFullLink() + lineDelimiter);
            stringBuilder.append("HLTB Image: " + hlGame.getFullImage() + lineDelimiter);
            stringBuilder.append("Main Duration: " + hlGame.getMainDuration() +
                    " (" + hlGame.getMainDurValue() + ")" + lineDelimiter);
            stringBuilder.append("Main And Extra Duration: " + hlGame.getMainAndExtraDuration() +
                    " (" + hlGame.getMainAndExtraDurValue() + ")" + lineDelimiter);
            stringBuilder.append("Completionist Duration: " + hlGame.getCompletionistDuration() +
                    " (" + hlGame.getCompDurValue() + ")" + lineDelimiter);
        }

        return stringBuilder.toString().split(lineDelimiter);
    }
}