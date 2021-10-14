package ir.sbpro.springdb.plat_modules;

import ir.sbpro.springdb.DBInitializer;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGamesService;
import ir.sbpro.springdb.plat_modules.platgames.PlatGameService;
import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import ir.sbpro.springdb.plat_modules.psngames.PSNGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("pggames/")
public class InitController {
    PSNGamesService psnGamesService;
    HLTBGamesService hltbGamesService;
    PlatGameService platGameService;

    @Autowired
    InitController(PSNGamesService psnGamesService, HLTBGamesService hltbGamesService, PlatGameService platGameService){
        this.psnGamesService = psnGamesService;
        this.hltbGamesService = hltbGamesService;
        this.platGameService = platGameService;
    }

    @GetMapping("init/")
    ResponseEntity<Object> initPlatGames() {
        DBInitializer dbInitializer = new DBInitializer(
                psnGamesService, hltbGamesService, platGameService);
        dbInitializer.fetchRepository();
        dbInitializer.loadPlatinumGames();
        int count = dbInitializer.syncDB();

        HashMap<String, Integer> out = new HashMap<>();
        out.put("count", count);
        return new ResponseEntity<Object>(out, HttpStatus.ACCEPTED);
    }
}
