package com.crud.communicator.repository;

import com.crud.communicator.domain.AccountEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long>{

    @Override
    AccountEntity save(AccountEntity account);

    @Override
    List<AccountEntity> findAll();

    Optional<AccountEntity> findByLogin(final String login);

    @Query(nativeQuery = true)
    Optional<AccountEntity> retrieveAccount(@Param("LOGIN") String login, @Param("PASSWORD_TO_ACCOUNT") String password);

    @Query(nativeQuery = true)
    Optional<AccountEntity> retrieveAccountWithTheSameEmailOrLogin(@Param("LOGIN") String login, @Param("EMAIL") String email);

    void deleteByLogin(String login);
}
