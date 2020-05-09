package com.mobile.app.ws.service;

import com.mobile.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {

    public UserDto createUser(UserDto userDto);

    public UserDto getUser(String userId);

    public UserDto updateUser(UserDto userDto);

    UserDto getUserByEmailId(String email);
}
