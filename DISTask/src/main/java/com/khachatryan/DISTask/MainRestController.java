package com.khachatryan.DISTask;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/main")
public class MainRestController {

    @GetMapping
    public String SayHello(){
        String res = "<h1>Hello!</h1>";
        res += "<h2>This is the task realisation for DISGroup from Khachatryan Sarmen</h2>";
        res += "<a href=\"http://localhost:8080/main/products\">Products database</a> <br>";
        res += "<a href=\"http://localhost:8080/main/articles\">Articles database</a>";
        return res;
    }
}
