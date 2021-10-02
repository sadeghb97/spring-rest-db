package ir.sbpro.springdb.modules.games;

import ir.sbpro.springdb.modules.studios.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class GameTemplateController {
    GameService gameService;
    StudioService studioService;

    @Autowired
    GameTemplateController(GameService gameService, StudioService studioService){
        this.gameService = gameService;
        this.studioService = studioService;
    }

    @GetMapping(value = {"", "/"})
    public String getGamesView(Model model){
        model.addAttribute("games", gameService.getAllGames());
        return "games";
    }

    @GetMapping(value = "/newgame")
    public String getNewGameView(Model model){
        model.addAttribute("gameObject", new GameModel());
        model.addAttribute("studios", studioService.getAllStudios());
        return "games/game_form";
    }

    @GetMapping(value = "/showgame/{game_pk}/")
    public String getShowGameView(Model model, @PathVariable("game_pk") Long gamePk){
        Optional<GameModel> gameOptional = gameService.getGame(gamePk);
        if(gameOptional.isEmpty()) return "redirect:/";

        model.addAttribute("gameObject", gameOptional.get());
        model.addAttribute("studios", studioService.getAllStudios());
        return "games/show_game";
    }

    @PostMapping(value = "/delgame")
    public String deleteGame(@RequestParam("delpk") Long gamePk){
        gameService.gameRepository.deleteById(gamePk);
        return "redirect:/";
    }

    @PostMapping(value = "/insertgame")
    public String insertGameFromForm(@ModelAttribute GameModel game, @RequestParam("cover_file") MultipartFile file){
        ResponseEntity<Object> responseEntity = null;
        if(game.getPk() == null || game.getPk() == 0) {
            responseEntity = gameService.registerGame(game, file, false);
        }
        else {
            responseEntity = gameService.registerGame(game, file, true);
        }

        if (responseEntity.getBody() instanceof GameModel) {
            GameModel gameModel = (GameModel) responseEntity.getBody();
            return "redirect:/showgame/" + gameModel.getPk() + "/";
        }

        return "redirect:/newgame";
    }
}
