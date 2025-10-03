package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker(User booker);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(User booker,
                                                           LocalDateTime localDateTime1,
                                                           LocalDateTime localDateTime2);

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime localDateTime);

    List<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status = WAITING")
    List<Booking> getWaitingBookingsByBookerId(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status = REJECTED")
    List<Booking> getRejectedBookingsByBookerId(Long userId);


    List<Booking> findAllByItemOwner(User owner);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User user,
                                                              LocalDateTime localDateTime1,
                                                              LocalDateTime localDateTime2);

    List<Booking> findAllByItemOwnerAndEndBefore(User user, LocalDateTime localDateTime);

    List<Booking> findAllByItemOwnerAndStartAfter(User user, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.status = WAITING")
    List<Booking> getWaitingBookingsByItemOwner(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.status = REJECTED")
    List<Booking> getRejectedBookingsByItemOwner(Long userId);

    Optional<Booking> findFirstByBookerAndItemAndEndBeforeOrderByEndDesc(User author, Item item, LocalDateTime now);

    Optional<Booking> findFirstByItemAndStartAfterOrderByStartAsc(Item item, LocalDateTime now);

    Optional<Booking> findFirstByItemAndEndAfterOrderByEndDesc(Item item, LocalDateTime now);

}