package ir.sbpro.springdb.plat_modules.metacritic_games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaCriticGamesRepository extends JpaRepository<MetaCriticGame, String> {}
