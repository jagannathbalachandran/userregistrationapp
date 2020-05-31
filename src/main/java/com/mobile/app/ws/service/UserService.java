package com.mobile.app.ws.service;

import com.mobile.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    public UserDto createUser(UserDto userDto);

    public UserDto getUser(String userId);

    public UserDto updateUser(UserDto userDto);

    public UserDto getUserByEmailId(String email);

    public void deleteUser(String id);

    public List<UserDto> getAllUsers(int page, int limit);
}
