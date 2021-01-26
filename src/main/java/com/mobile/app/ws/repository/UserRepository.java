package com.mobile.app.ws.repository;

import com.mobile.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity,Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String email);
    UserEntity findByEmailVerificationToken(String token);
}
