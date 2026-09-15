package com.hotel.service;

import com.hotel.dto.CreateHotelDto;
import com.hotel.dto.HotelDetailsDto;
import com.hotel.dto.HotelShortDto;
import com.hotel.entity.Hotel;
import com.hotel.mapper.HotelMapper;
import com.hotel.repository.HotelRepository;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelShortDto> getAllHotels() {
        return hotelMapper.toShortDtoList(hotelRepository.findAll());
    }

    @Override
    public HotelDetailsDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));
        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    public List<HotelShortDto> searchHotels(String name, String brand, String city, String country, String amenity) {
        Specification<Hotel> spec = (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (name != null && !name.isBlank()) {
                predicate = cb.and(predicate, cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.trim().toLowerCase() + "%"
                ));
            }

            if (brand != null && !brand.isBlank()) {
                predicate = cb.and(predicate, cb.equal(
                        cb.lower(root.get("brand")),
                        brand.trim().toLowerCase()
                ));
            }

            if (city != null && !city.isBlank()) {
                predicate = cb.and(predicate, cb.equal(
                        cb.lower(root.get("city").as(String.class)),
                        city.trim().toLowerCase()
                ));
            }

            if (country != null && !country.isBlank()) {
                predicate = cb.and(predicate, cb.equal(
                        cb.lower(root.get("country").as(String.class)),
                        country.trim().toLowerCase()
                ));
            }

            if (amenity != null && !amenity.isBlank()) {
                Join<Hotel, String> amenitiesJoin = root.join("amenities");
                predicate = cb.and(predicate, cb.equal(
                        cb.lower(amenitiesJoin),
                        amenity.trim().toLowerCase()
                ));
            }

            return predicate;
        };

        List<Hotel> hotels = hotelRepository.findAll(spec);
        return hotelMapper.toShortDtoList(hotels);
    }

    @Override
    @Transactional
    public HotelShortDto createHotel(CreateHotelDto createHotelDto) {
        Hotel hotel = hotelMapper.toEntity(createHotelDto);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toShortDto(savedHotel);
    }

    @Override
    @Transactional
    public void addAmenities(Long id, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));
        hotel.getAmenities().addAll(amenities);
        hotelRepository.save(hotel);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        List<Hotel> allHotels = hotelRepository.findAll();

        return switch (param.toLowerCase()) {
            case "brand" -> allHotels.stream()
                    .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));
            case "city" -> allHotels.stream()
                    .collect(Collectors.groupingBy(Hotel::getCity, Collectors.counting()));
            case "country" -> allHotels.stream()
                    .collect(Collectors.groupingBy(Hotel::getCountry, Collectors.counting()));
            case "amenities" -> allHotels.stream()
                    .flatMap(hotel -> hotel.getAmenities().stream())
                    .collect(Collectors.groupingBy(amenity -> amenity, Collectors.counting()));
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid histogram parameter: " + param);
        };
    }
}
