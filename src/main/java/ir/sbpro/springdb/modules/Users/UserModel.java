package ir.sbpro.springdb.modules.Users;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ir.sbpro.springdb.enums.Role;
import ir.sbpro.springdb.modules.ModuleEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserModel extends ModuleEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "authorities" , joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

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
}
