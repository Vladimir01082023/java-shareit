package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchItem(String text, Long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long itemId, ItemDto item, long userId) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> addComment(Long userId, CommentDto commentDto, Long itemId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
