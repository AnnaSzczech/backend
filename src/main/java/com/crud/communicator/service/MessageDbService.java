package com.crud.communicator.service;

import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.domain.MessageEntity;
import com.crud.communicator.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageDbService {

    @Autowired
    private MessageRepository messageRepository;

    public MessageEntity save(final MessageEntity account){
        return messageRepository.save(account);
    }

    public List<MessageEntity> findConversation(AccountEntity sender, AccountEntity recipient){
        return messageRepository.findBySenderOrRecipient(sender, recipient);
    }

    public void deleteMessages(String login){
        List<MessageEntity> messageEntities = new ArrayList<>();
        messageEntities.addAll(messageRepository.retrieveMessagesFromSpecificUser(login));
        messageEntities.addAll(messageRepository.retrieveMessagesToSpecificUser(login));
        System.out.println(messageEntities.size());
        messageEntities.forEach(messageEntity -> messageRepository.deleteById(messageEntity.getId()));
    }

    public void deleteById(final Long id){
        messageRepository.deleteById(id);
    }

    public Optional<MessageEntity> findMessage(final Long id){
        return messageRepository.findById(id);
    }
}
