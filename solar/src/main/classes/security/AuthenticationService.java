package main.classes.security;


import main.classes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {


    @Autowired
    UserService us;

    /***
     *
     * @param token - access token
     * @return true if authorized else false
     */
    public Boolean isAuthorized(String token) {
        return us.isAuthorized(token);
    }
}
