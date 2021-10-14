package ir.sbpro.springdb.plat_modules.psngames;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PSNGamesRepository extends JpaRepository<PSNGame, String> {
}
