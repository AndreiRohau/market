package by.ras.controllers;


import by.ras.ContactService;
import by.ras.OrderService;
import by.ras.ProductService;
import by.ras.UserService;
import by.ras.WebException.WebException;
import by.ras.controllers.utils.InternalMethods;
import by.ras.entity.Occupation;
import by.ras.entity.ProductType;
import by.ras.entity.Sex;
import by.ras.entity.particular.Contact;
import by.ras.entity.particular.Order;
import by.ras.entity.particular.Product;
import by.ras.entity.particular.User;
import by.ras.exception.ServiceException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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
public class AdminController {
    private final UserService userService;
    private final ContactService contactService;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public AdminController(UserService userService, ContactService contactService,
                           ProductService productService, OrderService orderService) {
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

    @ModelAttribute("occupations")
    public Occupation[] occupationTypes(){
        return Occupation.values();
    }
    @ModelAttribute("sexs")
    public Sex[] sexTypes(){
        return Sex.values();
    }
    @ModelAttribute("product")
    public Product productModel(){
        return new Product();
    }
    @ModelAttribute("productTypes")
    public ProductType[] productTypesTypes(){
        return ProductType.values();
    }

    @GetMapping(value = "/admin/adminmain")
    public String goToAdminmain(Model model){
        return "market/admin/adminmain";
    }

    //adding product to market
    @GetMapping("/admin/add_product")
    public String goToAddProduct(Model model){
        if(model.asMap().get("refresh") != null && model.asMap().get("refresh").equals("true")){
            model.addAttribute("user", new Product());
        }
        return "market/admin/add_product";
    }
    @PostMapping("/admin/add_product")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                               BindingResult bindingResult, Model model) throws WebException {
        log.info("in addProduct method");
        String result = "";
        if(!bindingResult.hasErrors()){
            try {
                product = productService.add(product);
                if(product.getId() != 0) {
                    result = "New product have been created.";
                }else {
                    result = "Product have not been saved. Probably, such model exists.";
                    log.error(result);
                }
            } catch (ServiceException e) {
                throw new WebException(e);
            }
        }
        model.addAttribute("result", result);
        return "market/admin/add_product";
    }

