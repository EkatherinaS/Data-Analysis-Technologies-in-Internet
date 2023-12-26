package com.example.rentappartmentclient.model.database;

import jakarta.persistence.*;

@Entity
@Table(name = "\"Address\"")
public class Address {
    @Id
    @Column(name = "district", nullable = false)
    private String district;
    @Id
    @Column(name = "street", nullable = false)
    private String street;
    @Id
    @Column(name = "house", nullable = false)
    private String house;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "year")
    private Integer year;


    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDistrict() {
        return district;
    }
    public String getStreet() { return street; }
    public String getHouse() {
        return house;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public void setHouse(String house) {
        this.house = house;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}