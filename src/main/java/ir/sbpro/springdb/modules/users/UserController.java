package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb.configs.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.API_PREFIX + "/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"", "/"})
    public List<UserModel> getAllUsers(){
        return userService.getAllRecords();
    }
}
