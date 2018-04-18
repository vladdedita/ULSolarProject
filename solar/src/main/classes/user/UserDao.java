package main.classes.user;

import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {

    User findByAppKey(String appKey);
}
