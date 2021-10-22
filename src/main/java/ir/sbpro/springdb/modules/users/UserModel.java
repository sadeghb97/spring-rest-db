package ir.sbpro.springdb.modules.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ir.sbpro.springdb.enums.Role;
import ir.sbpro.springdb._module_interfaces.HasCover;
import ir.sbpro.springdb._module_interfaces.HasPassword;
import ir.sbpro.springdb._module_interfaces.ModuleEntity;
import ir.sbpro.springdb.modules.games.GameModel;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.usergames.UserGame;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserModel extends ModuleEntity implements Serializable, HasCover, HasPassword {
    @NotBlank(message = "Username is blank")
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = "Wrong email!")
    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String cover;

    private boolean enabled = true;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "authorities" , joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @OneToMany
    private List<PlatinumGame> wishlist;

    @OneToMany
    @JsonManagedReference
    private List<UserGame> psnGames;

    public UserModel(){
        roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<PlatinumGame> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<PlatinumGame> wishlist) {
        this.wishlist = wishlist;
    }

    public List<UserGame> getPsnGames() {
        return psnGames;
    }

    public void setPsnGames(List<UserGame> psnGames) {
        this.psnGames = psnGames;
    }

    public boolean hasPlatGame(String platGameId){
        return psnGames.stream().anyMatch((game) -> game.getPlatinumGame().getId().equals(platGameId));
    }
}
