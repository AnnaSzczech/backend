package com.crud.communicator.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Email or login is not unique")
public class NotUniqueEmailOrLoginException extends RuntimeException{
}
