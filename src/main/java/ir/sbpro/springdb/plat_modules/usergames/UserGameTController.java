package ir.sbpro.springdb.plat_modules.usergames;

import ir.sbpro.springdb.enums.UserGameStatus;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.modules.users.UserService;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/library")
public class UserGameTController {
    UserService userService;
    UserGameService userGameService;

    @Autowired
    UserGameTController(UserGameService userGameService, UserService userService){
        this.userGameService = userGameService;
        this.userService = userService;
    }

    @GetMapping(value = {"", "/"})
    public String getLibraryGamesView(Model model,
                                   @ModelAttribute("gsearch") PlatinumGame platGame,
                                   @PageableDefault(size = 20) Pageable pageable){

        String sort = null;
        Optional<Sort.Order> orderOptional = pageable.getSort().get().findFirst();
        if(orderOptional.isPresent()) sort = orderOptional.get().getProperty();
        UserModel userModel = userService.getCurrentUser();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        model.addAttribute("records",
                userGameService.findLibraryGames(pageRequest, platGame, userModel.getPk(), sort));
        model.addAttribute("type", "library");
        return "usergames/user_games";
    }

    @GetMapping(value = "/{game_id}/")
    public String getLibraryGameView(Model model, @PathVariable("game_id") long gameId){
        UserModel userModel = userService.getCurrentUser();
        Optional<UserGame> gameOptional = userGameService.repository.findById(gameId);
        if(gameOptional.isEmpty()) return "redirect:/library/";

        UserGame userGame = gameOptional.get();
        if(userGame.getUser().getPk() != userModel.getPk()){
            return "redirect:/library/";
        }

        HashMap<String, UserGameStatus> statuses = new HashMap<>();
        Arrays.stream(UserGameStatus.values()).forEach((st) -> {
            statuses.put(st.name(), st);
        });

        model.addAttribute("record", userGame);
        model.addAttribute("statuses", statuses.entrySet());
        return "usergames/user_game_form";
    }

    @PostMapping(value = "/patch/")
    public String patchUserGame(@ModelAttribute("record") UserGame userGame){
        if(!userGameService.repository.existsById(userGame.getId())){
            return "redirect:/library/";
        }

        UserGame regUserGame = userGameService.repository.getById(userGame.getId());
        regUserGame.setRate(userGame.getRate());
        regUserGame.setStatus(userGame.getStatus());
        userGameService.repository.save(regUserGame);

        return "redirect:/library/" + regUserGame.getId() + "/";
    }

    @PostMapping(value = "/delete/{game_id}/")
    public String deleteUserGame(@PathVariable("game_id") long gameId){
        UserGame userGame = userGameService.repository.getById(gameId);
        UserModel userModel = userGame.getUser();
        userModel.getPsnGames().remove(userGame);
        userService.repository.save(userModel);
        
        userGameService.repository.deleteById(userGame.getId());
        return "redirect:/library/";
    }
}
