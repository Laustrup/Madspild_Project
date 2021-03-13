package systemudvikling.madspild.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import systemudvikling.madspild.model.Event;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.InputMismatchException;

@Controller
public class MainController {

    private Event event;
    private ArrayList<Event> events = new ArrayList<>();
    private String typeOfRedirect = new String();

    private boolean isAllowedInput;
    private boolean returnToMakeevent;
    private boolean isPasswordTaken;

    @GetMapping("/makeevent.html")
    public String createNadver(Model model) {

        if (returnToMakeevent && typeOfRedirect.equals("Not written")) {
            model.addAttribute("Exception", "Alle felter med * skal besvares!");
            System.out.println("Reached not written statement");
        }
        else if (returnToMakeevent && typeOfRedirect.equals("Time exception")) {
            model.addAttribute("Exception", "Tidsformattet er forkert!");
            System.out.println("Reached time exception statement");
        }
        else if (returnToMakeevent && isPasswordTaken) {
            model.addAttribute("Exception", "Adgangskode findes allerede...");
            System.out.println("Reached password is taken statement");
        }
        returnToMakeevent = false;

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
    public String createEvent(Model model,
            @RequestParam(name = "Type") String type,
            @RequestParam(name = "Adress") String adress,
            @RequestParam(name = "Description", required = false) String description,
            @RequestParam(name = "Hours") String hours,
            @RequestParam(name = "Minutes") String minutes,
            @RequestParam(name = "GiveAway", required = false) boolean isGiveAway,
            @RequestParam(name = "Password") String passWord, RedirectAttributes attributes) {

        boolean hasException = checkForEvent(type,adress,description,hours,minutes,passWord,isGiveAway);

        if (hasException) {
            return "redirect:/makeevent.html";
        }

        event = new Event(type,adress,description,hours,minutes,isGiveAway,passWord);
        events.add(event);

        model.addAttribute("Exception", "");
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
            return false;
        }
        else if (adress.equals("")) {
            return false;
        }
        else if (hours.equals("")) {
            return false;
        }
        else if (minutes.equals("")) {
            return false;
        }
        else if (passWord.equals("")) {
            return false;
        }

        return true;
    }

    private boolean checkIfReturn(boolean isAllowedInput, boolean isGiveAway, String type, String adress,
                                  String description, String hours, String minutes, String passWord) {
        if (!isAllowedInput) {
            System.out.println("Required inputs was not written...");
            typeOfRedirect = "Not written";
            return true;
        }
        if (isGiveAway != true) {
            isGiveAway = Boolean.FALSE.equals(isGiveAway);
        }

        try {
            event = new Event(type,adress,description,hours,minutes,isGiveAway,passWord);
        }
        catch (IllegalArgumentException e) {
            typeOfRedirect = "Time exception";
            return true;
        }
        catch (InputMismatchException e) {
            typeOfRedirect = "Time exception";
            return true;
        }

        return false;
    }

    private boolean checkPassword(String password) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getPassword() == password) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForEvent(String type, String adress, String description, String hours, String minutes,
                                   String passWord, boolean isGiveAway) {
        isAllowedInput = checkInputs(type, adress, hours, minutes, passWord);

        returnToMakeevent = checkIfReturn(isAllowedInput, isGiveAway,
                type,adress,description,hours,minutes,passWord);

        isPasswordTaken = checkPassword(passWord);

        if (returnToMakeevent) {
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
