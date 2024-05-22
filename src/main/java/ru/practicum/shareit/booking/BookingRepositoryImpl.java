//package ru.practicum.shareit.booking;
//
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.Status;
//import ru.practicum.shareit.item.ItemRepositoryImpl;
//import ru.practicum.shareit.user.UserRepositoryImpl;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class BookingRepositoryImpl implements BookingRepository {
//
//    private final Map<Integer, Integer> bookings = new HashMap<>();
//    private ItemRepositoryImpl itemRepository;
//    private UserRepositoryImpl userRepository;
//
//    @Override
//    public Booking getBooking(Integer id) {
//        return null;
//    }
//
//    @Override
//    public void saveBooking(Integer itemID, Integer userId) {
//        Booking booking = new Booking(getId(), LocalDateTime.now(), LocalDateTime.now(), itemRepository.getItemById(itemID),
//                userRepository.getUserById(userId), Status.WAITING);
//        bookings.put(itemID, booking.getId());
//
//    }
//
//    @Override
//    public void deleteBooking(Booking booking) {
//
//    }
//
//    @Override
//    public List<Booking> getBookings() {
//        return List.of();
//    }
//
//    private Integer getId() {
//        List<Integer> bookingIDS = new ArrayList<>();
//        Integer lastID = 0;
//        for (Integer id : bookings.keySet()) {
//            bookingIDS.add(id);
//            if (bookingIDS.get(id) > lastID) {
//                lastID = bookingIDS.get(id);
//            }
//        }
//        return lastID;
//    }
//}
