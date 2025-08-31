package DTO;

import DomainLayer.Employee.BankAccount;

public record EmployeeDTO(
            int id,
            String name,
            int storeBranchId,
            String bankAccountName,
            int bankAccountNumber,
            Double salary,
            boolean isHRManager,

            String employeeTerms,
            int licenseNumber
) {}
