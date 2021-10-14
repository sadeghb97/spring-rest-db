package ir.sbpro.springdb.plat_modules.platgames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/platgames")
public class PlatGTemplateController {
    PlatGameService platGameService;

    @Autowired
    PlatGTemplateController(PlatGameService platGameService){
        this.platGameService = platGameService;
    }

    @GetMapping(value = {"", "/"})
    public String getPlatGamesView(Model model,
                               @ModelAttribute("gsearch") PlatinumGame platGame,
                               @PageableDefault(size = 20) Pageable pageable){

        model.addAttribute("records",
                platGameService.findBySearchQuery(pageable, platGame));
        return "plat_games/plat_games";
    }

    @GetMapping(value = "/showgame/{game_pk}/")
    public String getPlatGameView(Model model, @PathVariable("game_pk") String gamePk){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(gamePk);
        if(gameOptional.isEmpty()) return "redirect:platgames/";

        model.addAttribute("record", gameOptional.get());
        return "plat_games/show_plat_game";
    }
}
