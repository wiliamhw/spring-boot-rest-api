package com.wiliamhw.springbootrestapi.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserDetailsRestRepository extends PagingAndSortingRepository<UserDetails, Long> {
    public List<UserDetails> findByRole(String role);
    public List<UserDetails> findByNameAndRole(String name, String role);
}
