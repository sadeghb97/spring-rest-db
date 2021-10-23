package ir.sbpro.springdb.plat_modules.psngames;

import ir.sbpro.springdb.plat_modules.hltbgames.HLTBGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PSNGamesService {
    public PSNGamesRepository gamesRepository;

    @Autowired
    PSNGamesService(PSNGamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }

    public PSNGame save(String psnId){
        if(psnId != null && !psnId.isEmpty()) {
            try {
                ir.sbpro.models.PSNGame psnGameFetcher = new ir.sbpro.models.PSNGame();
                psnGameFetcher.update(psnId);
                PSNGame psnGame = new PSNGame();
                psnGame.load(psnGameFetcher);
                return gamesRepository.save(psnGame);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    public PSNGame update(String psnId){
        try {
            Optional<PSNGame> psnOptional = gamesRepository.findById(psnId);
            if(psnOptional.isEmpty()) throw new Exception("HL Not Found");
            PSNGame psnGame = psnOptional.get();

            ir.sbpro.models.PSNGame psnGameFetcher = new ir.sbpro.models.PSNGame();
            psnGameFetcher.update(psnGame.getId());
            psnGame.load(psnGameFetcher);

            return gamesRepository.save(psnGame);
        } catch (Exception ex) {
            return null;
        }
    }
}
