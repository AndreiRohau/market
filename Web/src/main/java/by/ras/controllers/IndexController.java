package by.ras.controllers;

import by.ras.entity.Occupation;
import by.ras.entity.particular.User;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.util.Date;


@Controller
public class IndexController {

    @ModelAttribute("user")
    public User userModel(){
        return new User();
    }
    @ModelAttribute("occupations")
    public Occupation[] occupationTypes(){
        return Occupation.values();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("date", new Date());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String test(User user, Model model){
        model.addAttribute("date", new Date());
        System.out.println(user);
        return "index";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "test";
    }
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test2(){
        return "test";
    }

    @RequestMapping(value = "/user/usertest", method = RequestMethod.GET)
    public String userTest(){
        return "user/usertest";
    }



}
