package com.hotel.controller;

import com.hotel.dto.CreateHotelDto;
import com.hotel.dto.HotelDetailsDto;
import com.hotel.dto.HotelShortDto;
import com.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Hotel API", description = "Управление отелями и получение статистики")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Получение списка всех отелей с их краткой информацией")
    public ResponseEntity<List<HotelShortDto>> getAllHotels() {
        List<HotelShortDto> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Получение расширенной информации по конкретному отелю")
    public ResponseEntity<HotelDetailsDto> getHotelById(@PathVariable Long id) {
        HotelDetailsDto hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск отелей по параметрам: name, brand, city, country, amenities")
    public ResponseEntity<List<HotelShortDto>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenity) {
        List<HotelShortDto> hotels = hotelService.searchHotels(name, brand, city, country, amenity);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping("/hotels")
    @Operation(summary = "Создание нового отеля")
    public ResponseEntity<HotelShortDto> createHotel(@Valid @RequestBody CreateHotelDto createHotelDto) {
        HotelShortDto createdHotel = hotelService.createHotel(createHotelDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Добавление списка amenities к отелю")
    public ResponseEntity<Void> addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Получение количества отелей, сгруппированных по указанному параметру")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        Map<String, Long> histogram = hotelService.getHistogram(param);
        return ResponseEntity.ok(histogram);
    }
}
