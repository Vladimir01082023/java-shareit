package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;


import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingRepositoryTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void beforeEach() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@test.com");
        user1.setName("test1");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("test1");
        item1.setDescription("description");
        item1.setOwnerId(user1.getId());
        item1.setAvailable(true);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setBooker(user1);
        booking1.setItem(item1);
        booking1.setStart(LocalDateTime.now().minusDays(1));
        booking1.setEnd(LocalDateTime.now().minusHours(12));
        booking1.setStatus(Status.APPROVED);

        Booking booking4 = new Booking();
        booking4.setId(4L);
        booking4.setBooker(user1);
        booking4.setItem(item1);
        booking4.setStart(LocalDateTime.now().minusDays(1));
        booking4.setEnd(LocalDateTime.now().minusHours(12));
        booking4.setStatus(Status.CANCELED);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@test.com");
        user2.setName("test2");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("test2");
        item2.setDescription("descriptionTest");
        item2.setOwnerId(user2.getId());
        item2.setAvailable(true);

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setBooker(user2);
        booking2.setItem(item2);
        booking2.setStart(LocalDateTime.now().plusDays(1));
        booking2.setEnd(LocalDateTime.now().plusDays(2));
        booking2.setStatus(Status.WAITING);

        User user3 = new User();
        user3.setId(3L);
        user3.setEmail("test3@test.com");
        user3.setName("test3");

        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("test3");
        item3.setDescription("descriptionTest");
        item3.setOwnerId(user3.getId());
        item3.setAvailable(true);

        Booking booking3 = new Booking();
        booking3.setId(3L);
        booking3.setBooker(user3);
        booking3.setItem(item3);
        booking3.setStart(LocalDateTime.now().minusHours(1));
        booking3.setEnd(LocalDateTime.now().plusHours(3));
        booking3.setStatus(Status.APPROVED);

        entityManager.merge(user1);
        entityManager.merge(item1);
        entityManager.merge(booking1);

        entityManager.merge(user2);
        entityManager.merge(item2);
        entityManager.merge(booking2);

        entityManager.merge(user3);
        entityManager.merge(item3);
        entityManager.merge(booking3);
    }

    @Test
    public void findBookingsByOwnerIdAndStateTest() {
        Page<Booking> result = bookingRepository.findBookingsByOwnerIdAndState(1L,
                "APPROVED", PAGEABLE);

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.stream().findFirst().get().getId(), is(1L));
    }

    @Test
    public void findAllBookingByBookerIdTest() {
        List<Booking> result = bookingRepository.getAllBookingsByBookerId(1L);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getId(), is(1L));
    }

    @Test
    public void findAllPastBookingByIdTest() {
        Page<Booking> result = bookingRepository.findByBookerIdAndStatusEquals(1L, "CANCELED", PAGEABLE);

        assertThat(result.getTotalElements(), is(0L));
    }

    @Test
    public void findAllWaitingBookingByIdTest() {
        Page<Booking> result = bookingRepository.findByBookerIdAndStatusEquals(2L, "WAITING", PAGEABLE);

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.stream().findFirst().get().getId(), is(2L));
    }
}
