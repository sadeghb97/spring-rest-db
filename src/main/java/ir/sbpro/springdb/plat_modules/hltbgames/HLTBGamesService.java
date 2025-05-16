package ir.sbpro.springdb.plat_modules.hltbgames;

import ir.sbpro.springdb.AppSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HLTBGamesService {
    public HLTBGamesRepository gamesRepository;

    @Autowired
    HLTBGamesService(HLTBGamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }

    public HLTBGame save(String hltbUrl){
        if(hltbUrl != null && !hltbUrl.isEmpty()) {
            try {
                ir.sbpro.models.HLTBGame hltbGameFetcher = new ir.sbpro.models.HLTBGame();
                hltbGameFetcher.update(hltbUrl, AppSingleton.getInstance().networkConnection);
                HLTBGame hltbGame = new HLTBGame();
                hltbGame.load(hltbGameFetcher);
                return gamesRepository.save(hltbGame);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    public HLTBGame update(String hltbId){
        try {
            Optional<HLTBGame> hltbOptional = gamesRepository.findById(hltbId);
            if(hltbOptional.isEmpty()) throw new Exception("HL Not Found");
            HLTBGame hltbGame = hltbOptional.get();

            ir.sbpro.models.HLTBGame hltbGameFetcher = new ir.sbpro.models.HLTBGame();
            hltbGameFetcher.update(hltbGame.getFullLink(), AppSingleton.getInstance().networkConnection);

            hltbGame.load(hltbGameFetcher);
            return gamesRepository.save(hltbGame);
        } catch (Exception ex) {
            return null;
        }
    }
}
