package com.ishwar.school.management.security.repository;

import com.ishwar.school.management.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
