package by.ras.controllers;

import by.ras.UserService;
import by.ras.WebException.WebException;
import by.ras.entity.Occupation;
import by.ras.entity.Sex;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/market")
public class UserController{
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
    public void registerUser(String name) throws WebException {
        try {
            userService.add(new User());
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    //check registration
    @GetMapping("user/registration")
    public String goToRegisterUser(){
        return "market/user/registration";
    }
    @PostMapping("/user/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        model.addAttribute("success", null);
        if(!bindingResult.hasErrors()){
            System.out.println("creating user : " + user);
            model.addAttribute("success", "New User has been created");
        }
        return "market/user/registration";
    }



    @GetMapping("/find/{id}")
    public User findUserById(@PathVariable Long id) throws WebException {
        try {
            return userService.findById(id);
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @GetMapping("/find/{login}")
    public User findUserById(@PathVariable(value = "login") String login) throws WebException {
        try {
            return userService.findByLogin(login);
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @GetMapping("/find/all")
    public List<User> findAllUsers() throws WebException {
        try {
            return userService.findAll();
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @GetMapping("/update")
    public void updateUser(Long id, String name) throws WebException {
        try {
            userService.update(new User());
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @GetMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) throws WebException {
        try {
            userService.delete(id);
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }
}