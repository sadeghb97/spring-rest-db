package ir.sbpro.springdb.modules.games;

import ir.sbpro.springdb._module_interfaces.ModuleTemplateUtils;
import ir.sbpro.springdb.enums.UserGameStatus;
import ir.sbpro.springdb.modules.studios.StudioService;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.modules.users.UserService;
import ir.sbpro.springdb.plat_modules.usergames.UserGame;
import ir.sbpro.springdb.plat_modules.usergames.UserGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class GameTemplateController {
    GameService gameService;
    StudioService studioService;
    UserService userService;
    UserGameService userGameService;
    ModuleTemplateUtils<GameModel> templateUtils;

    @Autowired
    GameTemplateController(GameService gameService, StudioService studioService,
                           UserService userService, UserGameService userGameService){
        this.gameService = gameService;
        this.studioService = studioService;
        this.userService = userService;
        this.userGameService = userGameService;
        templateUtils = new ModuleTemplateUtils<GameModel>(gameService);
    }

    @GetMapping(value = {"", "/"})
    public String getGamesView(Model model,
                               @ModelAttribute("gsearch") GameModel gameModel,
                               @PageableDefault(size = 20) Pageable pageable){

        model.addAttribute("records",
                gameService.findBySearchQuery(pageable, gameModel));
        return "games";
    }

    @GetMapping(value = "/newgame")
    public String getNewGameView(Model model){
        model.addAttribute("record", new GameModel());
        model.addAttribute("studios", studioService.getAllRecords());
        return "games/game_form";
    }

    @GetMapping(value = "/showgame/{game_pk}/")
    public String getShowGameView(Model model, @PathVariable("game_pk") Long gamePk){
        Optional<GameModel> gameOptional = gameService.getRecord(gamePk);
        if(gameOptional.isEmpty()) return "redirect:/";

        //mitavan principal ro dorost shabihe model dar ghesmate argoman ha gereft
        //vali dar an surat ghabeliate cast kardan be user ra nadarad
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean access =
                currentUser.getAuthorities().stream().anyMatch((role) -> role.getAuthority().equals("ROLE_ADMIN"));

        UserModel userModel = userService.getCurrentUser();
        boolean inPsnGames = userModel.hasIndieGame(gamePk);

        model.addAttribute("gameObject", gameOptional.get());
        model.addAttribute("studios", studioService.getAllRecords());
        model.addAttribute("access", access ? "yes" : "no");
        model.addAttribute("in_psngames", inPsnGames);
        return "games/show_game";
    }

    private String _getShowGameRedirectRoute(Long gamePk){
        return "redirect:/showgame/" + gamePk + "/";
    }

    @PostMapping(value = "/delgame")
    public String deleteGame(@RequestParam("delpk") Long recordPk){
        return templateUtils.deleteRecord("/", recordPk);
    }

    @PostMapping(value = "/insertgame")
    public String insertGameFromForm(@Valid @ModelAttribute(name = "record") GameModel game,
                                     final BindingResult bindingResult,
                                     @RequestParam("cover_file") MultipartFile file){

        if(bindingResult.hasErrors()){
            //return "games/game_form";
            //ba code bala dar safhe ghabli mande va error ra mitavanim namayesh dahim
            //vali object haye pas daded shode be form khahad parid

            if(game.getPk() == null) return "redirect:/newgame";
            return "redirect:/showgame/" + game.getPk() + "/";
        }
        return templateUtils.getInsertRecord(game, file, "showgame", "newgame");
    }

    @PostMapping(value = "/showgame/{game_pk}/add_user_game/")
    public String addToUserGames(@PathVariable("game_pk") Long gamePk){
        Optional<GameModel> gameOptional = gameService.getRecord(gamePk);
        if(gameOptional.isEmpty()) return "redirect:/";

        GameModel game = gameOptional.get();
        UserModel userModel = userService.getCurrentUser();

        if(userModel.hasIndieGame(gamePk)){
            return "redirect:/showgame/" + gamePk + "/";
        }

        UserGame userGame = new UserGame();
        userGame.setIndieGame(game);
        userGame.setUser(userModel);
        userGame.setStatus(UserGameStatus.PLAYLIST);
        userGame = userGameService.save(userGame);

        userModel.getPsnGames().add(userGame);
        userService.repository.save(userModel);

        return _getShowGameRedirectRoute(gamePk);
    }
}
