package com.crud.communicator.controller;

import com.crud.communicator.controller.exceptions.AccountNotFoundException;
import com.crud.communicator.controller.exceptions.MessageNotFoundException;
import com.crud.communicator.domain.AccountEntity;
import com.crud.communicator.domain.MessageEntity;
import com.crud.communicator.domain.dto.MessageDto;
import com.crud.communicator.mapper.MessageMapper;
import com.crud.communicator.service.AccountDbService;
import com.crud.communicator.service.MessageDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/messages")
public class MessageController {

    @Autowired
    private MessageDbService messageDbService;

    @Autowired
    private AccountDbService accountDbService;

    @Autowired
    private MessageMapper messageMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void addMessage(@RequestBody MessageDto messageDto) {
        if (checkIfLoginExist(messageDto.getSenderLogin(), messageDto.getRecipientLogin())) {
            messageDbService.save(createMessageEntity(messageDto));
        } else {
            throw new AccountNotFoundException();
        }
    }

    @GetMapping(value = "/{sender}&{recipient}")
    public List<MessageDto> getConversation(@PathVariable String sender, @PathVariable String recipient){
        if (checkIfLoginExist(sender, recipient)) {
            List<MessageEntity> filterMessages = findConversation(sender, recipient);
            System.out.println(messageMapper.mapToMessageDtoList(filterMessages));
            return messageMapper.mapToMessageDtoList(filterMessages);
        }
        return new ArrayList<>();
    }

    @DeleteMapping(value = "deleteMessage/{id}")
    public void deleteMessage(@PathVariable Long id){
        if (!messageDbService.findMessage(id).isPresent()) {
            throw new MessageNotFoundException();
        }
        messageDbService.deleteById(id);
    }

    @GetMapping(value = "/getMessage/{id}")
    public MessageDto getMessage(@PathVariable Long id){
        if (!messageDbService.findMessage(id).isPresent()) {
            throw new MessageNotFoundException();
        }
        return messageMapper.mapToMessageDto(messageDbService.findMessage(id).get());
    }

    @PutMapping(value = "/{id}&{message}")
    public void editMessage(@PathVariable Long id, @PathVariable String message){
        if (!messageDbService.findMessage(id).isPresent()) {
            throw new MessageNotFoundException();
        }
        MessageEntity messageEntity = messageDbService.findMessage(id).get();
        messageEntity.setMessage(message);
        messageDbService.save(messageEntity);
    }

    @DeleteMapping(value = "/{sender}&{recipient}")
    public void deleteConversation(@PathVariable String sender, @PathVariable String recipient){
        if (checkIfLoginExist(sender, recipient)) {
            List<MessageEntity> messages = findConversation(sender, recipient);
            messages.forEach(messageEntity -> messageDbService.deleteById(messageEntity.getId()));
        }
    }

    private List<MessageEntity> findConversation(String sender, String recipient){
        AccountEntity senderEntity = findAccount(sender);
        List<MessageEntity> messages = messageDbService.findConversation(senderEntity, senderEntity);
        return messages.stream()
                .filter(messageEntity -> (messageEntity.getSender().getLogin().equals(recipient) || messageEntity.getRecipient().getLogin().equals(recipient)))
                .collect(Collectors.toList());
    }


    private MessageEntity createMessageEntity(MessageDto messageDto){
        MessageEntity messageEntity = messageMapper.mapToMessage(messageDto);
        messageEntity.setSender(findAccount(messageDto.getSenderLogin()));
        messageEntity.setRecipient(findAccount(messageDto.getRecipientLogin()));
        return messageEntity;
    }

    private boolean checkIfLoginExist(String senderLogin, String recipientLogin) {
        return (accountDbService.findAccountByLogin(senderLogin).isPresent() && accountDbService.findAccountByLogin(recipientLogin).isPresent());
    }

    private AccountEntity findAccount(String login) {
        return accountDbService.findAccountByLogin(login).get();
    }
}
