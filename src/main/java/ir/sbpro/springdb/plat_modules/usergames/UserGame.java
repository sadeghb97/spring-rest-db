package ir.sbpro.springdb.plat_modules.usergames;

import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;

import javax.persistence.*;

@Entity
public class UserGame {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private double rate;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private PlatinumGame platinumGame;

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

    public PlatinumGame getPlatinumGame() {
        return platinumGame;
    }

    public void setPlatinumGame(PlatinumGame platinumGame) {
        this.platinumGame = platinumGame;
    }
}
