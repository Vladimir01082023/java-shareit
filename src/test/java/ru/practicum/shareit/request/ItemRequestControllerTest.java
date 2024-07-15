package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    ItemRequestRepository itemRequestRepository;
    @MockBean
    ItemRepository itemRepository;

    @Autowired
    private MockMvc mvc;
    private LocalDateTime dateTime;
    private ItemRequestDto itemRequestDto;
    private ItemDto itemDto = new ItemDto(1L, "name", "description", true, 1L, 1L);
    private ItemRequestResponseDto itemRequestDtoResponse;
    private ItemRequest itemRequest;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void beforeEach() {
        Clock clock = Clock.fixed(Instant.parse("2024-05-30T10:15:30.00Z"), ZoneId.of("UTC"));
        dateTime = LocalDateTime.now(clock);

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setCreated(dateTime);
        itemRequest.setRequestor(1L);
        itemRequest.setItems(List.of(new ItemResponseDto(1L, "itemResponseDto", "description", 1L, true)));

        itemRequestDtoResponse = new ItemRequestResponseDto("description", dateTime,
                List.of(ItemMapper.toResponseDto(ItemMapper.fromDto(itemDto))));
    }

    @Test
    public void createNewItemRequest_whenCreated_shouldReturnStatusOkAndItemRequestDto() throws Exception {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestService.createItemRequest(any(), any()))
                .thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.requestor", is(itemRequest.getRequestor()), Long.class))
                .andExpect(jsonPath("$.created", is(dateTime.toString())));
    }

    @Test
    public void getItemRequests_whenInvoked_shouldReturnStatusOkAndCollectionItemRequestDto() throws Exception {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestService.getItemRequest(anyLong()))
                .thenReturn(List.of(itemRequest));


        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", is(dateTime.toString())))
                .andExpect(jsonPath("$[0].items", hasSize(1)));
    }

    @Test
    public void getAllItemRequestsWitchPagination_whenInvoked_shouldReturnStatusOkAndCollectionItemRequestDtoResponse()
            throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", is(dateTime.toString())))
                .andExpect(jsonPath("$[0].items", hasSize(1)));
    }

    @Test
    public void getInfoItemRequestById_whenInvoked_shouldReturnStatusOkAndItemRequestDtoResponse() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequest);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.created", is(dateTime.toString())))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }
}
