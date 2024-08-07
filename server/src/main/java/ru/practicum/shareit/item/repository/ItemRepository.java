package ru.practicum.shareit.item.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;


import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemByOwnerIdOrderByIdAsc(Long ownerId);

    Page<Item> findItemByOwnerId(Long ownerId, Pageable pageable);

    @Query(value = "select * from ITEMS as it " +
            "where it.ITEM_AVAILABLE = TRUE " +
            "and " +
            "(it. ITEM_DESCRIPTION ilike %?1% or it.ITEM_NAME ilike %?1%)", nativeQuery = true)
    List<Item> getItemByText(String itemText);

    @Query(value = "select * from ITEMS as it " +
            "where it.ITEM_AVAILABLE = TRUE " +
            "and " +
            "(it. ITEM_DESCRIPTION ilike %?1% or it.ITEM_NAME ilike %?1%)", nativeQuery = true)
    Page<Item> getItemByText(String itemText, Pageable pageable);

    @Query(value = "select * from items as i where i.ITEM_REQUEST_ID IN" +
            "(select req.ITEM_REQUEST_ID from ITEM_REQUEST as req where req.REQUESTOR_ID = ?1)", nativeQuery = true)
    List<Item> getItemResponsesByRequestorId(Long userId);


    @Query(value = "select * from items where ITEM_REQUEST_ID = ?1", nativeQuery = true)
    List<Item> getItemsByRequestId(Long requestId);

    List<Item> findByRequestIdIn(List<Long> requestsId);

    @Query(value = "select * from items where ITEM_OWNER_ID = ?1", nativeQuery = true)
    Page<Item> findAllByUserId(Long userId, Pageable pageable);

}