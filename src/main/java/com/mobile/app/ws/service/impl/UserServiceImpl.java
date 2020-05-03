package com.mobile.app.ws.service.impl;

import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.repository.UserRepository;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

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

        userToBeSaved.setUserId(generateRandomUserId());

        UserEntity savedUser = userRepository.save(userToBeSaved);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(savedUser , returnValue);
        return returnValue;
    }

    private String generateRandomUserId() {
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(1000);
        int rand_int2 = rand.nextInt(1000);

        String userId = String.valueOf(String.valueOf(rand_int1) + String.valueOf(rand_int2));
        return userId;
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
