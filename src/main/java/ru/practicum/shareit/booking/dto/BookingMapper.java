package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking toBooking(User booker, Item item, NewBookingRequest newBookingRequest) {
        return Booking.builder()
                .start(newBookingRequest.getStart())
                .end(newBookingRequest.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

}