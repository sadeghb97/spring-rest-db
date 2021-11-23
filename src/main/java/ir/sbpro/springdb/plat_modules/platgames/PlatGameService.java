package ir.sbpro.springdb.plat_modules.platgames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatGameService {
    public PlatinumGameRepository platGameRepository;

    @Autowired
    public PlatGameService(PlatinumGameRepository platinumGameRepository) {
        this.platGameRepository = platinumGameRepository;
    }

    public Page<PlatinumGame> findWishListGameBySQ(Pageable pageable, PlatinumGame platGame, Long userPk){
        if(platGame.getName() == null) platGame.setName("");
        return platGameRepository.findWishListByPrice(pageable, userPk, platGame);
    }

    public Page<PlatinumGame> findBySearchQuery(Pageable pageable, PlatinumGame platGame, String sort,
                                                PlatGamesFilter platGamesFilter){
        if(platGame.getName() == null) platGame.setName("");

        if(sort == null || sort.equals("creation"))
            return platGameRepository.findBySOCreationTime(pageable, platGame, platGamesFilter);
        if(sort.equals("points")) return platGameRepository.findBySOPoints(pageable, platGame, platGamesFilter);
        if(sort.equals("owners")) return platGameRepository.findBySOOwners(pageable, platGame, platGamesFilter);
        if(sort.equals("cr")) return platGameRepository.findBySOCompletionRate(pageable, platGame, platGamesFilter);
        if(sort.equals("plat")) return platGameRepository.findBySOPlatAchievers(pageable, platGame, platGamesFilter);
        if(sort.equals("tc")) return platGameRepository.findBySOTrophiesCount(pageable, platGame, platGamesFilter);
        if(sort.equals("fp")) return platGameRepository.findBySOPrice(pageable, platGame, platGamesFilter);
        if(sort.equals("gbp")) return platGameRepository.findBySOGBPrice(pageable, platGame, platGamesFilter);
        if(sort.equals("dis")) return platGameRepository.findBySODiscount(pageable, platGame, platGamesFilter);
        if(sort.equals("disper")) return platGameRepository.findBySODiscountPercent(pageable, platGame, platGamesFilter);
        if(sort.equals("maindur")) return platGameRepository.findBySOMainDuration(pageable, platGame, platGamesFilter);
        if(sort.equals("compdur")) return platGameRepository.findBySOCompletionistDuration(pageable, platGame, platGamesFilter);
        if(sort.equals("metascore")) return platGameRepository.findBySOMetaScore(pageable, platGame, platGamesFilter);
        if(sort.equals("userscore")) return platGameRepository.findBySOUserScore(pageable, platGame, platGamesFilter);
        return platGameRepository.findBySOOwners(pageable, platGame, platGamesFilter);
    }

    public PlatinumGame getPlatinumGame(String psnpId){
        Optional<PlatinumGame> gameOptional = platGameRepository.findById(psnpId);
        if(gameOptional.isEmpty()) return null;
        return gameOptional.get();
    }

    public Optional<PlatinumGame> getByPPID(String ppid){
        List<PlatinumGame> pgList = platGameRepository.filterByPPID(ppid);
        if(pgList != null && pgList.size() > 0) return Optional.ofNullable(pgList.get(0));
        return Optional.empty();
    }
}
