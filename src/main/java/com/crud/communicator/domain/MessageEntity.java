package com.crud.communicator.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "MessageEntity.retrieveMessagesFromSpecificUser",
                query = "SELECT * FROM MESSAGES " +
                        "JOIN ACCOUNTS ON MESSAGES.SENDER = ACCOUNTS.ID " +
                        "WHERE ACCOUNTS.LOGIN = :LOGIN",
                resultClass = MessageEntity.class),
        @NamedNativeQuery(
                name = "MessageEntity.retrieveMessagesToSpecificUser",
                query = "SELECT * FROM MESSAGES " +
                        "JOIN ACCOUNTS ON MESSAGES.RECIPIENT = ACCOUNTS.ID " +
                        "WHERE ACCOUNTS.LOGIN = :LOGIN",
                resultClass = MessageEntity.class)
})
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "MESSAGES")
public class MessageEntity {

    public MessageEntity(String message) {
        this.message = message;
        this.dateTheMessageWasSent = Timestamp.valueOf(LocalDateTime.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "DATE_OF_CREATION")
    private Timestamp dateTheMessageWasSent;

    @OneToOne(cascade = {CascadeType.REFRESH,  CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "SENDER")
    private AccountEntity sender;

    @OneToOne(cascade = {CascadeType.REFRESH,  CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "RECIPIENT")
    private AccountEntity recipient;
}
