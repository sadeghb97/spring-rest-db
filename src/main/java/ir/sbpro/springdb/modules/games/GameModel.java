package ir.sbpro.springdb.modules.games;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.sbpro.springdb._module_interfaces.HasCover;
import ir.sbpro.springdb._module_interfaces.ModuleEntity;
import ir.sbpro.springdb.modules.studios.StudioModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "games")
public class GameModel extends ModuleEntity {
    @Column(nullable = false)
    private int year;

    @NotBlank(message = "Name is blank!")
    @Column(nullable = false)
    private String name;

    private String link;
    private String price;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStudioPk(){
        return studio != null ? studio.getPk() : null;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
