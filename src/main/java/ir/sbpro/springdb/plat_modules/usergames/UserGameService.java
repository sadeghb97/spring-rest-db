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

    public Page<UserGame> findLibraryGames(Pageable pageable, PlatinumGame platGame, Long userPk, String sort){
        if(platGame.getName() == null) platGame.setName("");

        if(sort == null || sort.equals("owners")) return repository.findBySOOwners(pageable, userPk, platGame);
        if(sort.equals("points")) return repository.findBySOPoints(pageable, userPk, platGame);
        if(sort.equals("creation")) return repository.findBySOCreationTime(pageable, userPk, platGame);
        if(sort.equals("cr")) return repository.findBySOCompletionRate(pageable, userPk, platGame);
        if(sort.equals("plat")) return repository.findBySOPlatAchievers(pageable, userPk, platGame);
        if(sort.equals("tc")) return repository.findBySOTrophiesCount(pageable, userPk, platGame);
        if(sort.equals("fp")) return repository.findBySOPrice(pageable, userPk, platGame);
        if(sort.equals("rate")) return repository.findLibraryGamesByRate(pageable, userPk, platGame);
        if(sort.equals("status")) return repository.findLibraryGamesByStatus(pageable, userPk, platGame);
        if(sort.equals("maindur")) return repository.findBySOMainDuration(pageable, userPk, platGame);
        if(sort.equals("compdur")) return repository.findBySOCompletionistDuration(pageable, userPk, platGame);
        return repository.findBySOOwners(pageable, userPk, platGame);
    }
}
