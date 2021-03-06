package by.ras.controllers;

import by.ras.ContactService;
import by.ras.OrderService;
import by.ras.ProductService;
import by.ras.UserService;
import by.ras.WebException.WebException;
import by.ras.controllers.utils.InternalMethods;
import by.ras.entity.*;
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
    public ProductType[] productTypes(){
        return ProductType.values();
    }
    @ModelAttribute("order")
    public Order orderModel(){
        return new Order();
    }
    @ModelAttribute("orderStatuses")
    public OrderStatus[] orderStatuses(){
        return OrderStatus.values();
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
        return "market/admin/manage_clients";
    }

    //reset password for client method, password = [q1Q}
    @PostMapping("/admin/manage_clients/reset")
    public String resetClientsPassword(HttpServletRequest request) throws WebException {
        try {
            long userId = Long.parseLong(request.getParameter("user_id"));
            User user = new User();
            user.setId(userId);
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
            User user = new User();
            user.setId(Long.parseLong(request.getParameter("user_id")));
            //reset status to opposite;
            userService.changeRole(user);
            return ("redirect:/market/admin/manage_clients/" + ((int) request.getSession().getAttribute("current_page")));
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    //get clint orders
    @GetMapping(value = "/admin/client_orders/{userId}")
    public String goToClientOrders(Model model, @PathVariable("userId") long userId,
                                   HttpServletRequest request) throws WebException {
        try {
            User user = userService.findById(userId);
            List<Order> orders = user.getOrders();
            model.addAttribute("user_name", user.getLogin());
            model.addAttribute("user_id", user.getId());
            model.addAttribute("user_login", user.getLogin());
            model.addAttribute("orders", orders);
            return "market/admin/client_orders";
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    //inspect client ORDER
    @GetMapping("/admin/order/{orderId}")
    public String goToOrder(Model model, @PathVariable("orderId") long orderId,
                            HttpServletRequest request) throws WebException {
        try {
            Order order = orderService.findById(orderId);
            List<Product> products = order.getOrderedProducts();
            long userId = order.getUser().getId();
            model.addAttribute("products", products);
            model.addAttribute("orderId", orderId);
            model.addAttribute("user_id", userId);
            return "/market/admin/order";
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    //manager of orders
    @GetMapping(value = "/admin/manage_orders/{currentPage}")
    public String goToOrders(@ModelAttribute("product") Order order, Model model, @PathVariable("currentPage") int currentPage,
                               HttpServletRequest request) throws WebException {
        try {
            int OBJECTS_PER_PAGE = 6;
            long maxRows = orderService.countRows(); // 42
            long maxPage = (maxRows/OBJECTS_PER_PAGE) + (maxRows%OBJECTS_PER_PAGE == 0 ? 0 : 1); //3
            List<Long> pages = new LinkedList<>();
            for(int i = 0; i < maxPage; i++){
                pages.add(i, maxPage - i);
            }
            List<Order> orders = orderService.findAll(new PageRequest((currentPage-1), OBJECTS_PER_PAGE));
            model.addAttribute("orders", orders);
            model.addAttribute("pages", pages);
            model.addAttribute("current_page", ((long) currentPage));
            request.getSession().setAttribute("current_page", currentPage);
            return "/market/admin/manage_orders";

        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }
    @PostMapping("/admin/manage_orders/{currentPage}")
    public String searchOrders(@ModelAttribute("order") Order order, Model model,
                                 @PathVariable("currentPage") int currentPage,
                                 HttpServletRequest request) throws WebException {
        try {
            Order filter = InternalMethods.initOrderFilter(order);
            int OBJECTS_PER_PAGE = 6;
            long maxRows = orderService.countRowsComplex(filter); // 21
            long maxPage = (maxRows/OBJECTS_PER_PAGE) + (maxRows%OBJECTS_PER_PAGE == 0 ? 0 : 1); //3
            List<Long> pages = new LinkedList<>();
            for(int i = 0; i < maxPage; i++){
                pages.add(i, maxPage - i);
            }
            List<Order> orders = orderService.findAllComplex(filter, new PageRequest((currentPage-1), OBJECTS_PER_PAGE));
            model.addAttribute("post_method", true);
            model.addAttribute("order", order);
            model.addAttribute("orders", orders);
            model.addAttribute("pages", pages);
            model.addAttribute("current_page", ((long) currentPage));
            request.getSession().setAttribute("current_page", currentPage);
            return "/market/admin/manage_orders";
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    //change order status
    @PostMapping(value = "/admin/manage_order/change_status/{orderId}")
    public String changeOrderStatus(@ModelAttribute("order") Order order, Model model, @PathVariable("orderId") long orderId,
                                    HttpServletRequest request) throws WebException {
        try {
            Order orderToChange = orderService.findById(orderId);
            String orderStatus = orderToChange.getOrderStatus();
            if(orderStatus.equals(OrderStatus.NEW.name())){
                orderToChange.setOrderStatus(OrderStatus.ACTIVE.name());
            } else if(orderStatus.equals(OrderStatus.ACTIVE.name())) {
                orderToChange.setOrderStatus(OrderStatus.CLOSED.name());
            } else {
                orderToChange.setOrderStatus(OrderStatus.NEW.name());
            }
            orderService.editOrder(orderToChange);
            model.addAttribute("order", order);
            if(request.getParameter("change_order_status_trigger").equals("in_manage_orders")){
                return searchOrders(order, model, Integer.parseInt(request.getParameter("current_page")), request);
            }else {
                return goToClientOrders(model, orderToChange.getUser().getId(), request);
            }
        }catch (ServiceException e){
            throw new WebException(e);
        }
    }

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
            user = userService.findById(userId);
            contact = user.getContact();
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
        String result = "";
        try {
            if((contact.getCountry() == null) & (!bindingResultUser.hasErrors())){
                user = userService.updateByAdmin(user); //null?
                model.addAttribute("user", user);
                return "redirect:/market/admin/edit_client/"+userId+"?result=success";

            }else if((user.getName() == null) & (!bindingResultContact.hasErrors())){
                contact = contactService.editContact(contact);
                model.addAttribute("contact", contact);
                return "redirect:/market/admin/edit_client/"+userId+"?result=success";
            }
        } catch (Exception e) {
            return "redirect:/market/admin/edit_client/"+userId+"?result=incorrect%20login";
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
        return "/market/admin/edit_client";
    }

    //editing profile
    @GetMapping("/admin/product/{productId}")
    public String goToEditProduct(@PathVariable("productId") long productId, @Valid @ModelAttribute("product") Product product,
                                 BindingResult bindingResult, Model model, HttpServletRequest request) throws WebException {
        try {
            product.setId(productId);
            String result = "";
            result = request.getParameter("result");
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
        try {
            product.setId(productId);
            String result = "";
            if((!bindingResult.hasErrors())){
                product = productService.update(product);
                model.addAttribute("product", product);
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
