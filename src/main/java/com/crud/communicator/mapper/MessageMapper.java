package com.crud.communicator.mapper;

import com.crud.communicator.domain.MessageEntity;
import com.crud.communicator.domain.dto.MessageDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageMapper {

    public MessageEntity mapToMessage(MessageDto messageDto) {
        return new MessageEntity(messageDto.getMessage());
    }

    public  MessageDto mapToMessageDto(MessageEntity messageEntity) {
        return new MessageDto(messageEntity.getMessage(), messageEntity.getDateTheMessageWasSent().toString().substring(0, 16), messageEntity.getSender().getLogin(), messageEntity.getRecipient().getLogin());
    }
    public List<MessageDto> mapToMessageDtoList(List<MessageEntity> messageEntities) {
        List<MessageDto> messagesDto = new ArrayList<>();
        messageEntities.forEach(messageEntity -> messagesDto.add(mapToMessageDto(messageEntity)));
        return messagesDto;
    }
}
