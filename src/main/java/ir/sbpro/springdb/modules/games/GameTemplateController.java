package ir.sbpro.springdb.modules.games;

import ir.sbpro.springdb.modules._interfaces.ModuleTemplateUtils;
import ir.sbpro.springdb.modules.studios.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class GameTemplateController {
    GameService gameService;
    StudioService studioService;
    ModuleTemplateUtils<GameModel> templateUtils;

    @Autowired
    GameTemplateController(GameService gameService, StudioService studioService){
        this.gameService = gameService;
        this.studioService = studioService;
        templateUtils = new ModuleTemplateUtils<GameModel>(gameService);
    }

    @GetMapping(value = {"", "/"})
    public String getGamesView(Model model){
        model.addAttribute("games", gameService.getAllRecords());
        return "games";
    }

    @GetMapping(value = "/newgame")
    public String getNewGameView(Model model){
        model.addAttribute("gameObject", new GameModel());
        model.addAttribute("studios", studioService.getAllRecords());
        return "games/game_form";
    }

    @GetMapping(value = "/showgame/{game_pk}/")
    public String getShowGameView(Model model, @PathVariable("game_pk") Long gamePk){
        Optional<GameModel> gameOptional = gameService.getRecord(gamePk);
        if(gameOptional.isEmpty()) return "redirect:/";

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean access =
                currentUser.getAuthorities().stream().anyMatch((role) -> role.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("gameObject", gameOptional.get());
        model.addAttribute("studios", studioService.getAllRecords());
        model.addAttribute("access", access ? "yes" : "no");
        return "games/show_game";
    }

    @PostMapping(value = "/delgame")
    public String deleteGame(@RequestParam("delpk") Long gamePk){
        return templateUtils.deleteRecord("/", gamePk);
    }

    @PostMapping(value = "/insertgame")
    public String insertGameFromForm(@ModelAttribute GameModel game, @RequestParam("cover_file") MultipartFile file){
        return templateUtils.getInsertRecord(game, file, "showgame", "newgame");
    }
}
