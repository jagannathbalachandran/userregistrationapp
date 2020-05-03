package com.mobile.app.ws.service.impl;

import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.repository.UserRepository;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.utils.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto user) {

        UserEntity userExists = userRepository.findByEmail(user.getEmail());
        if (userExists != null) throw new RuntimeException("Record already exists");

        UserEntity userToBeSaved = new UserEntity();
        BeanUtils.copyProperties(user , userToBeSaved);
        userToBeSaved.setEncryptedPassword("test");

        userToBeSaved.setUserId(Util.generateRandomUserId());

        UserEntity savedUser = userRepository.save(userToBeSaved);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(savedUser , returnValue);
        return returnValue;
    }



    @Override
    public UserDto findUserByUserId(String userId) {

        UserEntity userExists = userRepository.findByUserId(userId);
        if(userExists == null) throw new RuntimeException("Record doesn't exist");

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userExists , returnValue);
        return returnValue;
    }
}
