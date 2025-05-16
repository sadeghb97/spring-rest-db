package ir.sbpro.springdb.db_updater;

import ir.sbpro.SBProIO.StylishPrinter;
import ir.sbpro.springdb.AppSingleton;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;

public class UpdateEntities {
    public static boolean updatePSNGame(PSNGamesService psnGamesService, PSNGame psnGame) {
        try {
            ir.sbpro.models.PSNGame psnFetcher = new ir.sbpro.models.PSNGame();
            psnFetcher.update(psnGame.getId(), AppSingleton.getInstance().networkConnection);
            psnGame.load(psnFetcher);
            psnGamesService.gamesRepository.save(psnGame);
            return true;
        }
        catch (Exception ex){
            StylishPrinter.print("PSNGUp Error: ", StylishPrinter.BOLD_RED);
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
