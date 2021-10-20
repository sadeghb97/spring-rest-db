package ir.sbpro.springdb.modules.users;

import ir.sbpro.springdb.plat_modules.platgames.PlatinumGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    @Query("SELECT u FROM UserModel u WHERE u.username = :#{#username}")
    UserModel findByUsername(String username);

}
