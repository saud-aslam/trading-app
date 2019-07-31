package ca.jrvs.apps.trading.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class AppController {

    @GetMapping(path = "/health")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String health() {
        try {
            return "I am healthy! Springboot is running";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
