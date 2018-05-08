package by.ras.controllers;


import by.ras.ContactService;
import by.ras.ProductService;
import by.ras.UserService;
import by.ras.WebException.WebException;
import by.ras.controllers.utils.InternalMethods;
import by.ras.entity.ProductType;
import by.ras.entity.particular.Product;
import by.ras.exception.ServiceException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/market")
@Log4j
public class AdminController {
    private final UserService userService;
    private final ContactService contactService;
    private final ProductService productServiceService;
    @Autowired
    public AdminController(UserService userService, ContactService contactService, ProductService productServiceService) {
        this.userService = userService;
        this.contactService = contactService;
        this.productServiceService = productServiceService;
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

    @ModelAttribute("product")
    public Product productModel(){
        return new Product();
    }
    @ModelAttribute("productTypes")
    public ProductType[] sexTypes(){
        return ProductType.values();
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
                product = productServiceService.add(product);
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



    @GetMapping(value = "/admin/adminmain")
    public String goToAdminmain(Model model){
        return "market/admin/adminmain";
    }
    @GetMapping(value = "/admin/manage_orders")
    public String goToAdminmainManageOrders(Model model){
        return "market/admin/manage_orders";
    }
    @GetMapping(value = "/admin/manage_clients")
    public String goToAdminmainManageClients(Model model){
        return "market/admin/manage_clients";
    }

}
