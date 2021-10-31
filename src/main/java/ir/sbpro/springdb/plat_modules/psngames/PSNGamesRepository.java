package ir.sbpro.springdb.plat_modules.psngames;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PSNGamesRepository extends JpaRepository<PSNGame, String> {

    @Query("SELECT g FROM PSNGame g WHERE g.ppid = :#{#ppid}")
    Optional<PSNGame> findByPPID(String ppid);

}
