package ir.sbpro.springdb.plat_modules.usergames;

import ir.sbpro.springdb.enums.UserGamePlatform;
import ir.sbpro.springdb.enums.PurchaseType;
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

import java.util.*;

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
                                   @PageableDefault(size = 24) Pageable pageable){

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

    @GetMapping(value = { "/bestgames"})
    public String getBestGamesView(Model model){
        UserModel userModel = userService.getCurrentUser();
        ArrayList<UserGame> allGames = userGameService.getUserRankedGames(userModel.getPk());
        BestGamesOrders bestGamesOrders = new BestGamesOrders();
        bestGamesOrders.load(allGames);

        model.addAttribute("games", bestGamesOrders);
        return "bestgames/best_games";
    }

    @GetMapping(value = { "/bestgames_ranking"})
    public String getBestGamesRankingView(Model model){
        UserModel userModel = userService.getCurrentUser();
        ArrayList<UserGame> allGames = userGameService.getUserRankedGames(userModel.getPk());
        BestGamesOrders bestGamesOrders = new BestGamesOrders();
        bestGamesOrders.load(allGames);

        model.addAttribute("games", bestGamesOrders);
        return "bestgames/best_games_ranking";
    }

    @PostMapping(value = "/uptopgames")
    public String updateBestGames(@RequestParam("ranked_ids") String rankedIds,
                                  @RequestParam("unranked_ids") String unrankedIds){

        return _getBestGamesView(rankedIds, unrankedIds, false);
    }

    @PostMapping(value = "/uptopgames_sp")
    public String updateBestGamesSpread(@RequestParam("ranked_ids") String rankedIds,
                                  @RequestParam("unranked_ids") String unrankedIds){

        return _getBestGamesView(rankedIds, unrankedIds, true);
    }

    public String _getBestGamesView(String rankedIds, String unrankedIds, boolean spreadMode){
        if(!rankedIds.isEmpty()) {
            String[] rankedIdsArray = rankedIds.split(",");
            for (int i = 0; rankedIdsArray.length > i; i++) {
                long gameId = Long.parseLong(rankedIdsArray[i]);
                int order = i + 1;

                Optional<UserGame> userGameOptional = userGameService.repository.findById(gameId);
                if (userGameOptional.isPresent()) {
                    UserGame userGame = userGameOptional.get();
                    userGame.setRank(order);
                    userGameService.save(userGame);
                }
            }
        }

        if(!unrankedIds.isEmpty()) {
            String[] unrankedIdsArray = unrankedIds.split(",");
            for (int i = 0; unrankedIdsArray.length > i; i++) {
                long gameId = Long.parseLong(unrankedIdsArray[i]);
                int order = (unrankedIdsArray.length * -1) + i;

                Optional<UserGame> userGameOptional = userGameService.repository.findById(gameId);
                if (userGameOptional.isPresent()) {
                    UserGame userGame = userGameOptional.get();
                    userGame.setRank(order);
                    userGameService.save(userGame);
                }
            }
        }

        if(!spreadMode) return "redirect:/library/bestgames";
        else return "redirect:/library/bestgames_ranking";
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

        UserGameStatus[] ugsValues = UserGameStatus.values();
        PurchaseType[] ptValues = PurchaseType.values();
        UserGamePlatform[] platforms = UserGamePlatform.values();

        model.addAttribute("record", userGame);
        model.addAttribute("statuses", ugsValues);
        model.addAttribute("ptlist", ptValues);
        model.addAttribute("platforms", platforms);

        return "usergames/user_game_form";
    }

    @PostMapping(value = "/patch/")
    public String patchUserGame(@ModelAttribute("record") UserGame userGame){
        if(!userGameService.repository.existsById(userGame.getId())){
            return "redirect:/library/";
        }

        UserGame regUserGame = userGameService.repository.getById(userGame.getId());
        regUserGame.setRate(userGame.getRate());
        regUserGame.setProgress(userGame.getProgress());
        regUserGame.setPlaytime(userGame.getPlaytime());
        regUserGame.setStatus(userGame.getStatus());
        regUserGame.setPurchaseType(userGame.getPurchaseType());
        regUserGame.setPlatform(userGame.getPlatform());

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
