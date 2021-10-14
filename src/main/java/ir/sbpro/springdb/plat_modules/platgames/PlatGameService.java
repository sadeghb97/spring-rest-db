package ir.sbpro.springdb.plat_modules.platgames;

import ir.sbpro.springdb.modules.games.GameModel;
import ir.sbpro.springdb.modules.games.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlatGameService {
    public PlatinumGameRepository platGameRepository;

    @Autowired
    public PlatGameService(PlatinumGameRepository platinumGameRepository) {
        this.platGameRepository = platinumGameRepository;
    }

    public Page<PlatinumGame> findBySearchQuery(Pageable pageable, PlatinumGame platGame){
        if(platGame.getName() == null) platGame.setName("");
        return platGameRepository.findBySearchQuery(pageable, platGame);
    }
}
