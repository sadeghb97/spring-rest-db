package ir.sbpro.springdb.plat_modules.usergames;

import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserGameService {
    UserGameRepository repository;

    @Autowired
    UserGameService(UserGameRepository userGameRepository){
        this.repository = userGameRepository;
    }

    public UserGame save(UserGame userGame){
        return repository.save(userGame);
    }

    public Page<UserGame> findLibraryGames(Pageable pageable, PlatinumGame platGame, Long userPk){
        if(platGame.getName() == null) platGame.setName("");
        return repository.findLibraryGamesByRate(pageable, userPk, platGame);
    }
}
