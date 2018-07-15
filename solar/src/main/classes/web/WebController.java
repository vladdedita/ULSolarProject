package main.classes.web;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@Controller
@Configuration
//@EnableWebMvc
@ComponentScan(basePackages="main.classes.security")
public class WebController extends WebMvcConfigurerAdapter {

    @RequestMapping("/")
    @CrossOrigin
    public String index() {
        return "index.html";
    }


}


//    @RequestMapping("/statistics")
//    @CrossOrigin
//    public String statistics() {
//
//        return "/statistics";    }


//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/{spring:\\w+}")
//                .setViewName("forward:/");
//        registry.addViewController("/**/{spring:\\w+}")
//                .setViewName("forward:/");
//        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css)$}")
//                .setViewName("forward:/");
//    }


