package by.ras.controllers;

import by.ras.UserService;
import by.ras.entity.Occupation;
import by.ras.entity.Role;
import by.ras.entity.particular.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@Log4j
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

    //constant address
    @Value("${home.address}")
    private String homeAddress;
    @ModelAttribute("home")
    public Model homeAddress(Model model){
        return model.addAttribute("home_address", homeAddress);
    }

    //method to login logout checks
    @GetMapping(value = "/")
    public String indexLoginLogout(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("date", new Date());
        model.addAttribute("login", SecurityContextHolder.getContext().getAuthentication().getName());
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(objUser instanceof org.springframework.security.core.userdetails.User){
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) objUser;
            String role = Role.valueOf(String.valueOf(userDetails.getAuthorities().toArray()[0])).name();
            log.info("Actual role is " + role);
            model.addAttribute("role", role);
            if(role.matches("CLIENT")) {
                return "redirect:/market/user/usermain";
            }else if(role.matches("ADMIN")) {
                return "redirect:/market/admin/adminmain";
            }
        }
        return "index";
    }

    @GetMapping(value = "/error/403")
    public Model error403(Model model) {
        model.addAttribute("home_address", homeAddress);
        return model;
    }

    @GetMapping(value = "/market/products/products")
    public String goToProducts(Model model){
        return "market/products/products";
    }

    @GetMapping(value = "/market/admin/adminmain")
    public String goToAdminmain(Model model){
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = getActualRole(objUser);
        model.addAttribute("role", role);
        return "market/admin/adminmain";
    }

    @GetMapping(value = "/market/user/usermain")
    public String goToUsermain(Model model){
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = getActualRole(objUser);
        model.addAttribute("role", role);
        return "market/user/usermain";
    }

    @GetMapping(value = "/test")
    public String test(Model model){
        model.addAttribute("login", SecurityContextHolder.getContext().getAuthentication().getName());
        return "test";
    }

    private String getActualRole(Object objUser) {
        String role = null;
        if(objUser instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) objUser;
            role = Role.valueOf(String.valueOf(userDetails.getAuthorities().toArray()[0])).name();
            log.info("Actual role is " + role);
        }
        return role;
    }


}
