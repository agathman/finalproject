package com.FinalProject.demo.Models;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepo extends CrudRepository<Users, String> {
    Users findByUserName(String userName);
}
