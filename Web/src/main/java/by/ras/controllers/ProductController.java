package by.ras.controllers;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@Log4j
public class ProductController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public ProductController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @ModelAttribute("product")
    public Product productModel(){
        return new Product();
    }
    @ModelAttribute("productTypes")
    public ProductType[] productTypesTypes(){
        return ProductType.values();
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

    @PostMapping("/market/products/product/{productId}")
    public String goToInfo(@PathVariable("productId") long productId, Model model) throws WebException {
        try {
            Product product = productService.findById(productId);
            model.addAttribute("product", product);
            if(InternalMethods.getRole().equals("ADMIN")){
                //todo: if deleting already used product (for reserve or order), throws expt.
                return "market/admin/product";
            }else {
                return "market/user/product";
            }
        }catch (ServiceException e){
            throw new WebException(e);
        }
    }


    @GetMapping(value = "/market/products/products/{currentPage}")
    public String goToProducts(@ModelAttribute("product") Product product, Model model, @PathVariable("currentPage") int currentPage,
                                    HttpServletRequest request) throws WebException {
        try {
            int OBJECTS_PER_PAGE = 6;
            long maxRows = productService.countRows(); // 42
            long maxPage = (maxRows/OBJECTS_PER_PAGE) + (maxRows%OBJECTS_PER_PAGE == 0 ? 0 : 1); //3
            List<Long> pages = new LinkedList<>();
            for(int i = 0; i < maxPage; i++){
                pages.add(i, maxPage - i);
            }
            List<Product> products = productService.findAll(new PageRequest((currentPage-1), OBJECTS_PER_PAGE));
            model.addAttribute("products", products);
            model.addAttribute("pages", pages);
            model.addAttribute("current_page", ((long) currentPage));
            model.addAttribute("role", InternalMethods.getRole());
            request.getSession().setAttribute("current_page", currentPage);
            return "/market/products/products";
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }

    @PostMapping("/market/products/products/{currentPage}")
    public String searchProducts(@ModelAttribute("product") Product product, Model model, @PathVariable("currentPage") int currentPage,
                               HttpServletRequest request) throws WebException {
        try {
            //optional if authorized as user - adds to his basket. if as ADMIN - deletes drops to trash =)
            if(request.getParameter("product_id_for_add") != null) {
                Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String actualRole = InternalMethods.getActualRole(objUser);
                Product productExecute = productService.findById(Long.valueOf(request.getParameter("product_id_for_add")));
                if(actualRole.equals("ADMIN")){
                    productService.delete(productExecute.getId());
                } else {
                    userService.addReserve(InternalMethods.getActualUserId(request), productExecute);
                }
            }
            Product filter = InternalMethods.initProductFilter(product);
            int OBJECTS_PER_PAGE = 6;
            long maxRows = productService.countRowsComplex(filter); // 21
            long maxPage = (maxRows/OBJECTS_PER_PAGE) + (maxRows%OBJECTS_PER_PAGE == 0 ? 0 : 1); //3
            List<Long> pages = new LinkedList<>();
            for(int i = 0; i < maxPage; i++){
                pages.add(i, maxPage - i);
            }
            List<Product> products = productService.findAllComplex(filter, new PageRequest((currentPage-1), OBJECTS_PER_PAGE));
            model.addAttribute("post_method", true);
            model.addAttribute("product", product);
            model.addAttribute("products", products);
            model.addAttribute("pages", pages);
            model.addAttribute("current_page", ((long) currentPage));
            model.addAttribute("role", InternalMethods.getRole());
            request.getSession().setAttribute("current_page", currentPage);
            return "/market/products/products";
        } catch (ServiceException e) {
            throw new WebException(e);
        }
    }
}
