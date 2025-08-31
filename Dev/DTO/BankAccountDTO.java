package DTO;

public record BankAccountDTO(
        String bankName,
        int branchNumber,
        int accountNumber,
        String accountOwnerName,

        int employeeId
) {
}
