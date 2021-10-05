package ir.sbpro.springdb.modules.studios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudioTemplateController {
    StudioService studioService;

    @Autowired
    public StudioTemplateController(StudioService studioService) {
        this.studioService = studioService;
    }

    @GetMapping(value = "/studios")
    public String getStudiosView(Model model){
        List<StudioModel> studios = studioService.getAllRecords();
        model.addAttribute("studios", studios);
        return "studios/studios";
    }
}
