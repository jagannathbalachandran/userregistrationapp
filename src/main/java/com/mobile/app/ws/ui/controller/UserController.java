package com.mobile.app.ws.ui.controller;

import com.mobile.app.ws.service.AddressService;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.AddressDto;
import com.mobile.app.ws.shared.dto.PasswordResetDetailsDto;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.ui.model.request.PasswordResetDetailsModel;
import com.mobile.app.ws.ui.model.request.PasswordResetRequestDetailsModel;
import com.mobile.app.ws.ui.model.request.UserDetailsModel;
import com.mobile.app.ws.ui.model.request.UserUpdateDetailsModel;
import com.mobile.app.ws.ui.model.response.AddressResponseModel;
import com.mobile.app.ws.ui.model.response.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @GetMapping(path = "/{id}" , produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public UserResponseModel getUser(@PathVariable String id){
        System.out.println("Get by user id " + id.toString());
        UserDto user = userService.getUser(id);
        ModelMapper mapper = new ModelMapper();
        UserResponseModel returnValue = mapper.map(user , UserResponseModel.class);
        return returnValue;
    }

    @GetMapping(path = "/{userId}/addresses" , produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public List<AddressResponseModel> getUserAddresses(@PathVariable String userId){
        System.out.println("Get by user id " + userId.toString());
        List<AddressDto> addressDtos = addressService.getAddresses(userId);

        ModelMapper mapper = new ModelMapper();

        Type listAddressResponseModel = new TypeToken<List<AddressResponseModel>>() {}.getType();
        return mapper.map(addressDtos, listAddressResponseModel);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}" , produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public AddressResponseModel getAddressForAUser(@PathVariable String userId ,@PathVariable String addressId ){
        System.out.println("Get by user id " + userId.toString());
        AddressDto addressDto = addressService.getAddressForAUser(userId , addressId);

        ModelMapper mapper = new ModelMapper();

       return mapper.map(addressDto , AddressResponseModel.class);
    }

    @GetMapping(path = "/email-verification" , produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public boolean verifyEmailAddress( @RequestParam(value = "token") String token ){
        System.out.println("Verify Token for user  " + token);
        return !userService.verifyTokenHasExpired(token);
    }
/*
    @GetMapping(path = "/{userId}/addresses" , produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public List<AddressResponseModel> getUserAddresses(@PathVariable String userId){
        System.out.println("Get by user id " + userId.toString());
        UserDto user = userService.getUser(userId);

        ModelMapper mapper = new ModelMapper();

        List<AddressResponseModel> addressResponseModels = new LinkedList<AddressResponseModel>();
        for (Iterator iterator = user.getAddresses().iterator(); iterator.hasNext(); ) {
            AddressDto addressDto = (AddressDto) iterator.next();
            addressResponseModels.add(mapper.map(addressDto , AddressResponseModel.class));
        }
        return addressResponseModels;
    }

*/


    @GetMapping
    public List<UserResponseModel> getAllUser(@RequestParam(value = "page" , defaultValue = "0") int page,
                                              @RequestParam(value = "limit" , defaultValue = "5") int limit){
        System.out.println("Get all users");
        List<UserResponseModel> returnListOfUsers = new LinkedList<>();
        ModelMapper mapper = new ModelMapper();

        List<UserDto> allUsers = userService.getAllUsers(page , limit);
        for (Iterator<UserDto> iterator = allUsers.iterator(); iterator.hasNext(); ) {
            UserDto userDto = iterator.next();
            UserResponseModel userResponseModel = mapper.map(userDto , UserResponseModel.class);
            returnListOfUsers.add(userResponseModel);
        }
        return returnListOfUsers;
    }

    @PostMapping(path = "/password-reset-request", produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public boolean requestResetPassword(@RequestBody PasswordResetRequestDetailsModel passwordResetRequestDetailsModel){
        System.out.println("Password reset request for  " + passwordResetRequestDetailsModel.getEmail() );
        return userService.requestPasswordReset(passwordResetRequestDetailsModel.getEmail());
    }

    @PutMapping(path = "/password-reset", produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public boolean passwordReset(@RequestBody PasswordResetDetailsModel passwordResetDetailsModel){

        ModelMapper mapper = new ModelMapper();

        PasswordResetDetailsDto passwordResetDetailsDto =  mapper.map(passwordResetDetailsModel , PasswordResetDetailsDto.class);
        return userService.passwordReset(passwordResetDetailsDto);
    }



    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public UserResponseModel createUser(@RequestBody UserDetailsModel userDetailsModel){
        System.out.println("Create User with firstName " + userDetailsModel.getFirstName() + " and last name " + userDetailsModel.getLastName());

        System.out.println("No of addresses is " + userDetailsModel.getAddresses().size());


        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetailsModel, UserDto.class);


        userDto = userService.createUser(userDto);
        UserResponseModel userResponse  = modelMapper.map(userDto, UserResponseModel.class);
        System.out.println("Created user  " + userResponse.getFirstName() + " " + userResponse.getLastName() + " with user id " + userResponse.getUserId());
        return userResponse;
    }

    @PutMapping
    public UserResponseModel updateUser(@RequestBody UserUpdateDetailsModel updateUserDetails){

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(updateUserDetails,userDto);
        UserDto updateUser = userService.updateUser(userDto);

        ModelMapper mapper = new ModelMapper();
        UserResponseModel userResponseModel = mapper.map(updateUser , UserResponseModel.class);

        System.out.println("Updated user with id " + userResponseModel.getUserId());
        return userResponseModel;

    }

    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return "Deleted user with id " + id;
    }
}
