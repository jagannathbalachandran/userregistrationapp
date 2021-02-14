package com.mobile.app.ws.repository;

import com.mobile.app.ws.io.entity.AddressEntity;
import com.mobile.app.ws.io.entity.PasswordResetTokenEntity;
import com.mobile.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity findByUserDetails(UserEntity userEntity);
}
