-- liquibase formatted sql

-- changeset eduard:1
CREATE TABLE hotels (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description CLOB,
                        brand VARCHAR(255) NOT NULL,
                        house_number INT NOT NULL,
                        street VARCHAR(255) NOT NULL,
                        city VARCHAR(255) NOT NULL,
                        country VARCHAR(255) NOT NULL,
                        post_code VARCHAR(50) NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        check_in VARCHAR(10) NOT NULL,
                        check_out VARCHAR(10) NOT NULL
);

CREATE TABLE hotel_amenities (
                                 hotel_id BIGINT NOT NULL,
                                 amenity VARCHAR(255) NOT NULL,
                                 FOREIGN KEY (hotel_id) REFERENCES hotels(id) ON DELETE CASCADE,
                                 PRIMARY KEY (hotel_id, amenity)
);