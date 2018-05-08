package by.ras.controllers;

import by.ras.ContactService;
import by.ras.UserService;
import by.ras.WebException.WebException;
import by.ras.entity.Occupation;
import by.ras.entity.Role;
import by.ras.entity.Sex;
import by.ras.entity.particular.Contact;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/market")
@Log4j
public class UserController{
    private final UserService userService;
    private final ContactService contactService;
    @Autowired
    public UserController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

    @Value("${home.address}")
    private String homeAddress;
    @ModelAttribute("home")
    public Model homeAddress(Model model){
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = getActualRole(objUser);
        model.addAttribute("role", role);
        model.addAttribute("home_address", homeAddress);
        return model;
    }

    @ModelAttribute("user")
    public User userModel(){
        return new User();
    }
    @ModelAttribute("contact")
    public Contact contactModel(){
        return new Contact();
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
    @GetMapping("/registration")
    public String goToRegisterUser(){
        return "market/registration";
    }
    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model) throws WebException {
        log.info("in reg method");
        String result = "";
        if(!bindingResult.hasErrors()){
            try {
                user = userService.add(user);
                if(user.getId() != 0) {
                    contactService.addContact(Contact.builder().user(user).country("None")
                            .city("None").street("None").houseNumber("0").phoneNumber("000000000000").build());
                    result = "New user and default contacts have been created.";
                }else {
                    result = "User have not been saved. Probably, such login exists.";
                    log.error(result);
                }
            } catch (ServiceException e) {
                throw new WebException(e);
            }
        }
        model.addAttribute("result", result);
        return "market/registration";
    }

    @GetMapping("/user/profile")
    public String goToProfile(@Valid @ModelAttribute("user") User user, @Valid @ModelAttribute("contact") Contact contact,
                              BindingResult bindingResult, Model model, HttpServletRequest request) throws WebException {
//        long userId = getActualUserId(request);
//        user.setId(userId);
//        contact.setId(userId);
        try {
            String result = "";
            result = request.getParameter("result");
            long user_id = (long) request.getSession().getAttribute("user_id");
            log.info("user_id " + user_id);
            user = userService.findById(user_id);
            contact = user.getContact();
            if(user!=null){log.info("user " + user.toString());log.info("contact " + contact.toString());}
            model.addAttribute("user", user);
            model.addAttribute("contact", contact);
            model.addAttribute("login", user.getLogin());
            model.addAttribute("result", result);

        } catch (ServiceException e) {
            throw new WebException(e);
        }
        return "market/user/profile";
    }


    @PostMapping("/user/profile")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResultUser,
                             @Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResultContact,
                             Model model, HttpServletRequest request) throws WebException {
//        long userId = getActualUserId(request);
//        user.setId(userId);
//        contact.setId(userId);
        log.info("**************************");
        log.info("in POST updateUser");
        log.info(user.toString());
        log.info(contact.toString());
        log.info("**************************");
        String result = "";
            try {
                if((contact.getCountry() == null) & (!bindingResultUser.hasErrors())){
                    log.info(user.toString());
                    user = userService.update(user);

                    log.info(user.toString());
                    model.addAttribute("user", user);
                    log.info("BindingResult bindingResult DOESNT FACE ERRORS - 1");
                    return "redirect:/market/user/profile?result=success";

                }else if((user.getName() == null) & (!bindingResultContact.hasErrors())){
                    log.info(contact.toString());
                    contact = contactService.editContact(contact);
                    log.info(contact.toString());
                    model.addAttribute("contact", contact);
                    log.info("BindingResult bindingResult DOESNT FACE ERRORS - 2 upd contact");
                    return "redirect:/market/user/profile?result=success";
                }
            } catch (Exception e) {
                return "redirect:/market/user/profile?result=incorrect%20login";
            }

        long user_id = (long) request.getSession().getAttribute("user_id");
        log.info("user_id " + user_id);
        try {
            if (contact.getCountry() == null) {
                contact = userService.findById(user_id).getContact();
                model.addAttribute("contact", contact);
            } else if(user.getName() == null){
                user = userService.findById(user_id);
                model.addAttribute("user", user);

            }
        }catch (ServiceException e){
            throw new WebException(e);
        }
        result = "Incorrect input data.";
        model.addAttribute("result", result);
        model.addAttribute("login", user.getLogin());
        return "/market/user/profile";
    }
//@PostMapping("/user/profile")
//    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResultUser,
//                             @Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResultContact,
//                             Model model, HttpServletRequest request) throws WebException {
//        log.info("**************************");
//        log.info("in POST updateUser");
//        log.info(user.toString());
//        log.info(contact.toString());
//        log.info("**************************");
//        boolean success = false;
//        if(!bindingResultUser.hasErrors() || !bindingResultContact.hasErrors()) {
//            log.info("BindingResult bindingResult DOESNT FACE ERRORS");
//            try {
//                if(contact.getCountry() == null){
//                    log.info(user.toString());
//                    user = userService.update(user);
//
//                    log.info(user.toString());
//                    model.addAttribute("user", user);
//                }else if(user.getName() == null){
//                    log.info(contact.toString());
//                    contact = contactService.editContact(contact);
//                    log.info(contact.toString());
//                    model.addAttribute("contact", contact);
//                }
//            } catch (ServiceException e) {
//                throw new WebException(e);
//            }
//            success = true;
//            return "redirect:/market/user/profile";
//        }
//        long user_id = (long) request.getSession().getAttribute("user_id");
//        log.info("user_id " + user_id);
//        try {
//            if (contact.getCountry() == null) {
//                contact = userService.findById(user_id).getContact();
//                model.addAttribute("contact", contact);
//
//            } else if(user.getName() == null){
//                user = userService.findById(user_id);
//                model.addAttribute("user", user);
//
//            }
//        }catch (ServiceException e){
//            throw new WebException(e);
//        }
//        model.addAttribute("success", success);
//        return "/market/user/profile";
//    }



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



    //INTERNAL METHODS
    private String getActualRole(Object objUser) {
        String role = null;
        if(objUser instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) objUser;
            role = Role.valueOf(String.valueOf(userDetails.getAuthorities().toArray()[0])).name();
        }
        return role;
    }

//    private long getActualUserId(HttpServletRequest request){
//        long userId = 0;
//        userId = (long) request.getSession().getAttribute("user_id");
//        return userId;
//    }
}