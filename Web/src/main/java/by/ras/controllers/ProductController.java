package by.ras.controllers;

import by.ras.controllers.utils.InternalMethods;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@Log4j
public class ProductController {

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

    @GetMapping(value = "/market/products/products")
    public String goToProducts(Model model){
        return "market/products/products";
    }

}
