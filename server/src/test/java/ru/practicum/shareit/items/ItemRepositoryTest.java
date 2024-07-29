package ru.practicum.shareit.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRepositoryTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

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

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test2@test.com");
        user2.setName("test2");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("test2");
        item2.setDescription("descriptionTest");
        item2.setOwnerId(user1.getId());
        item2.setAvailable(true);

        entityManager.merge(user1);
        entityManager.merge(item1);

        entityManager.merge(user2);
        entityManager.merge(item2);
    }

    @Test
    public void findAllByUserIdTest() {
        Page<Item> result = itemRepository.findItemByOwnerId(1L, PAGEABLE);

        assertThat(result.getTotalElements(), is(2L));
        assertThat(result.getNumberOfElements(), is(2));
    }

    @Test
    public void findAllByContainsTextTest() {
        Page<Item> result = itemRepository.getItemByText("descriptionTest", PAGEABLE);

        assertThat(result.getTotalElements(), is(1L));
        assertThat(result.getNumberOfElements(), is(1));
    }

}
