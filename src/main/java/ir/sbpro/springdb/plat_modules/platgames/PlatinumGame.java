package ir.sbpro.springdb.plat_modules.platgames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sbpro.models.MetacriticGame;
import ir.sbpro.models.PSNProfilesGame;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private MetaCriticGame metacriticGame;

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
    private double platAchievers = 0;
    private long upTime = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

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

    public double getPlatAchievers() {
        return platAchievers;
    }

    public void setPlatAchievers(double platAchievers) {
        this.platAchievers = platAchievers;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public MetaCriticGame getMetacriticGame() {
        return metacriticGame;
    }

    public void setMetaCriticGame(MetaCriticGame metacriticGame) {
        this.metacriticGame = metacriticGame;
    }

    public void load(PSNProfilesGame psnProfilesGame){
        setId(psnProfilesGame.id);
        setLink(psnProfilesGame.link);
        setImage(psnProfilesGame.image);
        setName(psnProfilesGame.name);
        setOwners(psnProfilesGame.owners);
        setRecent(psnProfilesGame.recent);
        setStore(psnProfilesGame.store);
        setPlatform(psnProfilesGame.platform);
        setPlatCount(psnProfilesGame.platCount);
        setGoldCount(psnProfilesGame.goldCount);
        setSilverCount(psnProfilesGame.silverCount);
        setBronzeCount(psnProfilesGame.bronzeCount);
        setAllTrophiesCount(psnProfilesGame.allTrophiesCount);
        setPoints(psnProfilesGame.points);
        setCompletionRate(psnProfilesGame.completionRate);
        setPlatAchievers(psnProfilesGame.platAchievers);
        setUpTime(psnProfilesGame.upTime);
    }

    public void load(PSNProfilesGame psnProfilesGame, HLTBGame hltbGame, PSNGame psnGame, MetaCriticGame mcGame){
        load(psnProfilesGame);
        setStoreGame(psnGame);
        setHlGame(hltbGame);
        setMetaCriticGame(mcGame);
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

    public String getCompletionSummary(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CR: " + completionRate);
        if(platCount > 0){
            stringBuilder.append(" - ");
            stringBuilder.append(platAchievers);
        }

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

            if(storeGame.getStoreClassificationType() != null && !storeGame.getStoreClassificationType().isEmpty()) {
                stringBuilder.append("Classification: " + storeGame.getStoreClassificationType() + " - " +
                        storeGame.getStoreClassificationDisplay() + lineDelimiter);
            }

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

        if(metacriticGame != null){
            stringBuilder.append("-------------" + lineDelimiter);
            stringBuilder.append("MetaCritic Slug: " + metacriticGame.getSlug() + lineDelimiter);
            stringBuilder.append("MetaCritic Name: " + metacriticGame.getName() + lineDelimiter);
            stringBuilder.append("MetaCritic Image: " + metacriticGame.getImage() + lineDelimiter);
            stringBuilder.append("MetaScore: " + metacriticGame.getMetaScore() +
                    " (" + metacriticGame.getMsCount() + ")" + lineDelimiter);
            stringBuilder.append("UserScore: " + metacriticGame.getUserScore() +
                    " (" + metacriticGame.getUsCount() + ")" + lineDelimiter);
        }

        return stringBuilder.toString().split(lineDelimiter);
    }

    @JsonIgnore
    public String[] getShortSummary(){
        String lineDelimiter = "\n";
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(getName());
        stringBuilder.append(lineDelimiter);

        stringBuilder.append(getOwners() + " Owners | " + getRecent() + " Recent");
        stringBuilder.append(lineDelimiter);

        stringBuilder.append(getTrophiesSummary());
        stringBuilder.append(lineDelimiter);

        stringBuilder.append(getCompletionSummary());
        stringBuilder.append(lineDelimiter);

        if(getStoreGame() != null){
            stringBuilder.append(getStoreGame().getPriceSummary());
            stringBuilder.append(lineDelimiter);
        }

        if(getHlGame() != null){
            stringBuilder.append(getHlGame().getDurationsSummary());
            stringBuilder.append(lineDelimiter);
        }

        if(getMetacriticGame() != null){
            stringBuilder.append(getMetacriticGame().getMetaSummary());
            stringBuilder.append(lineDelimiter);
        }

        return stringBuilder.toString().split(lineDelimiter);
    }
}
