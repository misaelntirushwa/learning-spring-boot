package com.ntm.learningspringboot.dao;

import com.ntm.learningspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class UserDataAccessService implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> selectAllUsers() {
        final String sql = "SELECT * FROM person";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            User.Gender gender = User.Gender.valueOf(resultSet.getString("gender"));
            return new User(
                    id,
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    gender,
                    resultSet.getInt("age"),
                    resultSet.getString("email")
            );
        });
    }

    @Override
    public Optional<User> selectUserByUserUid(UUID userUid) {
        final String sql = "SELECT * FROM person WHERE id = ?";

        User user = jdbcTemplate.queryForObject(sql, new Object[]{userUid}, (resultSet, i) -> {

            User.Gender gender = User.Gender.valueOf(resultSet.getString("gender"));
            return new User(
                    userUid,
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    gender,
                    resultSet.getInt("age"),
                    resultSet.getString("email")
            );
        });

        return Optional.ofNullable(user);
    }

    @Override
    public int updateUser(User user) {
        final String sql = "UPDATE person SET firstname = ?, lastname = ?, gender = ?, age = ?, email = ? WHERE id = ?";

        return jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(),
                user.getGender().toString(), user.getAge(), user.getEmail(), user.getUserUid());
    }

    @Override
    public int deleteUserByUserUid(UUID userUid) {
        final String sql = "DELETE FROM person WHERE id = ?";

        return jdbcTemplate.update(sql, userUid);
    }

    @Override
    public int insertUser(UUID userUid, User user) {
        final String sql = "INSERT INTO person (id, firstname, lastname, gender, age, email) VALUES (?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql, userUid, user.getFirstName(),
                user.getLastName(), user.getGender().toString(), user.getAge(), user.getEmail());

    }
}
