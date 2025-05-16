package ir.sbpro.springdb.plat_modules.usergames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sbpro.springdb.enums.UserGamePlatform;
import ir.sbpro.springdb.enums.PurchaseType;
import ir.sbpro.springdb.enums.UserGameStatus;
import ir.sbpro.springdb.modules.games.GameModel;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class UserGame {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private double rate;

    private int rank;

    private int progress;

    private int playtime;

    private UserGamePlatform platform;

    private UserGameStatus status;

    private PurchaseType purchaseType;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private PlatinumGame platinumGame;

    @ManyToOne
    @JoinColumn(referencedColumnName = "pk")
    private GameModel indieGame;

    @ManyToOne
    @JoinColumn(referencedColumnName = "pk")
    private UserModel user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    public UserGamePlatform getPlatform() {
        return platform;
    }

    public void setPlatform(UserGamePlatform platform) {
        this.platform = platform;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(PurchaseType purchaseType) {
        this.purchaseType = purchaseType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public PlatinumGame getPlatinumGame() {
        return platinumGame;
    }

    public void setPlatinumGame(PlatinumGame platinumGame) {
        this.platinumGame = platinumGame;
    }

    public GameModel getIndieGame() {
        return indieGame;
    }

    public void setIndieGame(GameModel indieGame) {
        this.indieGame = indieGame;
    }

    public UserGameStatus getStatus() {
        return status;
    }

    public void setStatus(UserGameStatus status) {
        this.status = status;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    @JsonIgnore
    public String getName(){
        if(platinumGame != null) return platinumGame.getName();
        if(indieGame != null) return indieGame.getName();
        return "";
    }

    @JsonIgnore
    public String getImage(){
        if(platinumGame != null) return platinumGame.getImage();
        if(indieGame != null) return "img/covers/gamemodel/" + indieGame.getPk();
        return "";
    }

    @JsonIgnore
    public String[] getShortSummary(){
        if(platinumGame != null) return getPlatinumShortSummary();

        if(indieGame != null) {
            String lineDelimiter = "\n";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(indieGame.getName());
            return stringBuilder.toString().split(lineDelimiter);
        }

        return new String[0];
    }

    @JsonIgnore
    public String[] getPlatinumShortSummary(){
        String[] pgTexts = platinumGame.getShortSummary();

        String lineDelimiter = "\n";
        StringBuilder stringBuilder = new StringBuilder("-----");
        stringBuilder.append(lineDelimiter);

        if(rate > 0) {
            stringBuilder.append("Rate: " + rate);
            stringBuilder.append(lineDelimiter);
        }

        stringBuilder.append(status);
        stringBuilder.append(lineDelimiter);

        if(purchaseType != null){
            stringBuilder.append(purchaseType);
            stringBuilder.append(lineDelimiter);
        }

        if(platform != null){
            stringBuilder.append(platform);
            stringBuilder.append(lineDelimiter);
        }

        String[] ugTexts = stringBuilder.toString().split(lineDelimiter);
        List<String> resultList = new ArrayList<>(pgTexts.length + ugTexts.length);
        Collections.addAll(resultList, pgTexts);
        Collections.addAll(resultList, ugTexts);
        String[] result = new String[resultList.size()];
        result = resultList.toArray(result);
        return result;
    }
}
