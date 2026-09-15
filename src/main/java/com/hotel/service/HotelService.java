package com.hotel.service;

import com.hotel.dto.CreateHotelDto;
import com.hotel.dto.HotelDetailsDto;
import com.hotel.dto.HotelShortDto;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<HotelShortDto> getAllHotels();
    HotelDetailsDto getHotelById(Long id);
    List<HotelShortDto> searchHotels(String name, String brand, String city, String country, String amenity);
    HotelShortDto createHotel(CreateHotelDto createHotelDto);
    void addAmenities(Long id, List<String> amenities);
    Map<String, Long> getHistogram(String param);
}
