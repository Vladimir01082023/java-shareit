package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class UserEmailUniqueException extends NoSuchElementException {
    public UserEmailUniqueException(String msg) {
        super(msg);
    }
}
