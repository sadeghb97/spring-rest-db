package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb.modules._interfaces.ModuleTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String getRecordsView(Model model){
        templateUtils.bindAllRecords(model);
        return "users/users";
    }

    @GetMapping(value = "/userform")
    public String getNewRecordView(Model model){
        model.addAttribute("record", new UserModel());
        return "users/user_form";
    }

    @GetMapping(value = "/userform/{user_pk}")
    public String getShowRecordView(Model model, @PathVariable("user_pk") Long userPk){
        boolean found = templateUtils.bindSingleRecord(model, userPk);
        if(found) return "users/user_form";
        return "redirect:/users";
    }

    @PostMapping(value = "/insertuser")
    public String insertRecordFromForm(@ModelAttribute UserModel user, @RequestParam("cover_file") MultipartFile file){
        return templateUtils.getInsertRecord(user, file, "userform", "userform");
    }

    @PostMapping(value = "/deluser")
    public String deleteRecord(@RequestParam("delpk") Long recordPk){
        return templateUtils.deleteRecord("/users", recordPk);
    }
}
