package com.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateHotelDto {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String brand;

    @NotNull
    private AddressDto address;

    @NotNull
    private ContactsDto contacts;

    @NotNull
    private ArrivalTimeDto arrivalTime;
}
