package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.UserAccount;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<UserAccount, UUID>, JpaSpecificationExecutor<UserAccount> {

    @Override
    @Query("select u from UserAccount u order by u.createdDate")
    List<UserAccount> findAll();
}
