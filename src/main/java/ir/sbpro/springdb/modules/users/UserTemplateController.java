package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb.modules.games.GameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
public class UserTemplateController {
    UserService userService;

    @Autowired
    public UserTemplateController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public String getUsersView(Model model){
        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/users";
    }

    @GetMapping(value = "/userform")
    public String getNewGameView(Model model){
        model.addAttribute("userObject", new UserModel());
        return "users/user_form";
    }

    @GetMapping(value = "/userform/{user_pk}")
    public String getShowGameView(Model model, @PathVariable("user_pk") Long userPk){
        Optional<UserModel> userOptional = userService.getUser(userPk);
        if(userOptional.isEmpty()) return "redirect:/users";

        model.addAttribute("userObject", userOptional.get());
        return "users/user_form";
    }

    @PostMapping(value = "/insertuser")
    public String insertUserFromForm(@ModelAttribute UserModel user, @RequestParam("cover_file") MultipartFile file){
        ResponseEntity<Object> responseEntity = null;
        if(user.getPk() == null || user.getPk() == 0) {
            responseEntity = userService.registerUser(user, file, false);
        }
        else {
            responseEntity = userService.registerUser(user, file, true);
        }

        if (responseEntity.getBody() instanceof UserModel) {
            UserModel userModel = (UserModel) responseEntity.getBody();
            return "redirect:/userform/" + userModel.getPk() + "/";
        }

        return "redirect:/userform";
    }
}
