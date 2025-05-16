package ir.sbpro.springdb.plat_modules.metacritic_games;

import ir.sbpro.games_repo.MetacriticGamesRepository;
import ir.sbpro.springdb.AppSingleton;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MetaCriticGamesService {
    public MetaCriticGamesRepository gamesRepository;

    @Autowired
    MetaCriticGamesService(MetaCriticGamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }

    public MetaCriticGame save(String mcUrl){
        if(mcUrl != null && !mcUrl.isEmpty()) {
            try {
                ir.sbpro.models.MetacriticGame mcFetcher = new ir.sbpro.models.MetacriticGame();
                mcFetcher.update(mcUrl, AppSingleton.getInstance().networkConnection);
                MetaCriticGame metaCriticGame = new MetaCriticGame();
                metaCriticGame.load(mcFetcher);
                return gamesRepository.save(metaCriticGame);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    public MetaCriticGame update(String mcSlug){
        try {
            Optional<MetaCriticGame> mcOptional = gamesRepository.findById(mcSlug);
            if(mcOptional.isEmpty()) throw new Exception("MetaCritic Not Found");
            MetaCriticGame metaCriticGame = mcOptional.get();

            ir.sbpro.models.MetacriticGame mcFetcher = new ir.sbpro.models.MetacriticGame();
            mcFetcher.update(metaCriticGame.getLink(), AppSingleton.getInstance().networkConnection);

            metaCriticGame.load(mcFetcher);
            return gamesRepository.save(metaCriticGame);
        } catch (Exception ex) {
            return null;
        }
    }
}
