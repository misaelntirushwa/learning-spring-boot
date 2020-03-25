package com.ntm.learningspringboot.service;

import com.ntm.learningspringboot.dao.UserDao;
import com.ntm.learningspringboot.model.User;
import com.ntm.learningspringboot.model.User.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(@Qualifier("postgres") UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers(Optional<String> gender) {
        List<User> users = userDao.selectAllUsers();

        if (!gender.isPresent()) {
            return users;
        }

        try {
            Gender theGender = Gender.valueOf(gender.get().toUpperCase());
            return users.stream()
                    .filter(user -> user.getGender().equals(theGender))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalStateException("Invalid gender", e);
        }

    }

    public Optional<User> getUser(UUID userUid) {

        Optional<User> userOptional = userDao.selectUserByUserUid(userUid);

        if (userOptional.isPresent()) {
            return userOptional;
        }

        return null;
    }

    public int updateUser(UUID userUid, User user) {
        Optional<User> optionalUser = getUser(userUid);

        if (optionalUser.isPresent()) {
            userDao.updateUser(user);
            return 1;
        }

        return -1;
    }

    public int removeUser(UUID userUid) {
        Optional<User> optionalUser = getUser(userUid);

        if (optionalUser.isPresent()) {
            userDao.deleteUserByUserUid(userUid);
            return 1;
        }

        return -1;
    }

    public int insertUser(User user) {
        UUID uuid = UUID.randomUUID();
        return userDao.insertUser(uuid, user);
    }
}
