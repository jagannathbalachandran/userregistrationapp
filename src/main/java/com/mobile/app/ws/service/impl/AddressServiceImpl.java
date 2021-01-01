package com.mobile.app.ws.service.impl;

import com.mobile.app.ws.io.entity.AddressEntity;
import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.repository.AddressRepository;
import com.mobile.app.ws.repository.UserRepository;
import com.mobile.app.ws.service.AddressService;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.AddressDto;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.ui.model.response.AddressResponseModel;
import com.mobile.app.ws.utils.Util;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        List<AddressEntity>  addressEntities = addressRepository.findAllByUserDetails(userEntity);
        ModelMapper mapper = new ModelMapper();

        Type listAddressDTOs = new TypeToken<List<AddressDto>>() {}.getType();
        return mapper.map(addressEntities, listAddressDTOs);
    }
}
