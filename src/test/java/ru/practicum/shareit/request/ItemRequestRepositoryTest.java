package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;


import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void beforeEach() {
        Clock clock = Clock.fixed(Instant.parse("2024-05-30T10:15:30.00Z"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@test.com");
        user1.setName("test1");

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        itemRequest1.setDescription("description");
        itemRequest1.setCreated(dateTime);
        itemRequest1.setRequestor(user1.getId());

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@test.com");
        user2.setName("test2");

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(2L);
        itemRequest2.setDescription("description");
        itemRequest2.setCreated(dateTime);
        itemRequest2.setRequestor(user2.getId());

        entityManager.merge(user1);
        entityManager.merge(itemRequest1);

        entityManager.merge(user2);
        entityManager.merge(itemRequest2);
    }

    @Test
    public void findAllItemRequestsByUserIdTest() {
        List<ItemRequest> result = itemRequestRepository.findByRequestorOrderByCreatedDesc(1L);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getId(), is(1L));
    }

    @Test
    public void getAllItemRequestsWitchPaginationTest() {
        Page<ItemRequest> result = itemRequestRepository.findByRequestorOrderByCreatedDesc(1L, PAGEABLE);

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.stream().findFirst().get().getId(), is(1L));
    }
}
