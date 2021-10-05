package ir.sbpro.springdb.modules.studios;

import ir.sbpro.springdb.modules._interfaces.ModuleTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudioTemplateController {
    StudioService studioService;
    ModuleTemplateUtils<StudioModel> templateUtils;

    @Autowired
    public StudioTemplateController(StudioService studioService) {
        this.studioService = studioService;
        templateUtils = new ModuleTemplateUtils<StudioModel>(studioService);
    }

    @GetMapping(value = "/studios")
    public String getRecordsView(Model model){
        templateUtils.bindAllRecords(model);
        return "studios/studios";
    }

    @GetMapping(value = "/studioform")
    public String getNewRecordView(Model model){
        model.addAttribute("record", new StudioModel());
        return "studios/studio_form";
    }

    @GetMapping(value = "/studioform/{record_pk}")
    public String getShowRecordView(Model model, @PathVariable("record_pk") Long recordPk){
        boolean found = templateUtils.bindSingleRecord(model, recordPk);
        if(found) return "studios/studio_form";
        return "redirect:/studios";
    }

    @PostMapping(value = "/insertstudio")
    public String insertRecordFromForm(@ModelAttribute StudioModel studio){
        return templateUtils.getInsertRecord(studio, null, "studioform", "studioform");
    }

    @PostMapping(value = "/delstudio")
    public String deleteRecord(@RequestParam("delpk") Long recordPk){
        return templateUtils.deleteRecord("/studios", recordPk);
    }
}
