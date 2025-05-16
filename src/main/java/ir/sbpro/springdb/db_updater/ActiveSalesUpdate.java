package ir.sbpro.springdb.db_updater;

import ir.sbpro.SBProIO.StylishPrinter;
import ir.sbpro.games_repo.StoreSalesRepository;
import ir.sbpro.models.ActiveSales;
import ir.sbpro.models.DLCSaleItem;
import ir.sbpro.models.GameSaleItem;
import ir.sbpro.models.StoreSale;
import ir.sbpro.springdb.AppSingleton;
import ir.sbpro.springdb.ApplicationContextHolder;
import ir.sbpro.springdb.plat_modules.active_sales.ActivePSNSale;
import ir.sbpro.springdb.plat_modules.active_sales.ActiveSalesService;
import ir.sbpro.springdb.plat_modules.platgames.PlatGameService;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
import ir.sbpro.springdb.plat_modules.sale_dlc_items.SaleDLCItem;
import ir.sbpro.springdb.plat_modules.sale_dlc_items.SaleDLCService;
import ir.sbpro.springdb.plat_modules.sale_game_items.SaleGameItem;
import ir.sbpro.springdb.plat_modules.sale_game_items.SaleGameService;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

public class ActiveSalesUpdate {
    ActiveSalesService activeSalesService;
    SaleGameService saleGameService;
    SaleDLCService saleDLCService;
    PlatGameService platGameService;
    PSNGamesService psnGamesService;

    public ActiveSalesUpdate(){
        ApplicationContext applicationContext = ApplicationContextHolder.getContext();
        activeSalesService = applicationContext.getBean(ActiveSalesService.class);
        saleGameService = applicationContext.getBean(SaleGameService.class);
        saleDLCService = applicationContext.getBean(SaleDLCService.class);
        platGameService = applicationContext.getBean(PlatGameService.class);
        psnGamesService = applicationContext.getBean(PSNGamesService.class);
    }

    public void update(){
        StoreSalesRepository salesFetcher = new StoreSalesRepository(AppSingleton.getInstance().networkConnection);
        try{
            salesFetcher.update();
            activeSalesService.activeSalesRepository.deleteAll();

            ArrayList<StoreSale> sales = salesFetcher.sales.sales;
            for(StoreSale sale : sales){
                ActivePSNSale springActiveSale = new ActivePSNSale();
                springActiveSale.load(sale);
                springActiveSale.setGames(new ArrayList<>());
                springActiveSale.setDlcList(new ArrayList<>());
                springActiveSale = activeSalesService.activeSalesRepository.save(springActiveSale);

                for(GameSaleItem gsi : sale.saleItems.game_discounts){
                    SaleGameItem springSaleGameItem = new SaleGameItem();
                    springSaleGameItem.load(gsi);
                    springSaleGameItem.setSale(springActiveSale);

                    Optional<PlatinumGame> pgOptional = platGameService.getByPPID(springSaleGameItem.getPPID());
                    if(pgOptional.isPresent()){
                        PlatinumGame platinumGame = pgOptional.get();
                        if(platinumGame.getStoreGame() != null){
                            PSNGame psnGame = platinumGame.getStoreGame();
                            psnGame.load(gsi);
                            psnGamesService.gamesRepository.save(psnGame);
                        }

                        springSaleGameItem.setPlatinumGame(platinumGame);
                    }

                    springSaleGameItem = saleGameService.saleGameRepository.save(springSaleGameItem);
                    springActiveSale.getGames().add(springSaleGameItem);
                }

                for(DLCSaleItem dsi : sale.saleItems.dlc_discounts){
                    SaleDLCItem springSaleDLCItem = new SaleDLCItem();
                    springSaleDLCItem.load(dsi);
                    springSaleDLCItem.setSale(springActiveSale);
                    springSaleDLCItem = saleDLCService.saleDLCRepository.save(springSaleDLCItem);
                    springActiveSale.getDlcList().add(springSaleDLCItem);
                }

                activeSalesService.activeSalesRepository.save(springActiveSale);
                System.out.print("Active Sales Updated! (");
                StylishPrinter.print(springActiveSale.getSaleName() + " - " + springActiveSale.getID(),
                        StylishPrinter.BOLD_CYAN);
                System.out.println(")");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
