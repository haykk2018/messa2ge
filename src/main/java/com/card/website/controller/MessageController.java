package com.card.website.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/message") // This means URL's start with /demo (after Application path)
@RestController // This means that this class is a Controller
public class MessageController {

    //      admin page by id
    @GetMapping
    public String firstMessage(){
        return "hello";
    }
    @GetMapping("{id}")
    public String messageByArgument (@PathVariable String id) {
        return id;
    }
}