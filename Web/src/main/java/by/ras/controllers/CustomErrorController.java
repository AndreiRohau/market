//package by.ras.controllers;
//
//import lombok.extern.log4j.Log4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.web.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//@RequestMapping("/error")
//@Log4j
//public class CustomErrorController implements ErrorController {
//    //constant address
//    @Value("${home.address}")
//    private String homeAddress;
//    @ModelAttribute("home")
//    public Model homeAddress(Model model){
//        return model.addAttribute("home_address", homeAddress);
//    }
//
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request) {
//        System.out.println("in handler");
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                log.info("Page not found");
//                return "404";
//            }
//            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                log.info("Server error");
//                return "5xx";
//            }
//        }
//        log.info("Access denied error");
//        return "403";
//    }
//
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//}
