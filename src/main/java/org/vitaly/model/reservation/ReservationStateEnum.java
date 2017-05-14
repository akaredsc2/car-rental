package org.vitaly.model.reservation;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum to hold one instance of each reservation state
 *
 * @see ReservationState
 */
public enum ReservationStateEnum {
    NEW(new NewState()),
    REJECTED(new RejectedState()),
    APPROVED(new ApprovedState()),
    CANCELED(new CanceledState()),
    ACTIVE(new ActiveState()),
    CLOSED(new ClosedState());

    private final ReservationState state;

    ReservationStateEnum(ReservationState state) {
        this.state = state;
    }

    /**
     * State held by enum entry
     *
     * @return state held by enum entry
     */
    public ReservationState getState() {
        return state;
    }

    /**
     * Returns reservation state of supplied name or empty optional if
     * no such state present in this enum
     *
     * @param stateString state to be parsed
     * @return reservation state of supplied name or empty optional if no such state present in this enum
     */
    public static Optional<ReservationState> stateOf(String stateString) {
        return Arrays.stream(ReservationStateEnum.values())
                .map(ReservationStateEnum::toString)
                .filter(string -> string.equalsIgnoreCase(stateString))
                .map(ReservationStateEnum::valueOf)
                .map(ReservationStateEnum::getState)
                .findFirst();
    }
}
