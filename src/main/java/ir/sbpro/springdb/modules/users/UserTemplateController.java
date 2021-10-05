package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb.modules._interfaces.ModuleTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
public class UserTemplateController {
    UserService userService;
    ModuleTemplateUtils<UserModel> templateUtils;

    @Autowired
    public UserTemplateController(UserService userService) {
        this.userService = userService;
        templateUtils = new ModuleTemplateUtils<UserModel>(userService);
    }

    @GetMapping(value = "/users")
    public String getUsersView(Model model){
        List<UserModel> users = userService.getAllRecords();
        model.addAttribute("users", users);
        return "users/users";
    }

    @GetMapping(value = "/userform")
    public String getNewUserView(Model model){
        model.addAttribute("userObject", new UserModel());
        return "users/user_form";
    }

    @GetMapping(value = "/userform/{user_pk}")
    public String getShowUserView(Model model, @PathVariable("user_pk") Long userPk){
        Optional<UserModel> userOptional = userService.getRecord(userPk);
        if(userOptional.isEmpty()) return "redirect:/users";

        model.addAttribute("userObject", userOptional.get());
        return "users/user_form";
    }

    @PostMapping(value = "/insertuser")
    public String insertUserFromForm(@ModelAttribute UserModel user, @RequestParam("cover_file") MultipartFile file){
        return templateUtils.getInsertRecord(user, file, "userform", "userform");
    }
}
