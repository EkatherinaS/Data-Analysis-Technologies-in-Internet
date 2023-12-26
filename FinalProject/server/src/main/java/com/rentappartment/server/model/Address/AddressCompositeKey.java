package com.rentappartment.server.model.Address;

import java.io.Serializable;

public class AddressCompositeKey implements Serializable {

    public AddressCompositeKey(String district, String street, String house) {
        this.district = district;
        this.street = street;
        this.house = house;
    }

    public AddressCompositeKey() {}

    private String district;
    private String street;
    private String house;
}
