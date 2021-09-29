package ir.sbpro.springdb.modules;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pk")
public class ModuleEntity {
    @Id
    @GeneratedValue
    protected Long pk;

    public ModuleEntity(){}

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @JsonIgnore
    public Long getPrimaryKey(){
        return pk;
    }
}
