//package ru.practicum.shareit.request;
//
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.user.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class ItemRequestRepositoryImpl implements ItemRequestRepository {
//    List<ItemRequest> requests = new ArrayList<>();
//
//    @Override
//    public ItemRequest saveNewRequest(ItemRequest request) {
//        requests.add(request);
//        return request;
//    }
//
//    @Override
//    public ItemRequest getItemRequest(Integer id) {
//        return requests.get(id);
//    }
//
//    @Override
//    public void deleteItemRequest(Integer id) {
//        requests.remove(id);
//    }
//
//    private Integer getId() {
//        Integer lastId = requests.stream()
//                .mapToInt(ItemRequest::getId)
//                .max()
//                .orElse(0);
//        return lastId + 1;
//    }
//}
