package com.mobile.app.ws.ui.controller;

import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.ui.model.request.UserDetailsModel;
import com.mobile.app.ws.ui.model.request.UserUpdateDetailsModel;
import com.mobile.app.ws.ui.model.response.UserResponseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}" , produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public UserResponseModel getUser(@PathVariable String id){
        System.out.println("Get by user id " + id.toString());
        UserResponseModel returnValue = new UserResponseModel();
        UserDto user = userService.getUser(id);

        BeanUtils.copyProperties(user ,returnValue );
        return returnValue;
    }

    @GetMapping
    public List<UserResponseModel> getUser(){
        System.out.println("Get all users");
        List<UserResponseModel> returnListOfUsers = new LinkedList<>();

        List<UserDto> allUsers = userService.getAllUsers();
        for (Iterator<UserDto> iterator = allUsers.iterator(); iterator.hasNext(); ) {
            UserDto userDto = iterator.next();
            UserResponseModel userResponseModel = new UserResponseModel();
            BeanUtils.copyProperties(userDto , userResponseModel);
            returnListOfUsers.add(userResponseModel);
        }
        return returnListOfUsers;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
    public UserResponseModel createUser(@RequestBody UserDetailsModel userDetailsModel){
        System.out.println("Created User with firstName " + userDetailsModel.getFirstName() + " and last name " + userDetailsModel.getLastName());
        UserResponseModel userResponse = new UserResponseModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsModel,userDto);
        userDto = userService.createUser(userDto);
        BeanUtils.copyProperties(userDto,userResponse);
        System.out.println("Created user  " + userDetailsModel.getFirstName() + " " + userDetailsModel.getLastName() + " with user id " + userDto.getUserId());
        return userResponse;
    }

    @PutMapping
    public UserResponseModel updateUser(@RequestBody UserUpdateDetailsModel updateUserDetails){

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(updateUserDetails,userDto);
        UserDto updateUser = userService.updateUser(userDto);
        UserResponseModel userResponseModel = new UserResponseModel();
        BeanUtils.copyProperties(updateUser , userResponseModel);
        System.out.println("Updated user with id " + updateUserDetails.getUserId());
        return userResponseModel;

    }

    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return "Deleted";
    }
}
