package org.ays.emergency_application.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Defines the priority levels for an emergency evacuation application.
 * <p>
 * Each priority is mapped to a numeric value to support persistence and integration scenarios
 * where priorities are represented as numbers.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum EmergencyEvacuationApplicationPriority {

    CRITICAL(6),
    HIGHEST(5),
    HIGH(4),
    MEDIUM(3),
    LOW(2),
    LOWEST(1);

    /**
     * Numeric representation of the priority.
     */
    private final int number;

    /**
     * Resolves a priority by its numeric value.
     *
     * @param number the numeric value to resolve
     * @return an {@link Optional} containing the matching priority, or empty if no match exists
     */
    public static Optional<EmergencyEvacuationApplicationPriority> valueOfNumber(final Integer number) {
        return Arrays.stream(EmergencyEvacuationApplicationPriority.values())
                .filter(priority -> priority.getNumber() == number)
                .findFirst();
    }

}
