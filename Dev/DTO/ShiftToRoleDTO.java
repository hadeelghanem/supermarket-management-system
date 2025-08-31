package DTO;

import javax.sound.sampled.BooleanControl;

public record ShiftToRoleDTO(
        int ShiftId,
        int StoreBranchId,
        Boolean SHIFT_MANAGER,
        Boolean CASHIER,
        Boolean DELIVERY_MAN,
        Boolean CLEANING,
        Boolean WAREHOUSE_MAN,
        Boolean ORDEYLY) {
}
