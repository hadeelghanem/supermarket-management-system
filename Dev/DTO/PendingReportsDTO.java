package DTO;

public record PendingReportsDTO(
        String destination,
        int weight,
        String productsForDes,
        int ReportId
) {
}