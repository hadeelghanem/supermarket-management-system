package DTO;

public record EmployeeToAvailabilityDTO(
        int id,
        int dayOfWeek,
        boolean morningShift,
        boolean eveningShift
) { }
