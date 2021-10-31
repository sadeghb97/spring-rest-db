package ir.sbpro.springdb.db_updater;

import ir.sbpro.SBProIO.StylishPrinter;
import ir.sbpro.springdb.ApplicationContextHolder;
import ir.sbpro.springdb.plat_modules.platgames.PlatGameService;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
import ir.sbpro.springdb.utils.TimeUtils;
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
}
