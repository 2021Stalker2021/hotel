package com.hotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.AddressDto;
import com.hotel.dto.ArrivalTimeDto;
import com.hotel.dto.ContactsDto;
import com.hotel.dto.CreateHotelDto;
import com.hotel.entity.Hotel;
import com.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    private ObjectMapper objectMapper;

    private Hotel savedHotel;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        hotelRepository.deleteAll();

        hotelRepository.deleteAll();

        Hotel hotel = Hotel.builder()
                .name("DoubleTree by Hilton Minsk")
                .description("The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...")
                .brand("Hilton")
                .houseNumber(9)
                .street("Pobediteley Avenue")
                .city("Minsk")
                .country("Belarus")
                .postCode("220004")
                .phone("+375 17 309-80-00")
                .email("doubletreeminsk.info@hilton.com")
                .checkIn("14:00")
                .checkOut("12:00")
                .amenities(new java.util.HashSet<>(Set.of("Free WiFi", "Fitness center")))
                .build();

        savedHotel = hotelRepository.save(hotel);
    }

    @Test
    void getAllHotels_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$[0].address", containsString("9 Pobediteley Avenue")));
    }

    @Test
    void getHotelById_ShouldReturnDetails() throws Exception {
        mockMvc.perform(get("/hotels/" + savedHotel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$.address.city", is("Minsk")))
                .andExpect(jsonPath("$.contacts.phone", is("+375 17 309-80-00")))
                .andExpect(jsonPath("$.amenities", hasItem("Free WiFi")));
    }

    @Test
    void getHotelById_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/hotels/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createHotel_ShouldReturnShortDtoAndStatusCreated() throws Exception {
        CreateHotelDto createDto = CreateHotelDto.builder()
                .name("Minsk Hotel")
                .brand("Standard")
                .address(new AddressDto(1, "Main St", "Minsk", "Belarus", "220000"))
                .contacts(new ContactsDto("+375 11 111-11-11", "minsk@hotel.com"))
                .arrivalTime(new ArrivalTimeDto("12:00", "11:00"))
                .build();

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Minsk Hotel")));
    }

    @Test
    void searchHotels_ShouldFilterByCity() throws Exception {
        mockMvc.perform(get("/search").param("city", "minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("DoubleTree by Hilton Minsk")));
    }

    @Test
    void addAmenities_ShouldSaveData() throws Exception {
        List<String> newAmenities = List.of("Free parking", "Concierge");

        mockMvc.perform(post("/hotels/" + savedHotel.getId() + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAmenities)))
                .andExpect(status().isOk());
    }

    @Test
    void getHistogram_ShouldReturnData() throws Exception {
        mockMvc.perform(get("/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk", is(1)));
    }
}
