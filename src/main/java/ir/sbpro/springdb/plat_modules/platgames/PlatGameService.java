package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.PlatPricesApi;
import ir.sbpro.Scrapper;
import ir.sbpro.models.PSNProfilesGame;
import ir.sbpro.models.PlatPricesGame;
import ir.sbpro.springdb.AppSingleton;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGame;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGamesService;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatGameService {
    public PlatinumGameRepository platGameRepository;

    @Autowired
    public PlatGameService(PlatinumGameRepository platinumGameRepository) {
        this.platGameRepository = platinumGameRepository;
    }

    public Page<PlatinumGame> findWishListGameBySQ(Pageable pageable, PlatinumGame platGame, Long userPk){
        if(platGame.getName() == null) platGame.setName("");
        return platGameRepository.findWishListByPrice(pageable, userPk, platGame);
    }

    public Page<PlatinumGame> findBySearchQuery(Pageable pageable, PlatinumGame platGame, String sort,
                                                PlatGamesFilter platGamesFilter){
        if(platGame.getName() == null) platGame.setName("");

        if(sort == null || sort.equals("creation"))
            return platGameRepository.findBySOCreationTime(pageable, platGame, platGamesFilter);
        if(sort.equals("points")) return platGameRepository.findBySOPoints(pageable, platGame, platGamesFilter);
        if(sort.equals("owners")) return platGameRepository.findBySOOwners(pageable, platGame, platGamesFilter);
        if(sort.equals("cr")) return platGameRepository.findBySOCompletionRate(pageable, platGame, platGamesFilter);
        if(sort.equals("plat")) return platGameRepository.findBySOPlatAchievers(pageable, platGame, platGamesFilter);
        if(sort.equals("tc")) return platGameRepository.findBySOTrophiesCount(pageable, platGame, platGamesFilter);
        if(sort.equals("fp")) return platGameRepository.findBySOPrice(pageable, platGame, platGamesFilter);
        if(sort.equals("gbp")) return platGameRepository.findBySOGBPrice(pageable, platGame, platGamesFilter);
        if(sort.equals("dis")) return platGameRepository.findBySODiscount(pageable, platGame, platGamesFilter);
        if(sort.equals("disper")) return platGameRepository.findBySODiscountPercent(pageable, platGame, platGamesFilter);
        if(sort.equals("maindur")) return platGameRepository.findBySOMainDuration(pageable, platGame, platGamesFilter);
        if(sort.equals("compdur")) return platGameRepository.findBySOCompletionistDuration(pageable, platGame, platGamesFilter);
        if(sort.equals("metascore")) return platGameRepository.findBySOMetaScore(pageable, platGame, platGamesFilter);
        if(sort.equals("userscore")) return platGameRepository.findBySOUserScore(pageable, platGame, platGamesFilter);
        return platGameRepository.findBySOOwners(pageable, platGame, platGamesFilter);
    }

    public PlatinumGame getPlatinumGame(String psnpId){
        Optional<PlatinumGame> gameOptional = platGameRepository.findById(psnpId);
        if(gameOptional.isEmpty()) return null;
        return gameOptional.get();
    }

    public Optional<PlatinumGame> getByPPID(String ppid){
        List<PlatinumGame> pgList = platGameRepository.filterByPPID(ppid);
        if(pgList != null && pgList.size() > 0) return Optional.ofNullable(pgList.get(0));
        return Optional.empty();
    }

    public Optional<PlatinumGame> initPlatinumGameWithName(PSNGamesService psnGamesService,
                                                   MetaCriticGamesService metaCriticGamesService, String name){
        try {
            PlatPricesGame platPricesGame = PlatPricesApi.getPlatPricesGameWithName(name,
                    AppSingleton.getInstance().networkConnection);
            return initPlatinumGame(psnGamesService, metaCriticGamesService, platPricesGame);
        }
        catch (Exception ex){
            return Optional.empty();
        }
    }

    public Optional<PlatinumGame> initPlatinumGameWithPPID(PSNGamesService psnGamesService,
                                                           MetaCriticGamesService metaCriticGamesService, String ppid){
        try {
            PlatPricesGame platPricesGame = PlatPricesApi.getPlatPricesGameWithPPID(ppid,
                    AppSingleton.getInstance().networkConnection);
            return initPlatinumGame(psnGamesService, metaCriticGamesService, platPricesGame);
        }
        catch (Exception ex){
            return Optional.empty();
        }
    }

    public Optional<PlatinumGame> initPlatinumGameWithPSNID(PSNGamesService psnGamesService,
                                                           MetaCriticGamesService metaCriticGamesService, String psnId){
        try {
            PlatPricesGame platPricesGame = PlatPricesApi.getPlatPricesGame(psnId,
                    AppSingleton.getInstance().networkConnection);
            return initPlatinumGame(psnGamesService, metaCriticGamesService, platPricesGame);
        }
        catch (Exception ex){
            return Optional.empty();
        }
    }

    public Optional<PlatinumGame> initPlatinumGame(PSNGamesService psnGamesService,
                                                   MetaCriticGamesService metaCriticGamesService,
                                                   PlatPricesGame platPricesGame){

        if(platPricesGame == null) return Optional.empty();
        String psnpUrl = platPricesGame.TrophyListURL;
        if(psnpUrl == null || psnpUrl.isEmpty()) return Optional.empty();

        Scrapper idScrapper = new Scrapper(psnpUrl);
        idScrapper.find("/trophies/", "-");
        if(!idScrapper.found) return Optional.empty();
        String psnpId = idScrapper.scrapped;
        String metacriticURL = platPricesGame.MetacriticURL;

        Optional<PlatinumGame> pgOptional = platGameRepository.findById(psnpId);
        if(pgOptional.isPresent()) return pgOptional;

        PlatinumGame platinumGame = new PlatinumGame();

        try {
            ir.sbpro.models.PSNGame psnGameFetcher = new ir.sbpro.models.PSNGame();
            psnGameFetcher.update(platPricesGame);
            PSNGame psnGame = new PSNGame();
            psnGame.load(psnGameFetcher);
            psnGamesService.gamesRepository.save(psnGame);

            MetaCriticGame metaCriticGame = null;
            if(metacriticURL != null && !metacriticURL.isEmpty()){
                metaCriticGame = metaCriticGamesService.save(metacriticURL);
            }

            PSNProfilesGame psnpFetcher = new PSNProfilesGame();
            psnpFetcher.update(psnpUrl,
                    AppSingleton.getInstance().networkConnection);
            platinumGame.load(psnpFetcher);
            platinumGame.setStoreGame(psnGame);
            if(metaCriticGame != null) platinumGame.setMetaCriticGame(metaCriticGame);
            platinumGame = platGameRepository.save(platinumGame);
            return Optional.of(platinumGame);
        }
        catch (Exception ex){
            return Optional.empty();
        }
    }
}
