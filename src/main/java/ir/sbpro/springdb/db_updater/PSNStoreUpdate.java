package ir.sbpro.springdb.db_updater;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ir.sbpro.PlatPricesApi;
import ir.sbpro.SBProIO.StylishPrinter;
import ir.sbpro.models.PlatPricesGame;
import ir.sbpro.springdb.ApplicationContextHolder;
import ir.sbpro.springdb.plat_modules.platgames.PlatGameService;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
import ir.sbpro.springdb.utils.TimeUtils;
import org.jsoup.Jsoup;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class PSNStoreUpdate {
    PSNGamesService psnGamesService;
    PlatGameService platGameService;

    public PSNStoreUpdate(){
        ApplicationContext applicationContext = ApplicationContextHolder.getContext();
        psnGamesService = applicationContext.getBean(PSNGamesService.class);
        platGameService = applicationContext.getBean(PlatGameService.class);
    }

    public void updateAll(int hoursTimeLimit){
        List<PSNGame> psnGames = psnGamesService.gamesRepository.findAll();

        int done = 0;
        for(PSNGame psnGame : psnGames){
            double sepHours = TimeUtils.getSepHours(psnGame.getUpTime(), System.currentTimeMillis());
            if(sepHours <= hoursTimeLimit) continue;

            boolean res = UpdateEntities.updatePSNGame(psnGamesService, psnGame);
            if(res) done++;
        }

        StylishPrinter.println("PSNStore Update Finished! (" + done + ")", StylishPrinter.BOLD_CYAN);
    }

    public void updatePlatStoreGames(int hoursTimeLimit){
        List<PlatinumGame> platGames = platGameService.platGameRepository.findAll();

        int done = 0;
        for(PlatinumGame platGame : platGames){
            if(platGame.getStoreGame() == null) continue;
            PSNGame psnGame = platGame.getStoreGame();

            double sepHours = TimeUtils.getSepHours(psnGame.getUpTime(), System.currentTimeMillis());
            if(sepHours <= hoursTimeLimit) continue;

            boolean res = UpdateEntities.updatePSNGame(psnGamesService, psnGame);
            if(res) done++;
        }

        StylishPrinter.println("PSNStore Update Finished! (" + done + ")", StylishPrinter.BOLD_CYAN);
    }

    public void updatePlatGamesTomanPrices(){
        List<PSNGame> psnGames = psnGamesService.gamesRepository.findAll();

        for(PSNGame psnGame : psnGames){
            psnGame.upTomanPrices();
            psnGamesService.gamesRepository.save(psnGame);
        }

        System.out.println("PlatGames Toman Prices Updated!");
    }

    public void initPlatGamesGBStore(){
        List<PlatinumGame> platGames = platGameService.platGameRepository.findAll();
        for(PlatinumGame platGame : platGames){
            PSNGame psnGame = platGame.getStoreGame();
            PSNGame gbGame = platGame.getGbStoreGame();

            if(gbGame != null || psnGame == null) continue;

            try {
                ir.sbpro.models.PSNGame psnFetcher = new ir.sbpro.models.PSNGame();
                PlatPricesGame platPricesGame =
                        PlatPricesApi.getPlatPricesGameWithName(psnGame.getName(), "GB");
                psnFetcher.update(platPricesGame);

                gbGame = new PSNGame();
                gbGame.load(psnFetcher);
                psnGamesService.gamesRepository.save(gbGame);

                platGame.setGbStoreGame(gbGame);
                platGameService.platGameRepository.save(platGame);
            }
            catch (Exception ex){
                System.out.println("FetchGBEX: " + ex.getMessage());
            }
        }
        System.out.println("Init GBStore Finished!");
    }
}
