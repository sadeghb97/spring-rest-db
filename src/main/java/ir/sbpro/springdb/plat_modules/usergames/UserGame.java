package ir.sbpro.springdb.plat_modules.usergames;

import ir.sbpro.springdb.enums.UserGameStatus;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserGame {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private double rate;

    private UserGameStatus status;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private PlatinumGame platinumGame;

    @ManyToOne
    @JoinColumn(referencedColumnName = "pk")
    private UserModel user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

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
}
