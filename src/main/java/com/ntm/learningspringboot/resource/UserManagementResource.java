package com.ntm.learningspringboot.resource;

import com.ntm.learningspringboot.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("management/api/v1/users")
public class UserManagementResource {

    private static final List<User> USERS = Arrays.asList(
            new User(UUID.randomUUID(), "James", "Bond", User.Gender.MALE, 45, "james.bond@gmail.com"),
            new User(UUID.randomUUID(), "Maria", "Jones", User.Gender.FEMALE, 35, "maria.jones@gmail.com"),
            new User(UUID.randomUUID(), "Anna", "Smith", User.Gender.FEMALE, 22, "anna.smith@hotmail.com")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public List<User> getAllUsers() {
        return USERS;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void registerNewUser(@RequestBody User user) {
        System.out.println(user);
    }

    @DeleteMapping(path = "{userUid}")
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void deleteUser(@PathVariable("userUid") UUID userUid) {
        System.out.println(userUid);
    }

    @PutMapping(path = "{userUid}")
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void updateUser(@PathVariable("userUid") UUID userUid, @RequestBody User user) {
        System.out.println(String.format("%s %s", userUid, user));
    }
}
