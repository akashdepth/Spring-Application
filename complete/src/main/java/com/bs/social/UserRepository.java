package com.bs.social;

import com.bs.social.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findAll();
    User findById(Long id);
    User findByMobileNumber(String mobileNumber);
}