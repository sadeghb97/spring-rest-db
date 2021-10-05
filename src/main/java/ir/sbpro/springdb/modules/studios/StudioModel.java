package ir.sbpro.springdb.modules.studios;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ir.sbpro.springdb.modules._interfaces.ModuleEntity;
import ir.sbpro.springdb.modules.games.GameModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "studios")
public class StudioModel extends ModuleEntity {

    @Column(nullable = false)
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "studio")
    private List<GameModel> games;

    public StudioModel(){}

    public StudioModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameModel> getGames() {
        return games;
    }

    public void setGames(List<GameModel> games) {
        this.games = games;
    }
}
