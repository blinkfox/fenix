package com.blinkfox.fenix.repository;

import com.blinkfox.fenix.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户库持久化类.
 *
 * @author blinkfox on 2019-08-09.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
