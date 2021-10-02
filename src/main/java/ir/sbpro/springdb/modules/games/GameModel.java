package ir.sbpro.springdb.modules.games;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.sbpro.springdb.modules.ModuleEntity;
import ir.sbpro.springdb.modules.studios.StudioModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class GameModel extends ModuleEntity {
    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String name;

    private String cover;

    private String description;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(referencedColumnName = "pk")
    private StudioModel studio;

    public GameModel(){
        super();
    }

    public GameModel(int year, String name) {
        super();
        this.year = year;
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudioModel getStudio() {
        return studio;
    }

    public void setStudio(StudioModel studio) {
        this.studio = studio;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStudioPk(){
        return studio != null ? studio.getPk() : null;
    }
}
