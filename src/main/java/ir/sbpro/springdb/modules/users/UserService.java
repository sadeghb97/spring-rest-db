package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb.modules._interfaces.EntityUtils;
import ir.sbpro.springdb.modules._interfaces.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends ModuleService<UserModel> {

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
