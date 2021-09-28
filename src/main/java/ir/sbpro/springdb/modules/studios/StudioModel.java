package ir.sbpro.springdb.modules.studios;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.sbpro.springdb.modules.games.GameModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "studios")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pk")
public class StudioModel {
    @Id
    @GeneratedValue
    private Long pk;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "studio")
    private List<GameModel> games;

    public StudioModel(){}

    public StudioModel(String name) {
        this.name = name;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
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
