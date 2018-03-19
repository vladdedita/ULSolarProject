package main.classes.controllers;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class FrontPageController {

    @RequestMapping("/")
    @CrossOrigin
    public String index(){

        return "index.html";
    }


}
