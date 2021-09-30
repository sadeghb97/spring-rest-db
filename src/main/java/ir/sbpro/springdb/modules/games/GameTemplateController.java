package ir.sbpro.springdb.modules.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameTemplateController {
    GameService gameService;

    @Autowired
    GameTemplateController(GameService gameService){
        this.gameService = gameService;
    }

    @GetMapping(value = {"", "/"})
    public String getGamesView(Model model){
        model.addAttribute("games", gameService.getAllGames());
        return "games";
    }
}
