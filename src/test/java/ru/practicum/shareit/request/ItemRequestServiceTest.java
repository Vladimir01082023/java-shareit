package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestServiceIml;
    @Mock
    private UserRepository userRepository;

    private LocalDateTime dateTime;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private ItemDto itemDto = new ItemDto(1L, "name", "description", true, null, 1L);
    private ItemRequestResponseDto itemRequestDtoResponse;
    private Item item;
    private User user;

    @BeforeEach
    void beforeEach() {
        Clock clock = Clock.fixed(Instant.parse("2024-05-30T10:15:30.00Z"), ZoneId.of("UTC"));
        dateTime = LocalDateTime.now(clock);

        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@test.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setCreated(dateTime);
        itemRequest.setRequestor(user.getId());

        item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(user.getId());
        item.setRequestId(itemRequest.getId());

        itemRequestDto = new ItemRequestDto(1L, "description");

        itemRequestDtoResponse = new ItemRequestResponseDto("description", dateTime,
                List.of(ItemMapper.toResponseDto(item)));
    }

    @Test
    public void createItemRequestt_whenCreateNewItemRequest_shouldReturnItemRequestDto() {
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);

        ItemRequest saveItemRequestDto = itemRequestServiceIml.createItemRequest(itemRequestDto, 1L);

        assertItemRequest(itemRequest, saveItemRequestDto);

        ItemRequestDto newItemRequestDto = ItemRequestMapper.toDto(item);
        assertThat(newItemRequestDto.getDescription(), is(itemRequest.getDescription()));

        ItemRequestResponseDto itemRequestResponseDto = ItemRequestMapper.toItemRequestResponseDto(itemRequest,
                List.of(ItemMapper.toResponseDto(item)));
        assertThat(itemRequestResponseDto.getDescription(), is(itemRequestDto.getDescription()));
    }

    @Test
    public void createItemRequest_whenCreateNewItemRequestWitchIncorrectData_shouldThrowsException() {
        itemRequestDto = new ItemRequestDto(99L, "description");

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemRequestServiceIml.createItemRequest(itemRequestDto, 1L));

        assertThat(exception.getMessage(), is("User does not exist"));
    }

    @Test
    public void getAllRequests_whenInvoked_shouldReturnCollectionItemRequestDtoResponse() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito.lenient().when(itemRequestRepository.findAllByRequestorIsNot(any(), any()))
                .thenReturn(List.of(itemRequest));

        Mockito.lenient().when(itemRepository.findByRequestIdIn(List.of(anyLong())))
                .thenReturn(List.of(item));

        List<ItemRequest> itemRequests = itemRequestServiceIml.getAllRequests(1L, 0, 10);
        assertThat(itemRequests.size(), is(1));
        assertThat(itemRequests.get(0), is(itemRequest));
    }

    @Test
    public void getAllRequests_whenUserIsNotExists_shouldThrowsException() {
        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemRequestServiceIml.getAllRequests(1L, 1, 10));

        assertThat(exception.getMessage(), is("User does not exist"));
    }

    @Test
    public void getItemRequestById_whenFoundItemRequestDtoResponse_shouldReturnItemRequestDtoResponse() {

        Mockito.lenient().when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito.lenient().when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);
        Mockito.lenient().when(itemRequestRepository.getItemRequestById(anyLong()))
                .thenReturn(itemRequest);
        Mockito.lenient().when(itemRepository.findByRequestIdIn(List.of(anyLong())))
                .thenReturn(List.of(item));

        Mockito.lenient().when(itemRepository.getItemsByRequestId(anyLong()))
                .thenReturn(List.of(item));

        ItemRequest itemRequest1 = itemRequestServiceIml
                .getItemRequestById(1L, 1L);

        assertThat(itemRequest1.getId(), is(1L));
        assertThat(itemRequest1.getDescription(), is("description"));
        assertThat(itemRequest1.getCreated(), is(dateTime));
    }

    @Test
    public void getItemRequestById_whenItemRequestIsNotExisted_shouldThrowsException() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemRequestServiceIml
                        .getItemRequestById(1L, 1L));

        assertThat(exception.getMessage(), is("ItemRequest does not exist"));
    }

    @Test
    public void getItemRequestById_whenUserIsNotExisted_shouldThrowsException() {

        NotFoundUserItemExceptions exception = assertThrows(NotFoundUserItemExceptions.class, () ->
                itemRequestServiceIml
                        .getItemRequestById(1L, 1L));

        assertThat(exception.getMessage(), is("User does not exist"));
    }

    @Test
    public void getItemRequest_WhenUserIsExist_shouldReturnListOfItemRequestResponse() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);

        when(itemRepository.getItemResponsesByRequestorId(anyLong()))
                .thenReturn(List.of(item));
        when(itemRequestRepository.findByRequestorOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequest> itemRequestsList = itemRequestServiceIml.getItemRequest(1L);
        assertThat(itemRequestsList.size(), is(1));
        assertThat(itemRequestsList.get(0), is(itemRequest));
    }

    private void assertItemRequest(ItemRequest itemRequest, ItemRequest itemRequestDto) {
        assertThat(itemRequest.getId(), is(itemRequest.getId()));
        assertThat(itemRequest.getCreated(), is(itemRequest.getCreated()));
        assertThat(itemRequest.getDescription(), is(itemRequest.getDescription()));
        assertThat(itemRequest.getRequestor(), is(itemRequest.getRequestor()));
    }
}