    //get all clients
    @GetMapping("/admin/manage_clients/{currentPage}")
    public String goToManageClients(Model model, @PathVariable("currentPage") int currentPage,
                                    HttpServletRequest request) throws WebException {
        try {
            int OBJECTS_PER_PAGE = 6;
            long maxRows = userService.countRows(); // 21
            log.info("max rows" + maxRows);
            long maxPage = (maxRows/OBJECTS_PER_PAGE) + (maxRows%OBJECTS_PER_PAGE == 0 ? 0 : 1); //3
            List<Long> pages = new LinkedList<>();
            for(int i = 0; i < maxPage; i++){
                pages.add(i, maxPage - i);
            }
            List<User> clients = userService.findAll(new PageRequest((currentPage-1), OBJECTS_PER_PAGE));
            model.addAttribute("clients", clients);
            model.addAttribute("pages", pages);
            model.addAttribute("current_page", ((long) currentPage));
            request.getSession().setAttribute("current_page", currentPage);
            return "market/admin/manage_clients";
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @PostMapping("/admin/manage_clients")
    public String manageClients(Model model) throws WebException {
        log.info("in manageClients method");

        return "market/admin/manage_clients";
    }

    //reset password for client method, password = [q1Q}
    @PostMapping("/admin/manage_clients/reset")
    public String resetClientsPassword(HttpServletRequest request) throws WebException {
        try {
            long userId = Long.parseLong(request.getParameter("user_id"));
            User user = new User();
            user.setId(userId);
            //reset to [q1Q}
            user.setPassword("q1Q");
            userService.resetPassword(user);
            return ("redirect:/market/admin/manage_clients/" + ((int) request.getSession().getAttribute("current_page")));
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    //change status for client
    @PostMapping("/admin/manage_clients/change_status")
    public String changeClientsStatus(HttpServletRequest request) throws WebException {
        try {
            long userId = Long.parseLong(request.getParameter("user_id"));
            User user = new User();
            user.setId(userId);
            //reset status to opposite;
            userService.changeStatus(user);
            return ("redirect:/market/admin/manage_clients/" + ((int) request.getSession().getAttribute("current_page")));
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }


    //get clint orders
    @GetMapping(value = "/admin/client_orders/{userId}/{currentPage}")
    public String goToClientOrders(Model model, @PathVariable("currentPage") int currentPage,
                                   @PathVariable("userId") long userId, HttpServletRequest request) throws WebException {
        try {
            int OBJECTS_PER_PAGE = 6;
            long maxRows = orderService.countRows(); // 21
            long maxPage = (maxRows/OBJECTS_PER_PAGE) + (maxRows%OBJECTS_PER_PAGE == 0 ? 0 : 1); //3
            List<Long> pages = new LinkedList<>();
            for(int i = 0; i < maxPage; i++){
                pages.add(i, maxPage - i);
            }
            User user = new User();
            user.setId(userId);
            List<Order> orders = orderService.findByUserId(user, new PageRequest((currentPage-1), OBJECTS_PER_PAGE));
            model.addAttribute("user_id", userId);
            model.addAttribute("orders", orders);
            model.addAttribute("pages", pages);
            model.addAttribute("current_page", currentPage);
            request.getSession().setAttribute("current_page", currentPage);
            return "market/admin/client_orders";
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }



    @GetMapping(value = "/admin/manage_orders")
    public String goToAdminmainManageOrders(Model model){
        return "market/admin/manage_orders";
    }




//to delete
//    @GetMapping(value = "/admin/edit_client/{userId}")
//    public String goToEditClient(Model model, @PathVariable("userId") long userId, HttpServletRequest request) throws WebException {
//        try{
//            User user = userService.findById(userId);
//
//            model.addAttribute("client", user);
//            model.addAttribute("user_id", userId);
//            return "market/admin/edit_client";
//        }catch (ServiceException e) {
//            throw new WebException(e);
//        }
//    }

    //editing profile
    @GetMapping("/admin/edit_client/{userId}")
    public String goToEditClient(@PathVariable("userId") long userId, @Valid @ModelAttribute("user") User user,
                                 @Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
                                 Model model, HttpServletRequest request) throws WebException {

        user.setId(userId);
        contact.setId(userId);
        try {
            String result = "";
            result = request.getParameter("result");
            log.info("user_id " + userId);
            user = userService.findById(userId);
            contact = user.getContact();
            if(user!=null){log.info("user " + user.toString());log.info("contact " + contact.toString());}
            model.addAttribute("user", user);
            model.addAttribute("contact", contact);
            model.addAttribute("login", user.getLogin());
            model.addAttribute("result", result);
            model.addAttribute("user_id", userId);

        } catch (ServiceException e) {
            throw new WebException(e);
        }
        return "market/admin/edit_client";
    }


    @PostMapping("/admin/edit_client/{userId}")
    public String editClient(@PathVariable("userId") int userId, @Valid @ModelAttribute("user") User user,
                             BindingResult bindingResultUser, @Valid @ModelAttribute("contact") Contact contact,
                             BindingResult bindingResultContact, Model model, HttpServletRequest request) throws WebException {

        user.setId(userId);
        contact.setId(userId);
        log.info("**************************");
        log.info("in POST updateUser");
        log.info(user.toString());
        log.info(contact.toString());
        log.info("**************************");
        String result = "";
        try {
            if((contact.getCountry() == null) & (!bindingResultUser.hasErrors())){
                log.info(user.toString());
                user = userService.updateByAdmin(user); //null?

                log.info(user.toString());
                model.addAttribute("user", user);
                log.info("BindingResult bindingResult DOESNT FACE ERRORS - 1");
                return "redirect:/market/admin/edit_client/"+userId+"?result=success";

            }else if((user.getName() == null) & (!bindingResultContact.hasErrors())){
                log.info(contact.toString());
                contact = contactService.editContact(contact);
                log.info(contact.toString());
                model.addAttribute("contact", contact);
                log.info("BindingResult bindingResult DOESNT FACE ERRORS - 2 upd contact");
                return "redirect:/market/admin/edit_client/"+userId+"?result=success";
            }
        } catch (Exception e) {
            return "redirect:/market/admin/edit_client/"+userId+"?result=incorrect%20login";
        }

        log.info("user_id " + userId);
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
        return "/market/admin/edit_client";
    }

    //editing profile
    @GetMapping("/admin/product/{productId}")
    public String goToEditProduct(@PathVariable("productId") long productId, @Valid @ModelAttribute("product") Product product,
                                 BindingResult bindingResult, Model model, HttpServletRequest request) throws WebException {
        product.setId(productId);
        try {
            String result = "";
            result = request.getParameter("result");
            log.info("product_id " + productId);
            product = productService.findById(productId);
            model.addAttribute("product", product);
            model.addAttribute("result", result);
            model.addAttribute("product_id", productId);
        } catch (ServiceException e) {
            throw new WebException(e);
        }
        return "market/admin/product";
    }
    @PostMapping("/admin/product/{productId}")
    public String editProduct(@PathVariable("productId") long productId, @Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult, Model model, HttpServletRequest request) throws WebException {

        product.setId(productId);
        log.info("**************************");
        log.info("in POST updatePRODUCT");
        log.info(product.toString());
        log.info("**************************");
        String result = "";
        try {
            if((!bindingResult.hasErrors())){
                log.info(product.toString());
                product = productService.update(product);
                log.info(product.toString());
                model.addAttribute("product", product);
                log.info("BindingResult bindingResult DOESNT FACE ERRORS");
                return "redirect:/market/admin/product/" + productId + "?result=success";
            }
            result = "Incorrect input data.";
            model.addAttribute("product", product);
            model.addAttribute("result", result);
            return "/market/admin/product";
        } catch (Exception e) {
            return "redirect:/market/admin/product/" + productId + "?result=Exception%20look%20at%20logs";
        }


    }

}
