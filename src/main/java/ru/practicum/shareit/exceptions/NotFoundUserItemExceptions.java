package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class NotFoundUserItemExceptions extends NoSuchElementException {
    public NotFoundUserItemExceptions(String message) {super(message);}
}
