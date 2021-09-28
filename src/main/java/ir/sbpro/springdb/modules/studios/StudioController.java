package ir.sbpro.springdb.modules.studios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studios")
public class StudioController {
    StudioService studioService;

    @Autowired
    public StudioController(StudioService studioService) {
        this.studioService = studioService;
    }

    @GetMapping(value = {"", "/"})
    public List<StudioModel> getAllStudios(){
        return studioService.getAllStudios();
    }

    @PostMapping(value = {"", "/"})
    public StudioModel registerStudio(@RequestBody StudioModel studio){
        return studioService.registerStudio(studio);
    }
}
