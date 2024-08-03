package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.dto.BookingDtoFromClient;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private ItemServiceImpl itemService;
    @InjectMocks
    private BookingServiceImpl bookingService;


    private LocalDateTime dateTime;
    private ItemRequest itemRequest;
    private Item item1;
    private Item item2;
    private User user1;
    private User user2;
    private BookingDtoFromClient bookingDto;
    private Booking booking;
    private Booking bookingApproved;

    @BeforeEach
    void beforeEach() {
        Clock clock = Clock.fixed(Instant.parse("2024-07-02T10:15:30.00Z"), ZoneId.of("UTC"));
        dateTime = LocalDateTime.now(clock);

        user1 = new User();
        user1.setId(1L);
        user1.setName("test");
        user1.setEmail("test@test.com");

        user2 = new User();
        user2.setId(5L);
        user2.setName("test");
        user2.setEmail("test@test.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setCreated(dateTime);

        item1 = new Item();
        item1.setId(1L);
        item1.setName("name");
        item1.setDescription("description");
        item1.setAvailable(true);
        item1.setOwnerId(user2.getId());
        item1.setRequestId(itemRequest.getId());

        item2 = new Item();
        item2.setId(1L);
        item2.setName("name");
        item2.setDescription("description");
        item2.setAvailable(false);
        item2.setOwnerId(user2.getId());
        item2.setRequestId(itemRequest.getId());

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(3));
        booking.setItem(item1);
        booking.setStatus(Status.WAITING);
        booking.setBooker(user1);

        bookingApproved = new Booking();
        bookingApproved.setId(1L);
        bookingApproved.setStart(LocalDateTime.now().plusHours(1));
        bookingApproved.setEnd(LocalDateTime.now().plusHours(3));
        bookingApproved.setItem(item1);
        bookingApproved.setStatus(Status.APPROVED);
        bookingApproved.setBooker(new User());

        bookingDto = new BookingDtoFromClient(1L, 1L, 1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3), Status.WAITING);
    }

    @Test
    public void createBooking_whenCreatedNewBooking_shouldReturnBooking() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user1));

        when(itemService.getItemById(anyLong()))
                .thenReturn(ItemMapper.toDto(item1));

        when(bookingRepository.save(any()))
                .thenReturn(booking);

        Booking saveBooking = bookingService.createBooking(1L, bookingDto);

        assertBooking(booking, saveBooking);
    }

    @Test
    public void createBooking_whenCreatedNewBookingWitchIncorrectData_shouldThrowsException() {
        bookingDto = new BookingDtoFromClient(1L, 1L, null, null, null, null);

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                bookingService.createBooking(1L, bookingDto));

        assertThat(exception.getMessage(), is("User not found"));
    }

    @Test
    public void createBooking_whenCreatedNewBookingIfYouAreOwner_shouldThrowsException() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        when(itemService.getItemById(anyLong()))
                .thenReturn(ItemMapper.toDto(item1));

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                bookingService.createBooking(5L, bookingDto));

        assertThat(exception.getMessage(), is("Owner can not book his own item"));
    }

    @Test
    public void createBooking_whenCreatedNewBookingNonAvailable_shouldThrowsException() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        when(itemService.getItemById(anyLong()))
                .thenReturn(ItemMapper.toDto(item2));

        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                bookingService.createBooking(1L, bookingDto));

        assertThat(exception.getMessage(), is("Item is not available"));
    }

    @Test
    public void createBooking_whenCreatedNewBookingWithStartNull_shouldThrowsException() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        when(itemService.getItemById(anyLong()))
                .thenReturn(ItemMapper.toDto(item1));

        bookingDto.setStart(null);

        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                bookingService.createBooking(1L, bookingDto));

        assertThat(exception.getMessage(), is("End/start time is null"));
    }

    @Test
    public void createBooking_whenCreatedNewBookingWithEndAndStartEqual_shouldThrowsException() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        when(itemService.getItemById(anyLong()))
                .thenReturn(ItemMapper.toDto(item1));

        bookingDto.setStart(LocalDateTime.of(2024, 10, 10, 10, 10));
        bookingDto.setEnd(LocalDateTime.of(2024, 10, 10, 10, 10));

        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                bookingService.createBooking(1L, bookingDto));

        assertThat(exception.getMessage(), is("End time is before start time or they are equal"));
    }

    @Test
    public void createBooking_whenCreatedNewBookingWithStartBeforeEnd_shouldThrowsException() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        when(itemService.getItemById(anyLong()))
                .thenReturn(ItemMapper.toDto(item1));

        bookingDto.setStart(LocalDateTime.now().minusDays(1));

        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                bookingService.createBooking(1L, bookingDto));

        assertThat(exception.getMessage(), is("Start time is before current time"));
    }

    @Test
    public void approvedBooking_whenApprovedBooking_shouldReturnBookingWitchStatusApproved() {
        when(bookingRepository.getBookingById(anyLong()))
                .thenReturn(booking);

        when(bookingRepository.save(any()))
                .thenReturn(bookingApproved);


        Booking approvedBooking = bookingService.approveBooking(true, 1L);

        assertThat(approvedBooking.getId(), is(booking.getId()));
        assertThat(approvedBooking.getStatus(), is(Status.APPROVED));
        assertThat(approvedBooking.getId(), is(BookingMapper.toBookerDtoFromClient(booking).getId()));
    }

    @Test
    public void approvedBooking_whenApprovedBookingIfNotOwnerItem_shouldThrowsException() {

        when(bookingRepository.getBookingById(anyLong()))
                .thenReturn(booking);
        when(bookingRepository.getBookingById(anyLong()))
                .thenReturn(booking);

        when(bookingRepository.save(any()))
                .thenReturn(bookingApproved);


        Booking approvedBooking = bookingService.approveBooking(true, 1L);

        ItemAvailableException exception = assertThrows(ItemAvailableException.class, () ->
                bookingService.approveBooking(true, 1L));

        assertThat(exception.getMessage(), is("Booking is already approved"));
    }


    @Test
    public void getBookingById_whenNotEnoughRights_shouldReturnBookingInformation() {
        when(bookingRepository.getBookingById(anyLong()))
                .thenReturn(booking);

        Booking bookingInformation = bookingService.getBooking(1L);

        assertBooking(booking, bookingInformation);
        assertThat(booking.getStatus(), is(BookingMapper.toBookerDtoFromClient(booking).getStatus()));
    }

    @Test
    public void getBookingListForCurrentUser_whenInvokedWitchStateAll_shouldReturnBookingListForCurrentUser() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        when(bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForUser(null, 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForCurrentUser_whenInvokedWitchStateWaiting_shouldReturnListBookingWitchStatusWaiting() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setStatus(Status.WAITING);

        when(bookingRepository.findByBookerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForUser(Status.WAITING.toString(), 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
        assertThat(bookings.get(0).getId(), is(BookingMapper.toBookerDtoFromClient(booking).getId()));
    }

    @Test
    public void getBookingListForCurrentUser_whenInvokedWitchStateRejected_shouldReturnListBookingWitchStatusRejected() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setStatus(Status.REJECTED);

        when(bookingRepository.findByBookerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForUser(Status.REJECTED.toString(), 1L,
                0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForCurrentUser_whenInvokedWitchStatePast_shouldReturnListBookingWitchStatusPast() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setEnd(LocalDateTime.now().minusDays(1));

        when(bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForUser("PAST", 1L, 0, 10);

        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void getBookingListForCurrentUser_whenInvokedWitchStateFuture_shouldReturnListBookingWitchStatusFuture() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setStatus(Status.WAITING);

        when(bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        List<Booking> bookings = bookingService.getAllBookingsForUser("FUTURE", 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForCurrentUser_whenInvokedWitchStateCurrent_shouldReturnListBookingWitchStatusCurrent() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStart(LocalDateTime.now().minusDays(1));
        when(bookingRepository.getAllBookingsByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForUser("CURRENT", 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForOwner_whenInvokedWitchStateAll_shouldReturnBookingListForCurrentUser() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForOwner("ALL", 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForOwner_whenInvokedWitchStateWaiting_shouldReturnListBookingWitchStatusWaiting() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));
        booking.setStatus(Status.WAITING);

        when(bookingRepository.findBookingsByOwnerIdAndState(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForOwner("WAITING", 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForOwner_whenInvokedWitchStateRejected_shouldReturnListBookingWitchStatusRejected() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setStatus(Status.REJECTED);

        when(bookingRepository.findBookingsByOwnerIdAndState(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForOwner(Status.REJECTED.toString(), 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForOwner_whenInvokedWitchStatePast_shouldReturnListBookingWitchStatusPast() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setEnd(LocalDateTime.now().minusDays(1));

        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForOwner("PAST", 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForOwner_whenInvokedWitchStateFuture_shouldReturnListBookingWitchStatusFuture() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setStatus(Status.WAITING);

        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForOwner("FUTURE", 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    @Test
    public void getBookingListForOwner_whenInvokedWitchStateCurrent_shouldReturnListBookingWitchStatusCurrent() {
        when(userService.getUserById(anyLong()))
                .thenReturn(UserMapper.toUserDto(user2));

        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStart(LocalDateTime.now().minusDays(1));

        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<Booking> bookings = bookingService.getAllBookingsForOwner("CURRENT", 1L, 0, 10);

        assertBooking(booking, bookings.get(0));
    }

    private void assertBooking(Booking booking, Booking bookingFind) {
        assertThat(booking.getId(), is(bookingFind.getId()));
        assertThat(booking.getStart(), is(bookingFind.getStart()));
        assertThat(booking.getEnd(), is(bookingFind.getEnd()));
        assertThat(booking.getStatus(), is(bookingFind.getStatus()));
        assertThat(booking.getBooker(), is(bookingFind.getBooker()));
        assertThat(booking.getItem(), is(bookingFind.getItem()));
    }
}
