package com.ntm.learningspringboot.service;

import com.ntm.learningspringboot.dao.UserDao;
import com.ntm.learningspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }

    Optional<User> getUser(UUID userUid) {
        return userDao.selectUserByUserUid(userUid);
    }

    int updateUser(User user) {
        Optional<User> optionalUser = getUser(user.getUserUid());

        if (optionalUser.isPresent()) {
            userDao.updateUser(user);
            return 1;
        }

        return -1;
    }

    int removeUser(UUID userUid) {
        Optional<User> optionalUser = getUser(userUid);

        if (optionalUser.isPresent()) {
            userDao.deleteUserByUserUid(userUid);
            return 1;
        }

        return -1;
    }

    int insertUser(User user) {
        UUID uuid = UUID.randomUUID();
        return userDao.insertUser(uuid, user);
    }
}
