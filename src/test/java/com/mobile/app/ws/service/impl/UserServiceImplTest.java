package com.mobile.app.ws.service.impl;

import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.repository.UserRepository;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.AddressDto;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.utils.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {


    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUser(){
        UserEntity userEntity = getUserEntity();

        when(userRepository.findByEmail("jagannathbalachandran@gmail.com")).thenReturn(userEntity);

        UserDto userDto = userService.getUserByEmailId("jagannathbalachandran@gmail.com");
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userEntity.getFirstName() , userDto.getFirstName());
        Assertions.assertEquals(userEntity.getLastName() , userDto.getLastName());
        Assertions.assertEquals(userEntity.getUserId() , userDto.getUserId());
    }


    @Test
    void testGetUserWhenUserNotFound() {
        UserEntity userEntity = getUserEntity();

        when(userRepository.findByEmail("jagannathbalachandran@gmail.com")).thenReturn(userEntity);

        Assertions.assertThrows(RuntimeException.class ,
                ()->{
                    UserDto userDto = userService.getUserByEmailId("jaggu@gmail.com");
                });
    }

    @Test
    void testCreateUser() {
        UserEntity userEntity = getUserEntity();

        when(userRepository.findByEmail("jagannathbalachandran@gmail.com")).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("password123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);


        UserDto userTobeSaved = getUserDto();

        UserDto createdUser = userService.createUser(userTobeSaved);

        Assertions.assertEquals(userTobeSaved.getPassword() , createdUser.getPassword());
        Assertions.assertEquals(userTobeSaved.getEmail() , createdUser.getEmail());

    }

    private UserDto getUserDto() {
        UserDto userTobeSaved = new UserDto();
        userTobeSaved.setEmail("jagannathbalachandran@gmail.com");
        userTobeSaved.setUserId("user123");
        userTobeSaved.setPassword("password123");
        List<AddressDto> addressDtoList = new ArrayList<>();
        AddressDto address1 = new AddressDto();
        address1.setAddressId("address123");
        address1.setCity("NY");
        address1.setCountry("USA");
        address1.setPostcode("12345");
        address1.setState("NY");
        addressDtoList.add(address1);
        userTobeSaved.setAddresses(addressDtoList);
        return userTobeSaved;
    }

    private UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Jagannath");
        userEntity.setLastName("Balachandran");
        userEntity.setUserId("user123");
        userEntity.setEncryptedPassword("password123");
        userEntity.setEmail("jagannathbalachandran@gmail.com");
        return userEntity;
    }
}
