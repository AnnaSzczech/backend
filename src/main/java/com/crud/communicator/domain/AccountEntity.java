package com.crud.communicator.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@NamedNativeQueries({
        @NamedNativeQuery(name = "AccountEntity.retrieveAccount",
                query = "SELECT * FROM ACCOUNTS " +
                        "WHERE LOGIN = :LOGIN AND PASSWORD_TO_ACCOUNT = :PASSWORD_TO_ACCOUNT",
                resultClass = AccountEntity.class),
        @NamedNativeQuery(name = "AccountEntity.retrieveAccountWithTheSameEmailOrLogin",
                query = "SELECT * FROM ACCOUNTS " +
                        "JOIN USERS ON ACCOUNTS.USER = USERS.ID " +
                        "WHERE ACCOUNTS.LOGIN = :LOGIN OR USERS.EMAIL = :EMAIL",
                resultClass = AccountEntity.class),
        }
)
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "ACCOUNTS")
public class AccountEntity {

    public AccountEntity(String login, String password) {
        this.login = login;
        this.password = password;
        this.dateOfAccountCreation = Date.valueOf(LocalDate.now());
        this.online = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable=false)
    private Long id;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "PASSWORD_TO_ACCOUNT")
    private String password;

    @Column(name = "ONLINE")
    private boolean online;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_CREATION")
    private java.util.Date dateOfAccountCreation;

    @OneToOne(cascade =  CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER")
    private UserEntity user;
}