package systemudvikling.madspild.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import systemudvikling.madspild.model.Event;

@Controller
public class MainController {

    Event event;

    @GetMapping("/makeevent.html")
    public String createNadver() {
        return "makeevent.html";
    }

    @PostMapping("/submit-event")
    public String createEvent() {
        return "redirect:/success";
    }

    @GetMapping("/index.html")
    public String goBack() {
        return "index.html";
    }


}
