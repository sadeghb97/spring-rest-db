package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.PlatPricesApi;
import ir.sbpro.models.MetacriticGame;
import ir.sbpro.models.PSNProfilesGame;
import ir.sbpro.models.PlatPricesGame;
import ir.sbpro.springdb.AppSingleton;
import ir.sbpro.springdb.enums.UserGameStatus;
import ir.sbpro.springdb.modules.users.UserModel;
import ir.sbpro.springdb.modules.users.UserService;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGamesService;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGame;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGamesRepository;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGamesService;
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

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/platgames")
public class PlatGTemplateController {
    PlatGameService platGameService;
    UserService userService;
    UserGameService userGameService;
    PSNGamesService psnGamesService;
    HLTBGamesService hltbGamesService;
    MetaCriticGamesService metaCriticGamesService;

    @Autowired
    PlatGTemplateController(PlatGameService platGameService, UserService userService,
                            UserGameService userGameService, PSNGamesService psnGamesService,
                            HLTBGamesService hltbGamesService, MetaCriticGamesService metaCriticGamesService){
        this.platGameService = platGameService;
        this.userService = userService;
        this.userGameService = userGameService;
        this.psnGamesService = psnGamesService;
        this.hltbGamesService = hltbGamesService;
        this.metaCriticGamesService = metaCriticGamesService;
    }

    @GetMapping(value = {"", "/"})
    public String getPlatGamesView(Model model,
                               @ModelAttribute("gsearch") PlatinumGame platGame,
                               @RequestParam(value = "libincluded", defaultValue = "0") Integer libIncluded,
                               @PageableDefault(size = 20) Pageable pageable){

        String sort = null;
        Optional<Sort.Order> orderOptional = pageable.getSort().get().findFirst();
        if(orderOptional.isPresent()) sort = orderOptional.get().getProperty();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        UserModel userModel = userService.getCurrentUser();
        PlatGamesFilter platGamesFilter = new PlatGamesFilter().setLibIncluded(libIncluded > 0);
        platGamesFilter.userPk = userModel.getPk();

        model.addAttribute("records",
                platGameService.findBySearchQuery(pageRequest, platGame, sort, platGamesFilter));
        model.addAttribute("filters", platGamesFilter);
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
        PlatinumGame platinumGame = platGameService.getPlatinumGame(gamePk);
        if(platinumGame == null) return "redirect:/platgames/";

        boolean inWishlist = false;
        UserModel userModel = userService.getCurrentUser();
        if(userModel.getWishlist().contains(platinumGame)) inWishlist = true;
        boolean inPsnGames = userModel.hasPlatGame(platinumGame.getId());

        model.addAttribute("record", platinumGame);
        model.addAttribute("in_wishlist", inWishlist);
        model.addAttribute("in_psngames", inPsnGames);
        model.addAttribute("patch_mode", true);
        model.addAttribute("psnp", platinumGame.getLink());
        model.addAttribute("hltb", platinumGame.getHlGame() != null ?
                platinumGame.getHlGame().getFullLink() : null);
        model.addAttribute("metaurl", platinumGame.getMetacriticGame() != null ?
                platinumGame.getMetacriticGame().getLink() : null);
        model.addAttribute("psnid", platinumGame.getStoreGame() != null ?
                platinumGame.getStoreGame().getId() : null);
        return "plat_games/show_plat_game";
    }

    @GetMapping(value = "/add/")
    public String getAddPlatGameView(Model model){
        model.addAttribute("patch_mode", false);
        return "plat_games/add_plat_game";
    }

    @GetMapping(value = "/agg_add/")
    public String getAddPlatGamesView(){
        return "plat_games/add_plat_games";
    }

    private String _getShowGameRedirectRoute(String psnpId){
        return "redirect:/platgames/showgame/" + psnpId + "/";
    }

    private void _updatePSN(PlatinumGame platinumGame, String newPsnId, boolean deepMode){
        PSNGame psnGame = null;
        if(deepMode) psnGame = psnGamesService.save(newPsnId);
        else psnGame = psnGamesService.update(newPsnId);

        if(psnGame != null){
            platinumGame.setStoreGame(psnGame);
            platGameService.platGameRepository.save(platinumGame);
        }
    }

