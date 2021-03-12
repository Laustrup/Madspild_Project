package systemudvikling.madspild.controllers;

import org.apache.catalina.connector.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import systemudvikling.madspild.model.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.InputMismatchException;

@Controller
public class MainController {

    private Event event;
    private ArrayList<Event> events = new ArrayList<>();
    private HttpSession session;

    @GetMapping("/makeevent.html")
    public String createNadver() {
        return "makeevent.html";
    }
    @GetMapping("/makeeventagain.html")
    public String nadverError() {
        return "makeeventagain.html";
    }

    @GetMapping("/success.html")
    public String itsASuccess(@RequestParam(name = "Type") String type,
                              @RequestParam(name = "Adress") String adress,
                              @RequestParam(name = "Description", required = false) String description,
                              @RequestParam(name = "Hours") String hours,
                              @RequestParam(name = "Minutes") String minutes,
                              @RequestParam(name = "GiveAway", required = false) boolean isGiveAway,
                              @RequestParam(name = "Password") String passWord, Model model) {

        String giveAwayString = "";
        if (isGiveAway) {
            giveAwayString = "ja";
        }
        else {
            giveAwayString = "nej";
        }

        model.addAttribute("Type", type);
        model.addAttribute("Adress", adress);
        model.addAttribute("Description", description);
        model.addAttribute("Hours", hours);
        model.addAttribute("Minutes", minutes);
        model.addAttribute("GiveAway", giveAwayString);
        model.addAttribute("Password", passWord);

        return "success.html";
    }

    @GetMapping("/findgiver.html")
    public String findAGiver(Model model) {
        model.addAttribute("Events",events);
        return "findgiver.html";
    }

    @PostMapping("/submit-event")
    public String createEvent(HttpServletRequest request, Model model,
                              @RequestParam(name = "Type") String type,
                              @RequestParam(name = "Adress") String adress,
                              @RequestParam(name = "Description", required = false) String description,
                              @RequestParam(name = "Hours") String hours,
                              @RequestParam(name = "Minutes") String minutes,
                              @RequestParam(name = "GiveAway", required = false) boolean isGiveAway,
                              @RequestParam(name = "Password") String passWord, RedirectAttributes attributes) {

        boolean isAllowedInput = checkInputs(type, adress, hours, minutes, passWord);

        boolean returnToMakeevent = CheckIfReturn(isAllowedInput, isGiveAway,
                type,adress,description,hours,minutes,passWord);

        if (returnToMakeevent) {
            return "redirect:/makeeventagain.html";
        }
        else {
            event = new Event(type,adress,description,hours,minutes,isGiveAway,passWord);
        }

        events.add(event);

        session = request.getSession();

        /*
        session.setAttribute("Event", event);

        System.out.println(session.getAttribute("Event"));
         */

        attributes.addAttribute("Type", type);
        attributes.addAttribute("Adress", adress);
        attributes.addAttribute("Description", description);
        attributes.addAttribute("Hours", hours);
        attributes.addAttribute("Minutes", minutes);
        attributes.addAttribute("GiveAway", isGiveAway);
        attributes.addAttribute("Password", passWord);

        return "redirect:/success.html";
    }

    private boolean checkInputs(String type, String adress, String hours, String minutes, String passWord) {
        if (type.equals("")) {
            return true;
        }
        else if (adress.equals("")) {
            return true;
        }
        else if (hours.equals("")) {
            return true;
        }
        else if (minutes.equals("")) {
            return true;
        }
        else if (passWord.equals("")) {
            return true;
        }

        return false;
    }

    private boolean CheckIfReturn(boolean isAllowedInput, boolean isGiveAway, String type, String adress,
                                  String description, String hours, String minutes, String passWord) {
        if (isAllowedInput) {
            System.out.println("Required inputs was not written...");
            return true;
        }
        if (isGiveAway != true) {
            isGiveAway = Boolean.FALSE.equals(isGiveAway);
        }

        try {
            event = new Event(type,adress,description,hours,minutes,isGiveAway,passWord);
        }
        catch (IllegalArgumentException e) {
            return true;
        }
        catch (InputMismatchException e) {
            return true;
        }

        return false;
    }

    @GetMapping("/editevent.html")
    public String deleteEvent(Model model, @RequestParam(name = "Password") String passWord) {
    /*
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getPassword().equals(passWord)) {
                events.remove(events.get(i));
            }
        }
        model = null;
        model.addAttribute("events", session.getAttribute("Event"));

        System.out.println(session.getAttribute("Event"));
    */
        return "index.html";


    }

    @GetMapping("/index.html")
    public String goBack() {
        return "index.html";
    }
}
