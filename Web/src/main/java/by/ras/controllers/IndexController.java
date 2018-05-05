package by.ras.controllers;

import by.ras.UserService;
import by.ras.entity.Occupation;
import by.ras.entity.particular.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Controller
public class IndexController {
    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User userModel(){
        return new User();
    }
    @ModelAttribute("occupations")
    public Occupation[] occupationTypes(){
        return Occupation.values();
    }

    //method to login logout checks
    @GetMapping(value = "/")
    public String indexLoginLogout(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("date", new Date());
        return "index";
    }

    @GetMapping(value = "/market/products")
    public String goToProducts(){
        return "market/products";
    }

    @GetMapping(value = "/market/admin/admintest")
    public String goToAdmintest(){
        return "market/admin/admintest";
    }

    @GetMapping(value = "/market/user/usertest")
    public String goToUserTest(){
        return "market/user/usertest";
    }

    @GetMapping(value = "/test")
    public String test(){
        return "test";
    }

    @PostMapping(value = "/test")
    public String test2(){
        return "test";
    }



}
