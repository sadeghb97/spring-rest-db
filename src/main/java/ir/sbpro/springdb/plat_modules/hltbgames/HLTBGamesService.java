package ir.sbpro.springdb.plat_modules.hltbgames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HLTBGamesService {
    public HLTBGamesRepository gamesRepository;

    @Autowired
    HLTBGamesService(HLTBGamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }
}
