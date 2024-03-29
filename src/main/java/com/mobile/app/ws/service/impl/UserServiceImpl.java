package com.mobile.app.ws.service.impl;

import com.mobile.app.ws.exception.UserAlreadyExistsException;
import com.mobile.app.ws.exception.UserNotFoundException;
import com.mobile.app.ws.io.entity.AddressEntity;
import com.mobile.app.ws.io.entity.PasswordResetTokenEntity;
import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.repository.PasswordResetTokenRepository;
import com.mobile.app.ws.repository.UserRepository;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.AddressDto;
import com.mobile.app.ws.shared.dto.PasswordResetDetailsDto;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.utils.Util;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity userExists = userRepository.findByEmail(userDto.getEmail());
        if (userExists != null)
            throw new UserAlreadyExistsException("User already exists");

        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDto.setUserId(Util.generateRandomUserId());

        for (Iterator iterator = userDto.getAddresses().iterator(); iterator.hasNext(); ) {
            AddressDto addressDto = (AddressDto) iterator.next();
            addressDto.setAddressId(Util.generateRandomAddressId());
            addressDto.setUserDetails(userDto);
        }

        ModelMapper mapper = new ModelMapper();
        UserEntity userToBeSaved = mapper.map(userDto , UserEntity.class);

        userToBeSaved.setEmailVerificationToken(Util.generateEmailVerificationToken(userDto.getUserId()));
        userToBeSaved.setEmailVerificationStatus(false);

        UserEntity savedUser = userRepository.save(userToBeSaved);

        UserDto returnValue =  mapper.map(savedUser ,UserDto.class );
        return returnValue;
    }


    @Override
    public UserDto getUser(String userId) {

        UserEntity userExists = userRepository.findByUserId(userId);
        if(userExists == null) throw new RuntimeException("Record doesn't exist");

        ModelMapper mapper = new ModelMapper();
        UserDto returnValue = mapper.map(userExists , UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userExists = userRepository.findByUserId(userDto.getUserId());
        if (userExists == null) throw new UserNotFoundException("User doesn't exist");
        userExists.setFirstName(userDto.getFirstName());
        userExists.setLastName(userDto.getLastName());
        ModelMapper mapper = new ModelMapper();

        List<AddressEntity> addressEntityList = new LinkedList<>();
        for (Iterator iterator = userDto.getAddresses().iterator(); iterator.hasNext(); ) {
            AddressDto addressDto = (AddressDto) iterator.next();
            AddressEntity addressEntity = mapper.map(addressDto ,AddressEntity.class );
            addressEntityList.add(addressEntity);
        }
        userExists.setAddresses(addressEntityList);
        UserEntity save = userRepository.save(userExists);

        UserDto returnValue =  mapper.map(save ,UserDto.class );
        return returnValue;

    }

    @Override
    public UserDto getUserByEmailId(String email) {
        UserEntity userExists = userRepository.findByEmail(email);
        if(userExists == null) throw new UserNotFoundException("User does not exist");

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
    public List<UserDto> getAllUsers(int page, int limit) {

        List<UserDto> usersDto = new LinkedList<>();
        Pageable pageable = PageRequest.of(page , limit);
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        Iterable<UserEntity> users = userPage.getContent();

        for (UserEntity userEntity : users){
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity , userDto);
            usersDto.add(userDto);
        }
        return usersDto;
    }

    @Override
    public boolean verifyTokenHasExpired(String token) {
        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
        String userToken = userEntity.getEmailVerificationToken();
        boolean isExpired = Util.hasTokenExpired(token);
        if(!isExpired){
            userEntity.setEmailVerificationStatus(true);
            userEntity.setEmailVerificationToken(null);
            userRepository.save(userEntity);
            return false;
        }
        return true;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            return false;

        String token = Util.generatePasswordResetToken(userEntity.getUserId());
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

        System.out.println("Generated token " + token + " for user " + email);
        return true;
    }

    @Override
    public boolean passwordReset(PasswordResetDetailsDto passwordResetDetailsDto) {
        System.out.println("Password reset for " + passwordResetDetailsDto.getEmail());
        String actualToken = passwordResetDetailsDto.getToken();

        boolean isExpired = Util.hasTokenExpired(actualToken);
        if(isExpired) return false;

        UserEntity userEntity = userRepository.findByEmail(passwordResetDetailsDto.getEmail());

        if(userEntity == null)
            return false;

        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByUserDetails(userEntity);
        String expectedToken = passwordResetTokenEntity.getToken();
        if(!actualToken.equals(expectedToken)){
            return false;
        }

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(passwordResetDetailsDto.getPassword()));
        userRepository.save(userEntity);

        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail() , userEntity.getEncryptedPassword() , userEntity.getEmailVerificationStatus() , true , true  , true , new ArrayList<>());
    }
}
