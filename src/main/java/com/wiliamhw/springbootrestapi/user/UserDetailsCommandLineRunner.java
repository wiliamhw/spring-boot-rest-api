package com.wiliamhw.springbootrestapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {
    public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
        super();
        this.repository = repository;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserDetailsRepository repository;

    private void createIfNotExists(UserDetails userDetails) {
        List<UserDetails> dbUserDetails = repository.findByNameAndRole(userDetails.getName(), userDetails.getRole());
        if (!dbUserDetails.isEmpty()) return;
        repository.save(userDetails);
    }

    @Override
    public void run(String... args) throws Exception {
        createIfNotExists(new UserDetails("Ranga", "Admin"));
        createIfNotExists(new UserDetails("Ravi", "Admin"));
        createIfNotExists(new UserDetails("John", "User"));

        List<UserDetails> users = repository.findByRole("Admin");

        users.forEach(user -> logger.info(user.toString()));
    }
}
