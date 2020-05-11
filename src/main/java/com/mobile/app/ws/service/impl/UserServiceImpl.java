package com.mobile.app.ws.service.impl;

import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.repository.UserRepository;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.utils.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {

        UserEntity userExists = userRepository.findByEmail(user.getEmail());
        if (userExists != null) throw new RuntimeException("Record already exists");

        UserEntity userToBeSaved = new UserEntity();
        BeanUtils.copyProperties(user , userToBeSaved);

        userToBeSaved.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userToBeSaved.setUserId(Util.generateRandomUserId());

        UserEntity savedUser = userRepository.save(userToBeSaved);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(savedUser , returnValue);
        return returnValue;
    }



    @Override
    public UserDto getUser(String userId) {

        UserEntity userExists = userRepository.findByUserId(userId);
        if(userExists == null) throw new RuntimeException("Record doesn't exist");

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userExists , returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userExists = userRepository.findByUserId(userDto.getUserId());
        if (userExists == null) throw new RuntimeException("Record doesn't exist");
        userExists.setFirstName(userDto.getFirstName());
        userExists.setLastName(userDto.getLastName());
        userRepository.save(userExists);

        UserDto returnValue = new UserDto();

        BeanUtils.copyProperties(userExists ,returnValue);
        return returnValue;

    }

    @Override
    public UserDto getUserByEmailId(String email) {
        UserEntity userExists = userRepository.findByEmail(email);
        if(userExists == null) throw new RuntimeException("Record doesn't exist");

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userExists , returnValue);
        return returnValue;
    }

    @Override
    public void deleteUser(String id) {
        UserEntity existingUser = userRepository.findByUserId(id);
        if(existingUser == null) throw new RuntimeException("Record doesn't exist");
        userRepository.delete(existingUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
//        List<UserEntity> users = new LinkedList<>();
        List<UserDto> usersDto = new LinkedList<>();

        Iterable<UserEntity> userEntities = userRepository.findAll();
        for (Iterator iterator = userEntities.iterator(); iterator.hasNext(); ) {
                UserEntity userEntity = (UserEntity) iterator.next();
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(userEntity , userDto);
                usersDto.add(userDto);
        }
        return usersDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail() , userEntity.getEncryptedPassword() , new ArrayList<>());
    }
}
