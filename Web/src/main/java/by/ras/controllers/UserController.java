package by.ras.controllers;

import by.ras.ContactService;
import by.ras.OrderService;
import by.ras.ProductService;
import by.ras.UserService;
import by.ras.WebException.WebException;
import by.ras.controllers.utils.InternalMethods;
import by.ras.entity.Occupation;
import by.ras.entity.Sex;
import by.ras.entity.particular.Contact;
import by.ras.entity.particular.Order;
import by.ras.entity.particular.Product;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/market")
@Log4j
public class UserController{
    private final UserService userService;
    private final ContactService contactService;
    private final ProductService productService;
    private final OrderService orderService;
    @Autowired
    public UserController(UserService userService, ContactService contactService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.contactService = contactService;
        this.productService = productService;
        this.orderService = orderService;
    }

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

    @GetMapping(value = "/user/usermain")
    public String goToUsermain(Model model){
        return "market/user/usermain";
    }

    //registration
    @GetMapping("/registration")
    public String goToRegisterUser(Model model){
        if(model.asMap().get("refresh") != null && model.asMap().get("refresh").equals("true")){
            model.addAttribute("user", new User());
        }
        return "market/registration";
    }
    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model) throws WebException {
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
                }
            } catch (ServiceException e) {
                throw new WebException(e);
            }
        }
        model.addAttribute("result", result);
        return "market/registration";
    }

    //editing profile
    @GetMapping("/user/profile")
    public String goToProfile(@Valid @ModelAttribute("user") User user, @Valid @ModelAttribute("contact") Contact contact,
                              BindingResult bindingResult, Model model, HttpServletRequest request) throws WebException {
        long userId = InternalMethods.getActualUserId(request);
        user.setId(userId);
        contact.setId(userId);
        try {
            String result = "";
            result = request.getParameter("result");
            user = userService.findById(userId);
            contact = user.getContact();
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
        long userId = InternalMethods.getActualUserId(request);
        user.setId(userId);
        contact.setId(userId);
        String result = "";
        try {
            if((contact.getCountry() == null) & (!bindingResultUser.hasErrors())){
                user = userService.update(user); //null?
                model.addAttribute("user", user);
                return "redirect:/market/user/profile?result=success";
            }else if((user.getName() == null) & (!bindingResultContact.hasErrors())){
                contact = contactService.editContact(contact);
                model.addAttribute("contact", contact);
                return "redirect:/market/user/profile?result=success";
            }
        } catch (Exception e) {
            return "redirect:/market/user/profile?result=incorrect%20login";
        }
        try {
            if (contact.getCountry() == null) {
                contact = userService.findById(userId).getContact();
                model.addAttribute("contact", contact);
            } else if(user.getName() == null){
                user = userService.findById(userId);
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

    @GetMapping("/user/basket")
    public String goToBusket(Model model, HttpServletRequest request) throws WebException {
        try{
            User user = userService.findById(InternalMethods.getActualUserId(request));
            List<Product> products = user.getReservedProducts();
            long totalPrice = 0;
            for(Product p : products){
                totalPrice += Long.valueOf(p.getPrice());
            }
            model.addAttribute("result", request.getParameter("result"));
            model.addAttribute("products", products);
            model.addAttribute("totalPrice", totalPrice);
            return "/market/user/basket";
        }catch (ServiceException e){
            throw new WebException(e);
        }
    }

    @PostMapping("/user/remove/{productId}")
    public String removeProductFromBusket(@PathVariable("productId") long productId, HttpServletRequest request) throws WebException {
        try {
            User user = userService.findById(InternalMethods.getActualUserId(request));
            Product product = productService.findById(productId);
            user.cancelReserve(product);
            userService.edit(user);
            return "redirect:/market/user/basket?result=Product%20removed";
        }catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @PostMapping("/user/buy")
    public String createNewOrder(HttpServletRequest request, Model model) throws WebException {
        try {
            User user = userService.findById(InternalMethods.getActualUserId(request));
            List<Product> products = new LinkedList<>(user.getReservedProducts());
            user.getReservedProducts().clear();
            userService.edit(user);
            long totalPrice = 0;
            for(Product p : products){
                totalPrice += Long.valueOf(p.getPrice());
            }
            Order order = new Order(user, String.valueOf(totalPrice));
            order = orderService.addOrder(order);
            order.setOrderedProducts(products);
            orderService.editOrder(order);
            model.addAttribute("user", user);
            model.addAttribute("result", "Order created");
            model.addAttribute("products", products);
            model.addAttribute("totalPrice", totalPrice);
            return "redirect:/market/user/orders";
        }catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @GetMapping("/user/orders")
    public String goToOrders(HttpServletRequest request, Model model) throws WebException {
        try{
            User user = userService.findById(InternalMethods.getActualUserId(request));
            List<Order> orders = user.getOrders();
            model.addAttribute("orders", orders);
            return "/market/user/orders";
        }catch (ServiceException e){
            throw new WebException(e);
        }
    }

}