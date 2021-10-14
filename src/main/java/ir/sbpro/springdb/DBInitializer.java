package ir.sbpro.springdb;

import ir.sbpro.PSNProfilesScrapper;
import ir.sbpro.PSStoreScrapper;
import ir.sbpro.enums.ConnectionType;
import ir.sbpro.games_repo.HLTBGamesRepository;
import ir.sbpro.games_repo.HelpHLTBGamesMap;
import ir.sbpro.games_repo.PSStoreGamesRepository;
import ir.sbpro.models.PSNProfilesGame;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGamesService;
import ir.sbpro.springdb.plat_modules.platgames.PlatGameService;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class DBInitializer {
    PSNGamesService psnGamesService;
    HLTBGamesService hltbGamesService;
    PlatGameService platGameService;

    public ArrayList<PSNProfilesGame> psnpGames;
    public ArrayList<PlatinumGame> platinumGames;

    public DBInitializer(PSNGamesService psnGamesService, HLTBGamesService hltbGamesService,
                         PlatGameService platGameService) {
        this.psnGamesService = psnGamesService;
        this.hltbGamesService = hltbGamesService;
        this.platGameService = platGameService;
    }

    public void fetchRepository(){
        try {
            PSStoreGamesRepository psStoreGamesRepository =
                    new PSStoreGamesRepository(Path.of("raws/repo/psngames"));

            HLTBGamesRepository hltbGamesRepository = new HLTBGamesRepository(
                    Path.of("raws/repo/hltbgames"));

            HelpHLTBGamesMap helpHLTBGamesMap = new HelpHLTBGamesMap();

            psStoreGamesRepository.loadFromJsonFile();
            hltbGamesRepository.loadFromJsonFile();

            PSNProfilesScrapper psnProfilesScrapper = new PSNProfilesScrapper("raws/sources/psnp/");

            for(int i=1; 40>=i; i++) {
                psnProfilesScrapper.fetchGames(i);
            }

            psnProfilesScrapper.psnProfilesGames.forEach((psnpGame) -> {
                psnpGame.fetchPSNStoreGame(PSStoreScrapper.DownloaderType.ScrapperDefault,
                        ConnectionType.OFFLINE, psStoreGamesRepository);
            });

            psnProfilesScrapper.psnProfilesGames.forEach((psnpGame) -> {
                psnpGame.fetchHLTBGame(null, hltbGamesRepository, helpHLTBGamesMap);
            });

            this.psnpGames = psnProfilesScrapper.psnProfilesGames;
        }
        catch (Exception ex){
            System.out.println("EX: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void loadPlatinumGames(){
        platinumGames = new ArrayList<>();
        for(int i=0; psnpGames.size() > i; i++){
            PSNProfilesGame psnpGame = psnpGames.get(i);

            PSNGame springPsnGame = null;
            if(psnpGame.storeGame != null) {
                springPsnGame = new PSNGame();
                springPsnGame.setId(psnpGame.storeGame.id);
                springPsnGame.setName(psnpGame.storeGame.name);
                springPsnGame.setStoreClassificationType(psnpGame.storeGame.storeClassificationType);
                springPsnGame.setStoreClassificationDisplay(psnpGame.storeGame.storeClassificationDisplay);
                springPsnGame.setPlatforms(psnpGame.storeGame.platforms);
                springPsnGame.setImages(psnpGame.storeGame.images);
                springPsnGame.setVideos(psnpGame.storeGame.videos);
                springPsnGame.setBasePrice(psnpGame.storeGame.gamePrice.basePrice);
                springPsnGame.setDiscountedPrice(psnpGame.storeGame.gamePrice.discountedPrice);
                springPsnGame.setDiscountText(psnpGame.storeGame.gamePrice.discountText);
                springPsnGame.setDiscounted(psnpGame.storeGame.gamePrice.discounted);
                springPsnGame.setAlsoIncluded(psnpGame.storeGame.gamePrice.alsoIncluded);
            }

            HLTBGame springHLTBGame = null;
            if(psnpGame.hlGame != null) {
                springHLTBGame = new HLTBGame();
                springHLTBGame.setId(psnpGame.hlGame.id);
                springHLTBGame.setName(psnpGame.hlGame.name);
                springHLTBGame.setLink(psnpGame.hlGame.link);
                springHLTBGame.setImage(psnpGame.hlGame.image);
                springHLTBGame.setMainDuration(psnpGame.hlGame.mainDuration);
                springHLTBGame.setMainAndExtraDuration(psnpGame.hlGame.mainAndExtraDuration);
                springHLTBGame.setCompletionistDuration(psnpGame.hlGame.completionistDuration);
                springHLTBGame.setMainDurValue(psnpGame.hlGame.mainDurValue);
                springHLTBGame.setMainAndExtraDurValue(psnpGame.hlGame.mainAndExtraDurValue);
                springHLTBGame.setCompDurValue(psnpGame.hlGame.compDurValue);
            }

            PlatinumGame platinumGame = new PlatinumGame();
            platinumGame.setId(psnpGame.id);
            platinumGame.setLink(psnpGame.link);
            platinumGame.setImage(psnpGame.image);
            platinumGame.setName(psnpGame.name);
            platinumGame.setOwners(psnpGame.owners);
            platinumGame.setRecent(psnpGame.recent);
            platinumGame.setStore(psnpGame.store);
            platinumGame.setPlatform(psnpGame.platform);
            platinumGame.setPlatCount(psnpGame.platCount);
            platinumGame.setGoldCount(psnpGame.goldCount);
            platinumGame.setSilverCount(psnpGame.silverCount);
            platinumGame.setBronzeCount(psnpGame.bronzeCount);
            platinumGame.setAllTrophiesCount(psnpGame.allTrophiesCount);
            platinumGame.setPoints(psnpGame.points);
            platinumGame.setCompletionRate(psnpGame.completionRate);
            platinumGame.setStoreGame(springPsnGame);
            platinumGame.setHlGame(springHLTBGame);

            platinumGames.add(platinumGame);
        }
    }

    public int syncDB(){
        if (platinumGames.size() > 0) {
            int count = 0;

            for(int i=0; platinumGames.size()>i; i++) {
                PlatinumGame pg = platinumGames.get(i);
                if (pg.getStoreGame() != null)
                    psnGamesService.gamesRepository.save(pg.getStoreGame());
                if (pg.getHlGame() != null)
                    hltbGamesService.gamesRepository.save(pg.getHlGame());
                PlatinumGame pgOut = platGameService.platGameRepository.save(pg);
                if(pgOut.getId() != null) count++;
            }

            return count;
        }
        return 0;
    }
}
