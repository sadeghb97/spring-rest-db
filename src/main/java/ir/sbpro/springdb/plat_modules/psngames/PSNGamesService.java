package ir.sbpro.springdb.plat_modules.psngames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PSNGamesService {
    public PSNGamesRepository gamesRepository;

    @Autowired
    PSNGamesService(PSNGamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }
}
