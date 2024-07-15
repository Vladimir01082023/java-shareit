package ru.practicum.shareit.items;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.DTO.BookingDTO;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemBookMapper;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
public class ItemServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemRequestService requestService;
    private final LocalDateTime currentTime = LocalDateTime.now();
    private UserDto user1 = new UserDto(1L, "user1", "user1@user1.com");
    private UserDto user2 = new UserDto(2L, "user2", "user2@user2.com");
    ItemDto item1Dto = new ItemDto(1L, "test1", "description", true, null, 2L);
    ItemDto item2Dto = new ItemDto(2L, "test1", "description", true, null, 1L);
    BookingDTO itemBooking1 = new BookingDTO(1L, 1L, 1L, currentTime, currentTime.plusDays(1), Status.WAITING);
    BookingDTO itemBooking2 = new BookingDTO(2L, 1L, 1L, currentTime, currentTime.plusDays(1), Status.WAITING);

    CommentDto comment1 = new CommentDto(1L, "comment", "user1", currentTime);
    ItemRequestDto request = new ItemRequestDto(1L, "description");


    @Test
    public void getItemsList_WithElements_WithNextAndLastBooking() throws InterruptedException {
        userService.saveUser(user1);
        userService.saveUser(user2);

        requestService.createItemRequest(request, 2L);

        itemService.addNewItem(1L, item1Dto);

        itemBooking1.setStart(LocalDateTime.now().plusSeconds(1));
        itemBooking1.setEnd(LocalDateTime.now().plusSeconds(2));
        itemBooking2.setStart(LocalDateTime.now().plusDays(1));
        itemBooking2.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.createBooking(2L, itemBooking1);
        bookingService.approveBooking(true, 1L);


        Thread.sleep(3000);

        itemService.addComment(1L, comment1, 2L);

        List<ItemBookDto> result = itemService.getItems(1L);

        assertThat(result.size(), is(1));

        ItemBookDto result1 = result.get(0);

        assertThat(result1.getId(), is(1L));
        assertThat(result1.getName(), is("test1"));
        assertThat(result1.getDescription(), is("description"));
        assertThat(result1.getAvailable(), is(true));
        assertThat(result1.getOwnerId(), is(1L));
        assertThat(result1.getLastBooking().getId(), is(1L));
        assertThat(result1.getComments().get(0).getId(), is(1L));

        Item item = ItemBookMapper.fromDto(result1);
        assertThat(item.getId(), is(1L));
        assertThat(item.getName(), is("test1"));
        assertThat(item.getDescription(), is("description"));
    }
}
