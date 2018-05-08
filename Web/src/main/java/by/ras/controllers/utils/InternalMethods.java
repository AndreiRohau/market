package by.ras.controllers.utils;

import by.ras.entity.Role;
import lombok.extern.log4j.Log4j;

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

    //find out ID of the authorized user
    public static long getActualUserId(HttpServletRequest request){
        long userId = 0;
        userId = (long) request.getSession().getAttribute("user_id");
        return userId;
    }

}
