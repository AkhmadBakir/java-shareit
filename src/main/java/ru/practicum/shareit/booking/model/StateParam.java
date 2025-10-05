package ru.practicum.shareit.booking.model;

public enum StateParam {

    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static StateParam getStateParam(String state) {
        try {
            return StateParam.valueOf(state.toUpperCase());
        } catch (RuntimeException e) {
            return StateParam.ALL;
        }

    }

}