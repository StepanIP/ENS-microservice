package ua.writer.writerservice.controller;

import com.example.common.model.Notification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping
    public String get(){
        Notification notification = new Notification();
        return "aboba";
    }

}
