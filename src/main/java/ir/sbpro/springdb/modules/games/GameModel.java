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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(referencedColumnName = "pk")
    private StudioModel studio;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public Long getStudioPk(){
        return studio != null ? studio.getPk() : null;
    }
}
