package by.ras.controllers;

import by.ras.UserService;
import by.ras.entity.Occupation;
import by.ras.entity.Sex;
import by.ras.entity.particular.User;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
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
    @ModelAttribute("sexs")
    public Sex[] sexTypes(){
        return Sex.values();
    }

    @GetMapping("/register")
    public void registerUser(String name){
        userService.add(new User());
    }
    //check registration
    @GetMapping("user/registration")
    public String goToRegisterUser(){
        return "user/registration";
    }
    @PostMapping("/user/registration")
    public String registerUser(User user){
        System.out.println(user);
        return "redirect:/";
    }

    @GetMapping("/find/{id}")
    public User findUserById(@PathVariable Long id){
        return userService.findById(id);
    }

    @GetMapping("/find/{login}")
    public User findUserById(@PathVariable(value = "login") String login){
        return userService.fingByLogin(login);
    }

    @GetMapping("/find/all")
    public List<User> findAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/update")
    public void updateUser(Long id, String name){
        userService.update(new User());
    }

    @GetMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.delete(id);
    }
}