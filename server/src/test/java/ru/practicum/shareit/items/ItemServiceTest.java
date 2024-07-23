package ru.practicum.shareit.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;

import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;

    private Item item;
    private ItemDto itemDtoCreated = new ItemDto(1L, "TestName", "TestDescription", true, null, 1L);
    private User user1;
    private User user2;
    private Booking booking1;
    private Booking booking2;
    private Comment comment;
    private CommentDto commentDto;
    private LocalDateTime currentTime;

    @BeforeEach
    void beforeEach() {
        currentTime = LocalDateTime.now();

        user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@test.com");
        user1.setName("Name");

        user2 = new User();
        user2.setId(99L);
        user2.setEmail("test99@test.com");
        user2.setName("Name99");

        item = new Item();
        item.setId(1L);
        item.setName("TestName");
        item.setDescription("TestDescription");
        item.setAvailable(true);
        item.setOwnerId(user1.getId());

        booking1 = new Booking();
        booking1.setId(1L);
        booking1.setItem(item);
        booking1.setBooker(user1);
        booking1.setStatus(Status.APPROVED);
        booking1.setStart(currentTime.minusDays(1));
        booking1.setEnd(currentTime.minusHours(12));

        booking2 = new Booking();
        booking2.setId(2L);
        booking2.setItem(item);
        booking2.setBooker(user1);
        booking2.setStatus(Status.WAITING);
        booking2.setStart(currentTime.plusHours(5));
        booking2.setEnd(currentTime.plusDays(12));

        comment = new Comment();
        comment.setId(1L);
        comment.setCreated(currentTime);
        comment.setItem(item);
        comment.setAuthorName(user1);
        comment.setText("text comment");

        commentDto = new CommentDto(comment.getId(), comment.getText(), comment.getAuthorName().getName(),
                comment.getCreated());
    }

    @Test
    public void addNewItem_whenCreatedItem_shouldReturnedItemDto() {
        when(itemRepository.save(any()))
                .thenReturn(item);

        when(userRepository.existsById(1L)).thenReturn(true);

        ItemDto newItemDto = itemService.addNewItem(1L, itemDtoCreated);

        assertThat(item.getId(), equalTo(newItemDto.getId()));
    }

    @Test
    public void addNewItem_whenDescriptionNull_shouldThrowsException() {
        itemDtoCreated = new ItemDto(1L, "name", null,
                true, 1L, 1L);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.addNewItem(1L, itemDtoCreated));

        assertThat(exception.getMessage(), is("Description of item  can not null"));
    }

    @Test
    public void addNewItem_whenCreatedItemAvailableNull_shouldThrowsException() {
        itemDtoCreated = new ItemDto(1L, "name", "description",
                null, 1L, 1L);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.addNewItem(1L, itemDtoCreated));

        assertThat(exception.getMessage(), is("Availability can not be null"));
    }

    @Test
    public void addNewItem_whenCreatedItemWhenNameNull_shouldThrowsException() {
        itemDtoCreated = new ItemDto(1L, null, "description",
                true, 1L, 1L);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.addNewItem(1L, itemDtoCreated));

        assertThat(exception.getMessage(), is("Name of item can not be empty or null"));
    }

    @Test
    public void addNewItem_whenCreatedItemWhenUserIsNotFound_shouldThrowsException() {
        itemDtoCreated = new ItemDto(1L, "name", "description",
                true, 1L, 1L);

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemService.addNewItem(99L, itemDtoCreated));

        assertThat(exception.getMessage(), is("User not found"));
    }

    @Test
    public void updateItem_whenUpdatedItem_shouldReturnUpdatedItem() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(itemRepository.existsById(1L))
                .thenReturn(true);

        ItemDto updateItemDto = new ItemDto(1L, "UpdateTestName", "TestDescription",
                true, null, null);

        Item updateItem = new Item();
        updateItem.setId(updateItemDto.getId());
        updateItem.setName(updateItemDto.getName());
        updateItem.setDescription(updateItemDto.getDescription());
        updateItem.setAvailable(updateItemDto.getAvailable());
        updateItem.setOwnerId(user1.getId());

        when(itemRepository.save(updateItem))
                .thenReturn(updateItem);

        ItemDto itemDto = itemService.updateItem(1L, updateItemDto, 1L);

        assertEquals(updateItemDto.getName(), itemDto.getName());
        assertEquals(updateItemDto.getDescription(), itemDto.getDescription());
    }

    @Test
    public void updateItem_whenUpdatedItemIfUserNotOwner_shouldThrowsException() {
        Mockito.lenient().when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        Mockito.lenient().when(userService.getUserById(99L))
                .thenReturn(any());
        ItemDto updateItemDto = new ItemDto(1L, "UpdateTestName", "TestDescription",
                true, null, null);

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemService.updateItem(1L, updateItemDto, 99L));

        assertThat(exception.getMessage(), is("Item not found"));
    }

    @Test
    public void updateItem_whenUpdatedWHenIdOwnerisDifferentIdUser_shouldThrowsException() {
        Mockito.lenient().when(itemRepository.existsById(any(Long.class))).thenReturn(true);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        Mockito.lenient().when(userService.getUserById(99L))
                .thenReturn(any());
        ItemDto updateItemDto = new ItemDto(1L, "UpdateTestName", "TestDescription",
                true, null, 100L);

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemService.updateItem(99L, updateItemDto, 1L));

        assertThat(exception.getMessage(), is("Id пользователя отличается от id владельца item"));
    }

    @Test
    public void getItemById_whenFoundItem_shouldReturnItemDto() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(itemRepository.existsById(1L))
                .thenReturn(true);

        ItemDto itemDtoBooking = itemService.getItemById(1L);

        assertThat(item.getId(), is(itemDtoBooking.getId()));
        assertThat(item.getName(), is(itemDtoBooking.getName()));
        assertThat(item.getDescription(), is(itemDtoBooking.getDescription()));
        assertThat(item.getAvailable(), is(itemDtoBooking.getAvailable()));
    }

    @Test
    public void getItemById_whenCreatedItemWhenItemIsNotFound_shouldThrowsException() {

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemService.getItemById(99L));

        assertThat(exception.getMessage(), is("Item not found"));
    }

    @Test
    public void findAllItemsForUser_whenFoundItems_shouldReturnCollectionItemDtoBooking() {
        when(itemRepository.findItemByOwnerId(user1.getId()))
                .thenReturn(List.of(item));
        when(itemRepository.existsById(any()))
                .thenReturn(true);
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(any()))
                .thenReturn(List.of(comment));

        List<ItemBookDto> itemBookDto = itemService.getItems(user1.getId());

        assertThat(item.getId(), is(itemBookDto.get(0).getId()));
        assertThat(item.getName(), is(itemBookDto.get(0).getName()));
        assertThat(item.getDescription(), is(itemBookDto.get(0).getDescription()));
        assertThat(item.getAvailable(), is(itemBookDto.get(0).getAvailable()));
        assertThat(commentDto, is(itemBookDto.get(0).getComments().get(0)));
    }

    @Test
    public void getItemByText_WithItemIsNotExisted_shouldThrowsException() {
        when(itemRepository.getItemByText(any()))
                .thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemService.getItemByText("never"));

        assertThat(exception.getMessage(), is("Item not found"));
    }

    @Test
    public void getItemByText_whenFoundItems_shouldReturnCollectionItemDto() {
        when(itemRepository.getItemByText(any()))
                .thenReturn(List.of(item));

        List<ItemDto> itemDtos = itemService.getItemByText("текст");

        assertThat(item.getId(), is(itemDtos.get(0).getId()));
        assertThat(item.getName(), is(itemDtos.get(0).getName()));
        assertThat(item.getDescription(), is(itemDtos.get(0).getDescription()));
        assertThat(item.getAvailable(), is(itemDtos.get(0).getAvailable()));
    }

    @Test
    public void getItemWithBook_ShouldThrowsException() {
        when(itemRepository.existsById(anyLong()))
                .thenReturn(false);

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemService.getItemByIdWithBook(99L));
        assertThat(exception.getMessage(), is("Item not found"));
    }

    @Test
    public void getCommentDto_ShouldThrowsException() {
        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                itemService.addComment(1L, commentDto, 99L));
        assertThat(exception.getMessage(), is("User is never booked this item!"));
    }

    @Test
    public void getCommentDto_WhenTextIsNull_ShouldThrowsException() {
        when(bookingRepository.getAllBookingsByBookerId(anyLong()))
                .thenReturn(List.of(booking1));

        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                itemService.addComment(1L, new CommentDto(1L, "", "author", LocalDateTime.now()), 99L));
        assertThat(exception.getMessage(), is("Text of comment is empty!"));
    }

//    @Test
//    public void getCommentDto_WhenTextIsEmpty_ShouldThrowsException() {
//        when(itemRepository.existsById(anyLong()))
//                .thenReturn(true);
//        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
//                itemService.addComment(1L, new CommentDto(1L, null, "author", LocalDateTime.now()), 99L));
//        assertThat(exception.getMessage(), is("Text of comment is empty!"));
//    }

}
