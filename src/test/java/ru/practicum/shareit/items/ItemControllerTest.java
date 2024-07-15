package ru.practicum.shareit.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotFoundUserItemExceptions;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.conroller.ItemController;
import ru.practicum.shareit.item.dto.ItemBookDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mvc;

    private final ItemDto itemDto = new ItemDto(1L, "ItemName", "ItemDescription",
            true, null, null);

    private final ItemBookDto itemDtoBooking = new ItemBookDto(1L, "itemBook", "Items with bookings",
            true, 1L, null, null, new ArrayList<>());


    @Test
    public void findItemById_whenItemFound_thenStatusOkAndMockDtoEqualsResponseDto() throws Exception {
        when(itemService.getItemWithBooking(itemDto.getId(), 1L))
                .thenReturn(itemDtoBooking);
        when(itemService.getItemByIdWithBook(itemDto.getId()))
                .thenReturn(itemDtoBooking);
        when(itemRepository.existsById(itemDto.getId()))
                .thenReturn(true);
        when(itemService.getItemById(1L))
                .thenReturn(itemDto);

        mvc.perform(get("/items/{itemId}", itemDtoBooking.getId())
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoBooking.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoBooking.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoBooking.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoBooking.getAvailable())));
    }

    @Test
    public void addItem_whenCreateItem_thenStatusCreatedAndMockDtoEqualsResponseDto() throws Exception {
        when(itemService.addNewItem(1L, itemDto))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void addItem_whenCreateItemWitchIncorrectData_shouldThrowsException() throws Exception {
        ItemDto newItemDto = new ItemDto(1L, "", null,
                null, null, null);
        when(itemService.addNewItem(1L, newItemDto))
                .thenThrow(new NotFoundException("Item not available"));

        mvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(newItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(NotFoundException.class));
    }

    @Test
    public void updateItem_whenUpdateItem_thenStatusOkAndMockDtoEqualsResponseDto() throws Exception {
        ItemDto updateItemDto = new ItemDto(1L, "UpdateItemName", "ItemDescription",
                true, null, null);

        when(itemService.updateItem(1L, updateItemDto, 1L))
                .thenReturn(updateItemDto);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(updateItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updateItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updateItemDto.getName())))
                .andExpect(jsonPath("$.description", is(updateItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(updateItemDto.getAvailable())));
    }

    @Test
    public void updateItem_whenUpdateItemIfOwnerIdItIsNotOwner_thenStatusOkAndMockDtoEqualsResponseDto()
            throws Exception {
        when(itemService.updateItem(99L, itemDto, 99L))
                .thenThrow(new NotFoundUserItemExceptions("Id пользователя отличается от id владельца item"));

        mvc.perform(patch("/items/{itemId}", 99L)
                        .header(USER_ID_HEADER, 99L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(NotFoundUserItemExceptions.class));
    }

    @Test
    public void findAllItemsDtoForUser_whenInvoked_shouldReturnCollectionWitchItemsDto() throws Exception {
        when(itemService.getItems(1L))
                .thenReturn(List.of(itemDtoBooking));

        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoBooking.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoBooking.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoBooking.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoBooking.getAvailable())));
    }

    @Test
    public void searchItems_whenInvoked_shouldReturnCollectionWitchItemsDto() throws Exception {
        when(itemService.getItemByText("ItemName"))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "ItemName")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    public void searchItems_whenTextIsEmpty_shouldReturnNewArrayList() throws Exception {
        when(itemService.getItemByText("ItemName"))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param(" ", "ItemName")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void addComment_whenAddComment_thenStatusOkAndMockDtoEqualsResponseDto() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "text", "authorName", LocalDateTime.now());

        when(itemService.addComment(1L, commentDto, 1L))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }
}
