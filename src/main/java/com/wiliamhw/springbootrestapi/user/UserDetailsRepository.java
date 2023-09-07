package com.wiliamhw.springbootrestapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    public List<UserDetails> findByRole(String role);
    public List<UserDetails> findByNameAndRole(String name, String role);
}
