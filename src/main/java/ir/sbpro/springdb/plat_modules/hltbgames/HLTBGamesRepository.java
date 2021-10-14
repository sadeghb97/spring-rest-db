package ir.sbpro.springdb.plat_modules.hltbgames;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HLTBGamesRepository extends JpaRepository<HLTBGame, String> {

}
