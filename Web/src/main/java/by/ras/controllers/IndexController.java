package by.ras.controllers;

import by.ras.UserService;
import by.ras.WebException.WebException;
import by.ras.controllers.utils.InternalMethods;
import by.ras.entity.Occupation;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

    //constant address and setted role
    @Value("${home.address}")
    private String homeAddress;
    @ModelAttribute("home")
    public Model homeAddress(Model model){
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = InternalMethods.getActualRole(objUser);
        model.addAttribute("role", role);
        model.addAttribute("home_address", homeAddress);
        return model;
    }

    //method to login logout checks
    @GetMapping(value = "/")
    public String indexLoginLogout(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "logout", required = false) String logout,
                                   Model model, HttpServletRequest request) throws WebException{
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("date", new Date());
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);

        String role = String.valueOf(model.asMap().get("role"));
        log.info("method=index : Actual role is " + role);
        if((role != null) && !(role.equals("null"))) {
            try {
                //putting userId into to session!!!!!!
                long userId = userService.findByLogin(login).getId();
                request.getSession(true).setAttribute("user_id", userId);
                log.info("user_id=" + userId + " in INDEX");
                if (role.matches("CLIENT")) {
                    return "redirect:/market/user/usermain";
                } else if (role.matches("ADMIN")) {
                    return "redirect:/market/admin/adminmain";
                }
            } catch (ServiceException e) {
                log.info("Cant find by Login to get user_id");
                throw new WebException(e);
            }
        }
        log.info("user_id="+request.getSession().getAttribute("user_id"));
        return "index";
    }

    @GetMapping(value = "/error/403")
    public Model error403(Model model) {
        model.addAttribute("home_address", homeAddress);
        return model;
    }

    @GetMapping(value = "/test")
    public String test(Model model){
        model.addAttribute("login", SecurityContextHolder.getContext().getAuthentication().getName());
        //throw new RuntimeException();
        return "test";
    }

}
