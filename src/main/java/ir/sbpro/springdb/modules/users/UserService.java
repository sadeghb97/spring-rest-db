package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb.modules.EntityUtils;
import ir.sbpro.springdb.modules.games.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserModel> getUser(Long userPk){
        return userRepository.findById(userPk);
    }

    public List<UserModel> getAllUsers(){
        return userRepository.findAll();
    }

    public ResponseEntity<Object> registerUser(UserModel user, MultipartFile file, boolean duplicateAllowed){
        EntityUtils<UserModel, UserRepository> entityUtils =
                new EntityUtils(userRepository, user, "user");

        return entityUtils.patchEntity(file, duplicateAllowed);
    }
}
