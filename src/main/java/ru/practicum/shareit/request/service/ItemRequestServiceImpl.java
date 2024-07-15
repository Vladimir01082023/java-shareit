package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemRequest> getItemRequest(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundUserItemExceptions("User does not exist");
        }
        List<ItemResponseDto> itemResponseDtos = itemRepository.getItemResponsesByRequestorId(userId).stream()
                .map(ItemMapper::toResponseDto)
                .collect(Collectors.toList());

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorOrderByCreatedDesc(userId);
        for (ItemRequest itemRequest : itemRequests) {
            itemRequest.setItems(itemResponseDtos);
        }
        return itemRequests;
    }

    @Override
    public ItemRequest createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundUserItemExceptions("User does not exist");
        }
        return itemRequestRepository.save(ItemRequestMapper.fromDto(itemRequestDto, userId, LocalDateTime.now()));
    }

    @Override
    public List<ItemRequest> getAllRequests(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundUserItemExceptions("User does not exist");
        }
        List<ItemRequest> listOfItemRequests = itemRequestRepository.findAllByRequestorIsNot(userId,
                PageRequest.of(from, size));

        List<ItemResponseDto> listOfItemResponse = itemRepository.findByRequestIdIn(listOfItemRequests.stream()
                        .map(ItemRequest::getId).collect(Collectors.toList())).stream()
                .map(ItemMapper::toResponseDto)
                .collect(Collectors.toList());

        return listOfItemRequests.stream()
                .peek(itemRequest -> itemRequest.setItems(listOfItemResponse))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequest getItemRequestById(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundUserItemExceptions("User does not exist");
        }
        if (!itemRequestRepository.existsById(requestId)) {
            throw new NotFoundUserItemExceptions("ItemRequest does not exist");
        }
        ItemRequest itemRequest = itemRequestRepository.getItemRequestById(requestId);
        ;
        List<ItemResponseDto> listOfItemResponse = itemRepository.findByRequestIdIn(List.of(itemRequest.getId())).stream()
                .map(ItemMapper::toResponseDto)
                .collect(Collectors.toList());
        itemRequest.setItems(listOfItemResponse);
        return itemRequest;
    }
}
