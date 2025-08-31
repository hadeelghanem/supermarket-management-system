package DTO;

public record TransportDTO(
        int TransportId
        , String Date
        , String Departure_Time
        , String Source
        ,String Destination
        , int Weight
) {
}
