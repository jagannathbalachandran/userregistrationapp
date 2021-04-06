package com.mobile.app.ws.service.impl;

import com.mobile.app.ws.exception.UserAlreadyExistsException;
import com.mobile.app.ws.exception.UserNotFoundException;
import com.mobile.app.ws.io.entity.AddressEntity;
import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.repository.UserRepository;
import com.mobile.app.ws.shared.dto.AddressDto;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.utils.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

        Assertions.assertThrows(UserNotFoundException.class ,
                ()->{
                    UserDto userDto = userService.getUserByEmailId("jaggu@gmail.com");
                });
    }

    @Test
    void testCreateUser() {
        UserEntity userEntity = getUserEntity();
        UserDto userTobeSaved = getUserDto();

        when(userRepository.findByEmail("jagannathbalachandran@gmail.com")).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("password123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto createdUser = userService.createUser(userTobeSaved);

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals(userTobeSaved.getPassword() , createdUser.getPassword());
        Assertions.assertEquals(userTobeSaved.getEmail() , createdUser.getEmail());
        Assertions.assertEquals(userTobeSaved.getFirstName() , createdUser.getFirstName());
        Assertions.assertEquals(userTobeSaved.getLastName() , createdUser.getLastName());

        Assertions.assertEquals(userTobeSaved.getAddresses().size() , createdUser.getAddresses().size());
        Assertions.assertEquals(userTobeSaved.getAddresses().get(0).getCity() , createdUser.getAddresses().get(0).getCity());
        Assertions.assertEquals(userTobeSaved.getAddresses().get(0).getState() , createdUser.getAddresses().get(0).getState());
        Assertions.assertEquals(userTobeSaved.getAddresses().get(0).getCountry() , createdUser.getAddresses().get(0).getCountry());
        Assertions.assertEquals(userTobeSaved.getAddresses().get(0).getPostcode() , createdUser.getAddresses().get(0).getPostcode());

    }

    @Test
    void testCreateUserThrowsExceptionWhenUserAlreadyExists()
    {
        UserEntity userEntity = getUserEntity();
        UserDto userTobeSaved = getUserDto();

        when(userRepository.findByEmail("jagannathbalachandran@gmail.com")).thenReturn(userEntity);

        Assertions.assertThrows(UserAlreadyExistsException.class ,
                ()->{
                    UserDto createdUser = userService.createUser(userTobeSaved);
                }) ;
    }


    @Test
    void testUpdateUser() {
        UserEntity userEntity = getUserEntity();

        UserDto userTobeUpdated = new UserDto();
        userTobeUpdated.setEmail("jagannathbalachandran@gmail.com");
        userTobeUpdated.setFirstName("Jaggu");
        userTobeUpdated.setLastName("Bala");
        userTobeUpdated.setUserId("user123");
        List<AddressDto> addressDtoList = new ArrayList<>();
        AddressDto address1 = new AddressDto();
        address1.setAddressId("address123");
        address1.setCity("Bangalore");
        address1.setCountry("India");
        address1.setPostcode("560062");
        address1.setState("KA");
        address1.setType("Shipping");

        addressDtoList.add(address1);
        userTobeUpdated.setAddresses(addressDtoList);


        when(userRepository.findByUserId("user123")).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto updatedUser = userService.updateUser(userTobeUpdated);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(updatedUser.getEmail() , userTobeUpdated.getEmail());
        Assertions.assertEquals(updatedUser.getFirstName() , userTobeUpdated.getFirstName());
        Assertions.assertEquals(updatedUser.getLastName() , userTobeUpdated.getLastName());

        Assertions.assertEquals(updatedUser.getAddresses().size() , userTobeUpdated.getAddresses().size());
        Assertions.assertEquals(updatedUser.getAddresses().get(0).getCity() , userTobeUpdated.getAddresses().get(0).getCity());
//        Assertions.assertEquals(updatedUser.getAddresses().get(0).getState() , userTobeUpdated.getAddresses().get(0).getState());
//        Assertions.assertEquals(updatedUser.getAddresses().get(0).getCountry() , userTobeUpdated.getAddresses().get(0).getCountry());
//        Assertions.assertEquals(updatedUser.getAddresses().get(0).getPostcode() , userTobeUpdated.getAddresses().get(0).getPostcode());
//        Assertions.assertEquals(updatedUser.getAddresses().get(0).getType() , userTobeUpdated.getAddresses().get(0).getType());

    }

    private UserDto getUserDto() {
        UserDto userTobeSaved = new UserDto();

        userTobeSaved.setEmail("jagannathbalachandran@gmail.com");
        userTobeSaved.setFirstName("Jagannath");
        userTobeSaved.setLastName("Balachandran");
        userTobeSaved.setEmail("jagannathbalachandran@gmail.com");
        userTobeSaved.setUserId("user123");
        userTobeSaved.setPassword("password123");
        userTobeSaved.setEncryptedPassword("password123");


        List<AddressDto> addressDtoList = new ArrayList<>();
        AddressDto address1 = new AddressDto();
        address1.setAddressId("address123");
        address1.setCity("NY");
        address1.setCountry("USA");
        address1.setPostcode("12345");
        address1.setState("NY");
        address1.setType("Shipping");

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
        userEntity.setEmailVerificationStatus(false);
        userEntity.setEmailVerificationToken("emailtoken123");
        userEntity.setAddresses(getListOfAddressEntities());
        return userEntity;
    }

    private List<AddressEntity> getListOfAddressEntities() {
        List<AddressEntity> addressEntities = new ArrayList<>();
        AddressEntity addressEntity1 = new AddressEntity();
        addressEntity1.setAddressId("address123");
        addressEntity1.setCity("NY");
        addressEntity1.setCountry("USA");
        addressEntity1.setPostcode("12345");
        addressEntity1.setState("NY");
        addressEntity1.setId(1L);
        addressEntity1.setType("Shipping");
        addressEntities.add(addressEntity1);

        return addressEntities;
    }
}
