package main.classes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class AuthenticationController {

    @Autowired
    AuthenticationService as;

    @RequestMapping(value="/isAuth",method = RequestMethod.POST)
    ResponseEntity isAuthorized(@RequestHeader(value="Authorization") String token) throws IOException{
        if(as.isAuthorized(token))
        {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);

    }


}
