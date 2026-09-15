package com.hotel.mapper;

import com.hotel.dto.CreateHotelDto;
import com.hotel.dto.HotelDetailsDto;
import com.hotel.dto.HotelShortDto;
import com.hotel.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface HotelMapper {

    @Mapping(target = "address.houseNumber", source = "houseNumber")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.country", source = "country")
    @Mapping(target = "address.postCode", source = "postCode")
    @Mapping(target = "contacts.phone", source = "phone")
    @Mapping(target = "contacts.email", source = "email")
    @Mapping(target = "arrivalTime.checkIn", source = "checkIn")
    @Mapping(target = "arrivalTime.checkOut", source = "checkOut")
    HotelDetailsDto toDetailsDto(Hotel hotel);

    @Mapping(target = "address", source = "hotel", qualifiedByName = "formatAddress")
    HotelShortDto toShortDto(Hotel hotel);

    List<HotelShortDto> toShortDtoList(List<Hotel> hotels);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "houseNumber", source = "address.houseNumber")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "postCode", source = "address.postCode")
    @Mapping(target = "phone", source = "contacts.phone")
    @Mapping(target = "email", source = "contacts.email")
    @Mapping(target = "checkIn", source = "arrivalTime.checkIn")
    @Mapping(target = "checkOut", source = "arrivalTime.checkOut")
    Hotel toEntity(CreateHotelDto createHotelDto);

    @Named("formatAddress")
    default String formatAddress(Hotel hotel) {
        if (hotel == null) {
            return null;
        }
        return String.format("%d %s, %s, %s, %s",
                hotel.getHouseNumber(),
                hotel.getStreet(),
                hotel.getCity(),
                hotel.getPostCode(),
                hotel.getCountry()
        );
    }
}
