package main.classes.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<User, Long> {
    User findByAppKey(String appKey);
    User findByToken(String token);
    List<User> findAll();
}
