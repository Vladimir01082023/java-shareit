//package ru.practicum.shareit.booking;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.time.LocalDateTime;
//
///**
// * TODO Sprint add-bookings.
// */
//@RestController
//@RequestMapping(path = "/bookings")
//@Slf4j
//@RequiredArgsConstructor
//public class BookingController {
//    private final BookingService bookingService;
//
//    @PutMapping("/{itemID}/{userId}")
//    public void booking(@PathVariable Integer itemID, @PathVariable Integer userId) {
//        bookingService.createBooking(itemID, userId);
//    }
//
//}
