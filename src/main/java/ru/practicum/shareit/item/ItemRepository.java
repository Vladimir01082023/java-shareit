package ru.practicum.shareit.item;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemByOwnerId(Long ownerId);

    @Query(value = "select * from ITEMS as it " +
            "where it.ITEM_AVAILABLE = TRUE " +
            "and " +
            "(it. ITEM_DESCRIPTION ilike %?1% or it.ITEM_NAME ilike %?1%)", nativeQuery = true)
    List<Item> getItemByText(String itemText);


}