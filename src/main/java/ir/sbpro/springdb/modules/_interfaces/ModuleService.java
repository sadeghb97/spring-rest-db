package ir.sbpro.springdb.modules._interfaces;

import ir.sbpro.springdb.modules.games.GameModel;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.modules.users.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public class ModuleService<T extends ModuleEntity> {
    public JpaRepository<T, Long> repository;

    public ModuleService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public Optional<T> getRecord(Long userPk){
        return repository.findById(userPk);
    }

    public List<T> getAllRecords(){
        return repository.findAll();
    }

    public ResponseEntity<Object> registerRecord(T model){
        return registerRecord(model, null);
    }

    public ResponseEntity<Object> registerRecord(T model, MultipartFile file){
        return registerRecord(model, file, false);
    }

    public ResponseEntity<Object> registerRecord(T model, MultipartFile file, boolean duplicateAllowed){
        EntityUtils<T, JpaRepository<T, Long>> entityUtils =
                new EntityUtils(repository, model, "user");

        return entityUtils.patchEntity(file, duplicateAllowed);
    }
}
