package com.mobile.app.ws.service;

import com.mobile.app.ws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
    public List<AddressDto> getAddresses(String userId);
}
