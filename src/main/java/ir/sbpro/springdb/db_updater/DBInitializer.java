package ir.sbpro.springdb.db_updater;

import ir.sbpro.PSStoreScrapper;
import ir.sbpro.enums.ConnectionType;
import ir.sbpro.games_repo.*;
import ir.sbpro.models.PSNProfilesGame;
import ir.sbpro.springdb.ApplicationContextHolder;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGamesService;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGame;
import ir.sbpro.springdb.plat_modules.metacritic_games.MetaCriticGamesService;
import ir.sbpro.springdb.plat_modules.platgames.PlatGameService;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
import org.springframework.context.ApplicationContext;

import java.nio.file.Path;
import java.util.ArrayList;

public class DBInitializer {
    PSNGamesService psnGamesService;
    HLTBGamesService hltbGamesService;
    PlatGameService platGameService;
    MetaCriticGamesService metaCriticGamesService;

    public ArrayList<PSNProfilesGame> psnpGames;
    public ArrayList<PlatinumGame> platinumGames;

    public DBInitializer(){
        ApplicationContext applicationContext = ApplicationContextHolder.getContext();
        platGameService = applicationContext.getBean(PlatGameService.class);
        psnGamesService = applicationContext.getBean(PSNGamesService.class);
        hltbGamesService = applicationContext.getBean(HLTBGamesService.class);
        metaCriticGamesService = applicationContext.getBean(MetaCriticGamesService.class);
    }

    public void fetchRepository(){
        try {
            Path platGamesPath = Path.of("raws/repo/platgames");
            Path psnGamesPath = Path.of("raws/repo/psngames");
            Path psnGamesMapPath = Path.of("raws/repo/psngames-map");
            Path hltbGamesPath = Path.of("raws/repo/hltbgames");
            Path hltbGamesMapPath = Path.of("raws/repo/hltbgames-map");
            Path mcGamesPath = Path.of("raws/repo/mcgames");
            Path mcGamesMapPath = Path.of("raws/repo/mcgames-map");


            PSStoreGamesRepository psStoreGamesRepository =
                    new PSStoreGamesRepository(psnGamesPath, psnGamesMapPath);

            HLTBGamesRepository hltbGamesRepository =
                    new HLTBGamesRepository(hltbGamesPath, hltbGamesMapPath);

            MetacriticGamesRepository metacriticGamesRepository =
                    new MetacriticGamesRepository(mcGamesPath, mcGamesMapPath);

            HelpHLTBGamesMap helpHLTBGamesMap = new HelpHLTBGamesMap();

            psStoreGamesRepository.loadFromJsonFile();
            hltbGamesRepository.loadFromJsonFile();
            metacriticGamesRepository.loadFromJsonFile();

            PSNPGamesRepository psnpGamesRepository = new PSNPGamesRepository(platGamesPath);
            psnpGamesRepository.load();

            psnpGamesRepository.games.forEach((psnpGame) -> {
                psnpGame.fetchPSNStoreGame(PSStoreScrapper.DownloaderType.ScrapperDefault,
                        ConnectionType.OFFLINE, psStoreGamesRepository);
            });

            psnpGamesRepository.games.forEach((psnpGame) -> {
                psnpGame.fetchHLTBGame(null, hltbGamesRepository, helpHLTBGamesMap);
            });

            psnpGamesRepository.games.forEach((psnpGame) -> {
                psnpGame.fetchMetacriticGame(metacriticGamesRepository);
            });

            this.psnpGames = psnpGamesRepository.games;
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
                springPsnGame.load(psnpGame.storeGame);
            }

            HLTBGame springHLTBGame = null;
            if(psnpGame.hlGame != null) {
                springHLTBGame = new HLTBGame();
                springHLTBGame.load(psnpGame.hlGame);
            }

            MetaCriticGame springMCGame = null;
            if(psnpGame.metacriticGame != null){
                springMCGame = new MetaCriticGame();
                springMCGame.load(psnpGame.metacriticGame);
            }

            PlatinumGame platinumGame = new PlatinumGame();
            platinumGame.load(psnpGame, springHLTBGame, springPsnGame, springMCGame);
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
                if (pg.getMetacriticGame() != null)
                    metaCriticGamesService.gamesRepository.save(pg.getMetacriticGame());

                PlatinumGame pgOut = platGameService.platGameRepository.save(pg);
                if(pgOut.getId() != null) count++;

                if((count % 50) == 0){
                    System.out.println("Synced: " + count);
                }
            }

            System.out.println("Syncing Finished: " + count);
            return count;
        }
        return 0;
    }
}
