package ir.sbpro.springdb.plat_modules.platgames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<PlatinumGame> findBySearchQuery(Pageable pageable, PlatinumGame platGame, String sort){
        if(platGame.getName() == null) platGame.setName("");

        if(sort == null || sort.equals("owners")) return platGameRepository.findBySOOwners(pageable, platGame);
        if(sort.equals("points")) return platGameRepository.findBySOPoints(pageable, platGame);
        if(sort.equals("cr")) return platGameRepository.findBySOCompletionRate(pageable, platGame);
        if(sort.equals("plat")) return platGameRepository.findBySOPlatAchievers(pageable, platGame);
        if(sort.equals("tc")) return platGameRepository.findBySOTrophiesCount(pageable, platGame);
        if(sort.equals("fp")) return platGameRepository.findBySOPrice(pageable, platGame);
        if(sort.equals("dis")) return platGameRepository.findBySODiscount(pageable, platGame);
        if(sort.equals("disper")) return platGameRepository.findBySODiscountPercent(pageable, platGame);
        if(sort.equals("maindur")) return platGameRepository.findBySOMainDuration(pageable, platGame);
        if(sort.equals("compdur")) return platGameRepository.findBySOCompletionistDuration(pageable, platGame);
        return platGameRepository.findBySOOwners(pageable, platGame);
    }

    public PlatinumGame getPlatinumGame(String psnpId){
        Optional<PlatinumGame> gameOptional = platGameRepository.findById(psnpId);
        if(gameOptional.isEmpty()) return null;
        return gameOptional.get();
    }
}
