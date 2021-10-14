package ir.sbpro.springdb.plat_modules.hltbgames;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "hltbgames")
public class HLTBGame {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String name;
    private String link;
    private String image;

    private String mainDuration = "";
    private String mainAndExtraDuration = "";
    private String completionistDuration = "";
    private int mainDurValue;
    private int mainAndExtraDurValue;
    private int compDurValue;

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

    public String getMainDuration() {
        return mainDuration;
    }

    public void setMainDuration(String mainDuration) {
        this.mainDuration = mainDuration;
    }

    public String getMainAndExtraDuration() {
        return mainAndExtraDuration;
    }

    public void setMainAndExtraDuration(String mainAndExtraDuration) {
        this.mainAndExtraDuration = mainAndExtraDuration;
    }

    public String getCompletionistDuration() {
        return completionistDuration;
    }

    public void setCompletionistDuration(String completionistDuration) {
        this.completionistDuration = completionistDuration;
    }

    public int getMainDurValue() {
        return mainDurValue;
    }

    public void setMainDurValue(int mainDurValue) {
        this.mainDurValue = mainDurValue;
    }

    public int getMainAndExtraDurValue() {
        return mainAndExtraDurValue;
    }

    public void setMainAndExtraDurValue(int mainAndExtraDurValue) {
        this.mainAndExtraDurValue = mainAndExtraDurValue;
    }

    public int getCompDurValue() {
        return compDurValue;
    }

    public void setCompDurValue(int compDurValue) {
        this.compDurValue = compDurValue;
    }

    @JsonIgnore
    private String makeUrl(String purl){
        String hlStartUrl = "https://howlongtobeat.com";
        StringBuilder stringBuilder = new StringBuilder(hlStartUrl);
        if(!purl.startsWith("/")) stringBuilder.append("/");
        stringBuilder.append(purl);
        return stringBuilder.toString();
    }

    @JsonIgnore
    public String getFullLink(){
        return makeUrl(link);
    }

    @JsonIgnore
    public String getFullImage(){
        return makeUrl(image);
    }

    @JsonIgnore
    public String getDurationsSummary(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mainDuration);
        stringBuilder.append(" | ");
        stringBuilder.append(mainAndExtraDuration);
        stringBuilder.append(" | ");
        stringBuilder.append(completionistDuration);
        return stringBuilder.toString();
    }
}
