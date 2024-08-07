package ru.practicum.shareit.request.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    ItemRequest getItemRequestById(Long id);

    List<ItemRequest> findByRequestorOrderByCreatedDesc(Long userId);

    Page<ItemRequest> findByRequestorOrderByCreatedDesc(Long userId, Pageable pageable);

    List<ItemRequest> findAllByRequestorIsNot(Long userId, Pageable pageable);

}
