package ir.sbpro.springdb.plat_modules.metacritic_games;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "metacritic_games")
public class MetaCriticGame {
    public static final String START_URL = "https://www.metacritic.com/game/";

    @Id
    @Column(name = "id", nullable = false)
    private String slug;

    private String name;
    private String image;
    private boolean msIsTBD = false;
    private int metaScore;
    private boolean usIsTBD = false;
    private int msCount;
    private double userScore;
    private int usCount;
    private long upTime = 0;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isMsIsTBD() {
        return msIsTBD;
    }

    public void setMsIsTBD(boolean msIsTBD) {
        this.msIsTBD = msIsTBD;
    }

    public int getMetaScore() {
        return metaScore;
    }

    public void setMetaScore(int metaScore) {
        this.metaScore = metaScore;
    }

    public boolean isUsIsTBD() {
        return usIsTBD;
    }

    public void setUsIsTBD(boolean usIsTBD) {
        this.usIsTBD = usIsTBD;
    }

    public int getMsCount() {
        return msCount;
    }

    public void setMsCount(int msCount) {
        this.msCount = msCount;
    }

    public double getUserScore() {
        return userScore;
    }

    public void setUserScore(double userScore) {
        this.userScore = userScore;
    }

    public int getUsCount() {
        return usCount;
    }

    public void setUsCount(int usCount) {
        this.usCount = usCount;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public void load(ir.sbpro.models.MetacriticGame mcFetcher){
        setSlug(mcFetcher.slug);
        setName(mcFetcher.name);
        setImage(mcFetcher.image);
        setMsIsTBD(mcFetcher.msIsTBD);
        setMetaScore(mcFetcher.metaScore);
        setMsCount(mcFetcher.msCount);
        setUsIsTBD(mcFetcher.usIsTBD);
        setUserScore(mcFetcher.userScore);
        setUsCount(mcFetcher.usCount);
        setUpTime(mcFetcher.upTime);
    }

    @JsonIgnore
    public String getLink(){
        return START_URL + slug;
    }

    @JsonIgnore
    public String getMetaSummary(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Metacritic Rate: ");
        if(msIsTBD) stringBuilder.append("TBD");
        else stringBuilder.append(metaScore);
        stringBuilder.append(" - ");

        if(usIsTBD) System.out.print("TBD");
        else stringBuilder.append(userScore);
        return stringBuilder.toString();
    }
}
