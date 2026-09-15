package com.hotel.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelShortDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
