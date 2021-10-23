package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.models.PSNProfilesGame;
import ir.sbpro.springdb.enums.UserGameStatus;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.modules.users.UserService;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGamesService;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
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
    PSNGamesService psnGamesService;
    HLTBGamesService hltbGamesService;

    @Autowired
    PlatGTemplateController(PlatGameService platGameService, UserService userService,
                            UserGameService userGameService, PSNGamesService psnGamesService,
                            HLTBGamesService hltbGamesService){
        this.platGameService = platGameService;
        this.userService = userService;
        this.userGameService = userGameService;
        this.psnGamesService = psnGamesService;
        this.hltbGamesService = hltbGamesService;
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

    @GetMapping(value = "/add/")
    public String getAddPlatGameView(Model model){
        return "plat_games/add_plat_game";
    }

    @PostMapping(value = "/update/psn/{psnpid}/")
    public String updatePSN(@PathVariable("psnpid") String psnpId){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(psnpId);
        if(gameOptional.isEmpty()) return "redirect:/platgames/";

        PlatinumGame platinumGame = gameOptional.get();
        if(platinumGame.getStoreGame() != null) {
            PSNGame psnGame = psnGamesService.update(platinumGame.getStoreGame().getId());
            if(psnGame != null){
                platinumGame.setStoreGame(psnGame);
                platGameService.platGameRepository.save(platinumGame);
            }
        }

        return "redirect:/platgames/showgame/" + psnpId + "/";
    }

    @PostMapping(value = "/update/psnp/{psnpid}/")
    public String updatePSNP(@PathVariable("psnpid") String psnpId){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(psnpId);
        if(gameOptional.isEmpty()) return "redirect:/platgames/";

        try {
            PlatinumGame platinumGame = gameOptional.get();
            PSNProfilesGame psnpGame = new PSNProfilesGame();
            psnpGame.update(platinumGame.getLink());
            platinumGame.load(psnpGame);
            platGameService.platGameRepository.save(platinumGame);
        }
        catch (Exception ex){}

        return "redirect:/platgames/showgame/" + psnpId + "/";
    }

    @PostMapping(value = "/update/hltb/{psnpid}/")
    public String updateHLTB(@PathVariable("psnpid") String psnpId){
        Optional<PlatinumGame> gameOptional = platGameService.platGameRepository.findById(psnpId);
        if(gameOptional.isEmpty()) return "redirect:/platgames/";

        PlatinumGame platinumGame = gameOptional.get();
        if(platinumGame.getHlGame() != null) {
            HLTBGame hltbGame = hltbGamesService.update(platinumGame.getHlGame().getId());
            if(hltbGame != null){
                platinumGame.setHlGame(hltbGame);
                platGameService.platGameRepository.save(platinumGame);
            }
        }

        return "redirect:/platgames/showgame/" + psnpId + "/";
    }

    @PostMapping(value = "/insertgame/")
    public String insertPlatGame(Model model, @RequestParam("psnp") String psnpUrl,
                                     @RequestParam("hltb") String hltbUrl,
                                     @RequestParam("psnid") String psnId){

        PSNGame psnGame = psnGamesService.save(psnId);
        if(psnGame == null) System.out.println("PSN Game Missed");

        HLTBGame hltbGame = hltbGamesService.save(hltbUrl);
        if(hltbGame == null) System.out.println("HLTB Game Missed");

        try{
            PlatinumGame platinumGame = new PlatinumGame();
            PSNProfilesGame psnpFetcher = new PSNProfilesGame();
            psnpFetcher.update(psnpUrl);
            platinumGame.load(psnpFetcher, hltbGame, psnGame);
            platGameService.platGameRepository.save(platinumGame);
            return "redirect:/platgames/showgame/" + platinumGame.getId() + "/";
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "redirect:/platgames/add/";
        }
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