    private void _updatePSN(PlatinumGame platinumGame){
        try {
            PlatPricesGame platPricesGame =
                    PlatPricesApi.getPlatPricesGameWithName(platinumGame.getName(),
                            AppSingleton.getInstance().networkConnection);
            ir.sbpro.models.PSNGame psnFetcher = new ir.sbpro.models.PSNGame();
            psnFetcher.update(platPricesGame);
            PSNGame springPSNGame = new PSNGame();
            springPSNGame.load(psnFetcher);
            springPSNGame = psnGamesService.gamesRepository.save(springPSNGame);

            if (springPSNGame != null) {
                platinumGame.setStoreGame(springPSNGame);
                platGameService.platGameRepository.save(platinumGame);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Ex: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/update/psn/{psnpid}/")
    public String updatePSN(@PathVariable("psnpid") String psnpId){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        if(platinumGame.getStoreGame() != null)
            _updatePSN(platinumGame, platinumGame.getStoreGame().getId(), false);
        else _updatePSN(platinumGame);
        return _getShowGameRedirectRoute(psnpId);
    }

    @PostMapping(value = "/patch/psn/{psnpid}/")
    public String patchPSN(@PathVariable("psnpid") String psnpId, @RequestParam("psnid") String newPsnID){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        _updatePSN(platinumGame, newPsnID, true);
        return _getShowGameRedirectRoute(psnpId);
    }

    private void _updatePSNP(PlatinumGame platinumGame, String newPsnpUrl){
        try {
            PSNProfilesGame psnpGame = new PSNProfilesGame();
            psnpGame.update(newPsnpUrl, AppSingleton.getInstance().networkConnection);
            platinumGame.load(psnpGame);
            platGameService.platGameRepository.save(platinumGame);
        }
        catch (Exception ex){}
    }

    @PostMapping(value = "/update/psnp/{psnpid}/")
    public String updatePSNP(@PathVariable("psnpid") String psnpId){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        _updatePSNP(platinumGame, platinumGame.getLink());
        return _getShowGameRedirectRoute(psnpId);
    }

    @PostMapping(value = "/patch/psnp/{psnpid}/")
    public String patchPSNP(@PathVariable("psnpid") String psnpId, @RequestParam("psnp") String newPsnUrl){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        _updatePSNP(platinumGame, newPsnUrl);
        return _getShowGameRedirectRoute(psnpId);
    }

    private void _updateHLTB(PlatinumGame platinumGame, String newHLTBUrl, boolean deepMode){
        HLTBGame hltbGame = null;
        if(deepMode) hltbGame = hltbGamesService.save(newHLTBUrl);
        else hltbGame = hltbGamesService.update(newHLTBUrl);

        if(hltbGame != null){
            platinumGame.setHlGame(hltbGame);
            platGameService.platGameRepository.save(platinumGame);
        }
    }

    @PostMapping(value = "/update/hltb/{psnpid}/")
    public String updateHLTB(@PathVariable("psnpid") String psnpId){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        if(platinumGame.getHlGame() != null)
            _updateHLTB(platinumGame, platinumGame.getHlGame().getFullLink(), false);
        return _getShowGameRedirectRoute(psnpId);
    }

    @PostMapping(value = "/patch/hltb/{psnpid}/")
    public String patchHLTB(@PathVariable("psnpid") String psnpId, @RequestParam("hltb") String newHLTBUrl){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        _updateHLTB(platinumGame, newHLTBUrl, true);
        return _getShowGameRedirectRoute(psnpId);
    }

    private void _updateMetaCritic(PlatinumGame platinumGame, String newMetaUrl, boolean deepMode){
        MetaCriticGame metaCriticGame = null;
        if(deepMode) metaCriticGame = metaCriticGamesService.save(newMetaUrl);
        else metaCriticGame = metaCriticGamesService.update(newMetaUrl);

        if(metaCriticGame != null){
            platinumGame.setMetaCriticGame(metaCriticGame);
            platGameService.platGameRepository.save(platinumGame);
        }
    }

    @PostMapping(value = "/update/metacritic/{psnpid}/")
    public String updateMetaCritic(@PathVariable("psnpid") String psnpId){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        if(platinumGame.getMetacriticGame() != null)
            _updateMetaCritic(platinumGame, platinumGame.getMetacriticGame().getLink(), false);
        else {
            String mcSlugPrediction = MetacriticGame.getSlugPrediction(platinumGame.getName());
            String finalUrl = MetaCriticGame.START_URL + mcSlugPrediction;
            _updateMetaCritic(platinumGame, finalUrl, true);
        }
        return _getShowGameRedirectRoute(psnpId);
    }

    @PostMapping(value = "/patch/metacritic/{psnpid}/")
    public String patchMetaCritic(@PathVariable("psnpid") String psnpId, @RequestParam("metaurl") String newMetaUrl){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
        if(platinumGame == null) return "redirect:/platgames/";

        _updateMetaCritic(platinumGame, newMetaUrl, true);
        return _getShowGameRedirectRoute(psnpId);
    }

    @PostMapping(value = "/insertgames/")
    public String insertPlatGames(@RequestParam("psnp") String psnpUrls){
        String[] urls = psnpUrls.split("\n");
        int sucCount = 0;
        int erCount = 0;

        for(int i=0; urls.length>i; i++) {
            try {
                String pUrl = urls[i];
                int pos1 = pUrl.lastIndexOf("/");
                int pos2 = pUrl.indexOf("-", pos1);
                if(pos1 < 0 || pos2 < 0) continue;

                String psnpId = pUrl.substring(pos1 + 1, pos2);
                PlatinumGame platinumGame = platGameService.getPlatinumGame(psnpId);
                if(platinumGame != null) continue;

                platinumGame = new PlatinumGame();
                PSNProfilesGame psnpFetcher = new PSNProfilesGame();
                psnpFetcher.update(pUrl, AppSingleton.getInstance().networkConnection);
                platinumGame.load(psnpFetcher);
                platGameService.platGameRepository.save(platinumGame);
                System.out.println("PSNP Game Fetched!");
                sucCount++;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                erCount++;
            }
        }

        return "redirect:/platgames/";
    }

    @PostMapping(value = "/insertgame/")
    public String insertPlatGame(@RequestParam("psnp") String psnpUrl, @RequestParam("hltb") String hltbUrl,
                                 @RequestParam("psnid") String psnId,
                                 @RequestParam("metaurl") String metaUrl){

        PSNGame psnGame = psnGamesService.save(psnId);
        if(psnGame == null) System.out.println("PSN Game Missed");

        HLTBGame hltbGame = hltbGamesService.save(hltbUrl);
        if(hltbGame == null) System.out.println("HLTB Game Missed");

        MetaCriticGame metaCriticGame = metaCriticGamesService.save(metaUrl);
        if(metaCriticGame == null) System.out.println("MetaCritic Game Missed");

        try{
            PlatinumGame platinumGame = new PlatinumGame();
            PSNProfilesGame psnpFetcher = new PSNProfilesGame();
            psnpFetcher.update(psnpUrl, AppSingleton.getInstance().networkConnection);
            platinumGame.load(psnpFetcher, hltbGame, psnGame, metaCriticGame);
            platGameService.platGameRepository.save(platinumGame);
            return _getShowGameRedirectRoute(platinumGame.getId());
        }
        catch (Exception ex){
            ex.printStackTrace();
            return "redirect:/platgames/add/";
        }
    }

    @PostMapping(value = "/insertgame_psnid/")
    public String insertPlatGameWithPSNID(@RequestParam("psnid") String psnID){
        Optional<PlatinumGame> pgOptional = platGameService.initPlatinumGameWithPSNID(
                psnGamesService, metaCriticGamesService, psnID);

        if(pgOptional.isPresent()){
            return _getShowGameRedirectRoute(pgOptional.get().getId());
        }

        return "redirect:/platgames/add/";
    }

    @PostMapping(value = "/insertgame_ppid/")
    public String insertPlatGameWithPPID(@RequestParam("ppid") String ppid){
        Optional<PlatinumGame> pgOptional = platGameService.initPlatinumGameWithPPID(
                psnGamesService, metaCriticGamesService, ppid);

        if(pgOptional.isPresent()){
            return _getShowGameRedirectRoute(pgOptional.get().getId());
        }

        return "redirect:/platgames/add/";
    }

    @PostMapping(value = "/insertgame_name/")
    public String insertPlatGameWithName(@RequestParam("game_name") String gameName){
        Optional<PlatinumGame> pgOptional = platGameService.initPlatinumGameWithName(
                psnGamesService, metaCriticGamesService, gameName);

        if(pgOptional.isPresent()){
            return _getShowGameRedirectRoute(pgOptional.get().getId());
        }

        return "redirect:/platgames/add/";
    }

    @PostMapping(value = "/showgame/{game_pk}/add_wish/")
    public String addToWishList(@PathVariable("game_pk") String gamePk){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(gamePk);
        if(platinumGame == null) return "redirect:/platgames/";

        UserModel userModel = userService.getCurrentUser();

        if(!userModel.getWishlist().contains(platinumGame)){
            userModel.getWishlist().add(platinumGame);
            userService.repository.save(userModel);
        }

        return _getShowGameRedirectRoute(gamePk);
    }

    @PostMapping(value = "/showgame/{game_pk}/delete_wish/")
    public String removeFromWishList(@PathVariable("game_pk") String gamePk){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(gamePk);
        if(platinumGame == null) return "redirect:/platgames/";

        UserModel userModel = userService.getCurrentUser();

        if(userModel.getWishlist().contains(platinumGame)){
            userModel.getWishlist().remove(platinumGame);
            userService.repository.save(userModel);
        }

        return _getShowGameRedirectRoute(gamePk);
    }

    @PostMapping(value = "/showgame/{game_pk}/add_user_game/")
    public String addToUserGames(@PathVariable("game_pk") String gamePk){
        PlatinumGame platinumGame = platGameService.getPlatinumGame(gamePk);
        if(platinumGame == null) return "redirect:/platgames/";

        UserModel userModel = userService.getCurrentUser();
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

        return _getShowGameRedirectRoute(gamePk);
    }
}
