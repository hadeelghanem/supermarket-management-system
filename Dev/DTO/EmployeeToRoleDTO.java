package DTO;

public record EmployeeToRoleDTO(
        int ID,
        boolean HR_MANAGER,
        boolean SHIFT_MANAGER,
        boolean CASHIER,
        boolean DELIVERY_MAN,
        boolean CLEANING,
        boolean WAREHOUSE_MAN,
        boolean ORDEYLY
) {
}
