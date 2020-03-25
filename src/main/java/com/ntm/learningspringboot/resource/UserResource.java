package com.ntm.learningspringboot.resource;

import com.ntm.learningspringboot.model.User;
import com.ntm.learningspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> fetchUsers(@QueryParam("gender") String gender) {

        return userService.getAllUsers(Optional.ofNullable(gender));
    }

    @GetMapping(path = "{userUid}")
    public ResponseEntity<?> fetchUser(@PathVariable("userUid") UUID userUid) {
        Optional<User> userOptional = userService.getUser(userUid);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage("user " + userUid + " was not found"));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {

        int result = userService.insertUser(user);

        return getIntegerResponseEntity(result);
    }

    @PutMapping(path = "{userUid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateUser(@PathVariable("userUid") UUID userUid, @RequestBody User user) {
        int result = userService.updateUser(userUid, user);

        return getIntegerResponseEntity(result);
    }

    @DeleteMapping(path = "{userUid}")
    public ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID userUid) {
        int result = userService.removeUser(userUid);

        return getIntegerResponseEntity(result);
    }

    private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
        if (result == 1) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
    class ErrorMessage {
        String message;

        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
