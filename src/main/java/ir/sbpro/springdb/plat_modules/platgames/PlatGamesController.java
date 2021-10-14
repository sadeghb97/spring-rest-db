package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.springdb.configs.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_PREFIX + "/platgames")
public class PlatGamesController {
    PlatGameService platGameService;

    @Autowired
    public PlatGamesController(PlatGameService platGameService) {
        this.platGameService = platGameService;
    }
}
