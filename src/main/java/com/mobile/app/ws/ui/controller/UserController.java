package com.mobile.app.ws.ui.controller;

import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.ui.model.request.UserDetailsModel;
import com.mobile.app.ws.ui.model.response.UserResponseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}")
    public UserResponseModel getUser(@PathVariable String id){
        System.out.println("Get by user id " + id.toString());
        UserResponseModel returnValue = new UserResponseModel();
        UserDto user = userService.findUserByUserId(id);
        BeanUtils.copyProperties(user ,returnValue );
        return returnValue;
    }


//    @GetMapping(path = "/{id}")
//    public String getUser(@PathVariable String id){
//        System.out.println("Get by user id " + id);
//        return "Get by user id " + id;
//    }

    @PostMapping
    public UserResponseModel createUser(@RequestBody UserDetailsModel userDetailsModel){
        System.out.println("Created User with firstName " + userDetailsModel.getFirstName() + " and last name " + userDetailsModel.getLastName());
        UserResponseModel userResponse = new UserResponseModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsModel,userDto);
        userDto = userService.createUser(userDto);
        BeanUtils.copyProperties(userDto,userResponse);

        return userResponse;
    }

    @PutMapping
    public String updateUser(){
        return "Update User was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "Delete User was called";
    }
}
