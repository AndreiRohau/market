package by.ras.controllers.utils;

import by.ras.entity.Role;
import by.ras.entity.particular.Order;
import by.ras.entity.particular.Product;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

@Log4j
public class InternalMethods {

    //find out ROLE of the authorized user
    public static String getActualRole(Object objUser) {
        String role = null;
        if(objUser instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) objUser;
            role = Role.valueOf(String.valueOf(userDetails.getAuthorities().toArray()[0])).name();
            log.info(" method=private String getActualRole() : Actual role is " + role);
        }
        return role;
    }
    //find out ROLE of the authorized user
    public static String getRole() {
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = null;
        if(objUser instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) objUser;
            role = Role.valueOf(String.valueOf(userDetails.getAuthorities().toArray()[0])).name();
            log.info(" method=private String getActualRole() : Actual role is " + role);
        }
        return role;
    }

    //find out ID of the authorized user
    public static long getActualUserId(HttpServletRequest request){
        long userId = 0;
        userId = (long) request.getSession().getAttribute("user_id");
        return userId;
    }

    //initialize empty product
    public static Product initProductFilter(Product p){
        Product product = new Product();
        if((p.getCompany() != null) && !(p.getCompany().equals(""))){
            product.setCompany(p.getCompany());
        }
        if((p.getProductName() != null) && !(p.getProductName().equals(""))){
            product.setProductName(p.getProductName());
        }
        if((p.getModel() != null) && !(p.getModel().equals(""))){
            product.setModel(p.getModel());
        }
        if((p.getProductType() != null) && !(p.getProductType().equals(""))){
            product.setProductType(p.getProductType());
        }
        if((p.getPrice() != null) && !(p.getPrice().equals(""))){
            product.setPrice(p.getPrice());
        }
        if((p.getDescription() != null) && !(p.getDescription().equals(""))){
            product.setDescription(p.getDescription());
        }

        return product;
    }
    //initialize empty product
    public static Order initOrderFilter(Order o){
        Order order = new Order();
        if((o.getOrderStatus() != null) && !(o.getOrderStatus().equals(""))){
            order.setOrderStatus(o.getOrderStatus());
        }
        return order;
    }

}
