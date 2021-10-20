package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb._module_interfaces.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ModuleService<UserModel> {

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public UserModel getCurrentUser(){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findByUsername(principal.getUsername());
    }

    public UserModel findByUsername(String username){
        UserRepository userRepository = (UserRepository) repository;
        return userRepository.findByUsername(username);
    }
}
