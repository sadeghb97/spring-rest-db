package ir.sbpro.springdb.modules;

import ir.sbpro.springdb.responses.ErrorsResponseMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class EntityUtils<T> {
    JpaRepository<T, Long> repository;
    ModuleEntity entity;

    public EntityUtils(JpaRepository<T, Long> repository, ModuleEntity entity){
        this.repository = repository;
        this.entity = entity;
    }

    public boolean isDuplicate(){
        if(entity.getPrimaryKey() == null) return false;
        return repository.existsById(entity.getPrimaryKey());
    }

    public static ResponseEntity<Object> getDuplicateResponse(String entityName){
        return new ErrorsResponseMap("pk",
                "This " + entityName + " already exists!").getEntityResponse();
    }
}