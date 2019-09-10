package com.crud.communicator.repository;

import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.domain.MessageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

    @Override
    MessageEntity save(MessageEntity account);

    @Override
    List<MessageEntity> findAll();

    @Query(nativeQuery = true)
    List<MessageEntity> retrieveMessagesFromSpecificUser(@Param("LOGIN") String sender);

    @Query(nativeQuery = true)
    List<MessageEntity> retrieveMessagesToSpecificUser(@Param("LOGIN") String recipient);

    List<MessageEntity> findBySenderOrRecipient(@Param("sender") AccountEntity sender, @Param("recipient") AccountEntity recipient);

    @Override
    void deleteById(final Long id);

    @Override
    Optional<MessageEntity> findById(final Long id);
}
