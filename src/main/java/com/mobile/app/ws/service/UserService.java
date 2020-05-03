package com.mobile.app.ws.service;

import com.mobile.app.ws.shared.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {

    public UserDto createUser(UserDto userDto);

    public UserDto findUserByUserId(String userId);

    public UserDto updateUser(UserDto userDto);
}
