package DTO;

public record ShiftDTO(
        int ShiftId,
        int storeId,
        String date,
        String ShiftType,
        String ShiftStatus
) {
}
