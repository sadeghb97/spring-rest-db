package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.springdb.enums.UserGameStatus;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.modules.users.UserService;
import ir.sbpro.springdb.plat_modules.usergames.UserGame;
import ir.sbpro.springdb.plat_modules.usergames.UserGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/platgames")
public class PlatGTemplateController {
    PlatGameService platGameService;
    UserService userService;
    UserGameService userGameService;

    @Autowired
    PlatGTemplateController(PlatGameService platGameService, UserService userService, UserGameService userGameService){
        this.platGameService = platGameService;
        this.userService = userService;
        this.userGameService = userGameService;
    }

    @GetMapping(value = {"", "/"})
    public String getPlatGamesView(Model model,
                               @ModelAttribute("gsearch") PlatinumGame platGame,
                               @PageableDefault(size = 20) Pageable pageable){

        String sort = null;
        Optional<Sort.Order> orderOptional = pageable.getSort().get().findFirst();
        if(orderOptional.isPresent()) sort = orderOptional.get().getProperty();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        model.addAttribute("records",
                platGameService.findBySearchQuery(pageRequest, platGame, sort));
        model.addAttribute("type", "plats");
        return "plat_games/plat_games";
    }

    @GetMapping(value = {"wishlist", "wishlist/"})
    public String getWishListView(Model model,
                                   @ModelAttribute("gsearch") PlatinumGame platGame,
                                   @PageableDefault(size = 20) Pageable pageable){

        UserModel userModel = userService.getCurrentUser();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        model.addAttribute("records",
                platGameService.findWishListGameBySQ(pageRequest, platGame, userModel.getPk()));
        model.addAttribute("type", "wishlist");
        return "plat_games/plat_games";
    }

    @GetMapping(value = "/showgame/{game_pk}/")
    public String getPlatGameView(Model model, @PathVariable("game_pk") String gamePk){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(gamePk);
        if(gameOptional.isEmpty()) return "redirect:platgames/";

        boolean inWishlist = false;
        PlatinumGame platinumGame = gameOptional.get();
        UserModel userModel = userService.getCurrentUser();
        if(userModel.getWishlist().contains(platinumGame)) inWishlist = true;
        boolean inPsnGames = userModel.hasPlatGame(platinumGame.getId());

        model.addAttribute("record", gameOptional.get());
        model.addAttribute("in_wishlist", inWishlist);
        model.addAttribute("in_psngames", inPsnGames);
        return "plat_games/show_plat_game";
    }

    @PostMapping(value = "/showgame/{game_pk}/add_wish/")
    public String addToWishList(@PathVariable("game_pk") String gamePk){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(gamePk);
        if(gameOptional.isEmpty()) return "redirect:platgames/";

        UserModel userModel = userService.getCurrentUser();

        PlatinumGame platinumGame = gameOptional.get();
        if(!userModel.getWishlist().contains(platinumGame)){
            userModel.getWishlist().add(platinumGame);
            userService.repository.save(userModel);
        }

        return "redirect:/platgames/showgame/" + gamePk + "/";
    }

    @PostMapping(value = "/showgame/{game_pk}/delete_wish/")
    public String removeFromWishList(@PathVariable("game_pk") String gamePk){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(gamePk);
        if(gameOptional.isEmpty()) return "redirect:platgames/";

        UserModel userModel = userService.getCurrentUser();

        PlatinumGame platinumGame = gameOptional.get();
        if(userModel.getWishlist().contains(platinumGame)){
            userModel.getWishlist().remove(platinumGame);
            userService.repository.save(userModel);
        }

        return "redirect:/platgames/showgame/" + gamePk + "/";
    }

    @PostMapping(value = "/showgame/{game_pk}/add_user_game/")
    public String addToUserGames(@PathVariable("game_pk") String gamePk){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(gamePk);
        if(gameOptional.isEmpty()) return "redirect:platgames/";

        UserModel userModel = userService.getCurrentUser();
        PlatinumGame platinumGame = gameOptional.get();
        if(userModel.hasPlatGame(platinumGame.getId())){
            return "redirect:/platgames/showgame/" + gamePk + "/";
        }

        UserGame userGame = new UserGame();
        userGame.setPlatinumGame(platinumGame);
        userGame.setUser(userModel);
        userGame.setStatus(UserGameStatus.PLAYLIST);
        userGame = userGameService.save(userGame);

        userModel.getPsnGames().add(userGame);
        userService.repository.save(userModel);

        return "redirect:/platgames/showgame/" + gamePk + "/";
    }
}
