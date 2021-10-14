package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb._module_interfaces.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ModuleService<UserModel> {

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
