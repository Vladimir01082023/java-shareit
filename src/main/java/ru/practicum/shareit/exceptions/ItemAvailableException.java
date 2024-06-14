package ru.practicum.shareit.exceptions;

import java.util.NoSuchElementException;

public class ItemAvailableException extends NoSuchElementException{
    public ItemAvailableException(String message){super(message);}
}
