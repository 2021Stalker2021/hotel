package com.hotel.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
