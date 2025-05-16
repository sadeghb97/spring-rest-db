package ir.sbpro.springdb.modules.games;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameModel, Long> {

    //JPQL defaulte Query hast - az NativeQuery ham mitavan estefade kard

    /*@Query("select g from games g where g.pk=:gameid")
    GameModel findByQuery(@Param("gameid") String gameId);*/

    /*@Query("select p from Posts p join p.categories pc where (:#{#posts.title} is null or " +
            "p.title like concat('%',:#{#posts.title},'%')) and " +
            "(coalesce(:#{#posts.categories},null) is null or " +
            "pc in (:#{#posts.categories})) " +
            "group by p.id having count (p.id) >= :num")
    Page<Posts> findBySearch(Posts posts, @Param("num") Long size, Pageable pageable);*/

    @Query("SELECT g FROM GameModel g WHERE g.name LIKE concat('%', :#{#gameModel.name}, '%') " +
            "ORDER BY g.createdAt DESC")
    Page<GameModel> findBySearchQuery(Pageable pageable, GameModel gameModel);
}
