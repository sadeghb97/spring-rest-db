package ir.sbpro.springdb.modules.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.sbpro.springdb.enums.Role;
import ir.sbpro.springdb.modules._interfaces.HasCover;
import ir.sbpro.springdb.modules._interfaces.HasPassword;
import ir.sbpro.springdb.modules._interfaces.ModuleEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserModel extends ModuleEntity implements Serializable, HasCover, HasPassword {
    @Column(nullable = false, unique = true)
    private String username;

    @Email
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

    UserModel(){
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
}
