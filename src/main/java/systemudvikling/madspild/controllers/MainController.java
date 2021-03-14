package systemudvikling.madspild.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import systemudvikling.madspild.model.Event;
import java.util.ArrayList;
import java.util.InputMismatchException;

@Controller
public class MainController {

    private Event event;
    private ArrayList<Event> events = new ArrayList<>();

    private boolean isWritten;
    private boolean correctTimeFormat;
    private boolean isAllowedInput;
    private boolean returnToMakeevent;
    private boolean isPasswordTaken;
    private boolean tooLongDescription;

    private boolean isAnSubmitError;

    @GetMapping("/makeevent.html")
    public String createNadver(Model model) {
        arrangeModel(model);

        return "makeevent.html";
    }

    private void arrangeModel(Model model) {
        if (isAnSubmitError) {
            if (!isWritten) {
                model.addAttribute("Exception", "Alle felter med * skal besvares!");
                System.out.println("Reached not written statement");
            }
            else if (!correctTimeFormat) {
                model.addAttribute("Exception", "Tidsformattet er forkert!");
                System.out.println("Reached time exception statement");
            }
            else if (isPasswordTaken) {
                model.addAttribute("Exception", "Adgangskode findes allerede...");
                System.out.println("Reached password is taken statement");
            }
            else if (tooLongDescription) {
                model.addAttribute("Exception", "Beskrivelsen må højest have 90 tegn!");
                System.out.println("Reached character statement");
            }
        }
        else {
            model.addAttribute("Exception", "");
        }
        //Clears all boolean attributes for next submit.
        isWritten = true;
        correctTimeFormat = true;
        returnToMakeevent = false;
        isPasswordTaken = false;

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
    public String createEvent(@RequestParam(name = "Type") String type,
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
            isWritten = false;
            return true;
        }
        if (isGiveAway != true) {
            isGiveAway = Boolean.FALSE.equals(isGiveAway);
        }

        try {
            event = new Event(type,adress,description,hours,minutes,isGiveAway,passWord);
        }
        catch (IllegalArgumentException e) {
            correctTimeFormat = false;
            return true;
        }
        catch (InputMismatchException e) {
            correctTimeFormat = false;
            return true;
        }
        if (isPasswordTaken) {
            return true;
        }
        if (tooLongDescription) {
            return true;
        }

        return false;
    }

    private boolean checkPassword(String password) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDescriptionLength(String description) {
        if (description.length() > 90) {
            return true;
        }
        else {
            return false;
        }

    }

    private boolean checkForEvent(String type, String adress, String description, String hours, String minutes,
                                   String passWord, boolean isGiveAway) {

        isAllowedInput = checkInputs(type, adress, hours, minutes, passWord);
        isPasswordTaken = checkPassword(passWord);
        tooLongDescription = checkDescriptionLength(description);
        returnToMakeevent = checkIfReturn(isAllowedInput, isGiveAway,
                type,adress,description,hours,minutes,passWord);

        if (returnToMakeevent) {
            isAnSubmitError = true;
            return true;
        }
        return false;
    }

    @PostMapping("/editevent.html")
    public String showEvent(Model model, @RequestParam(name = "Search") String passWord) {

        boolean isFound = false;

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getPassword().equals(passWord)) {
                model.addAttribute("Chosen event",events.get(i));
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            model.addAttribute("Chosen event", "couldn't find event");
            model.addAttribute("Chose to delete", "");
        }
        return "editevent.html";
    }

    @GetMapping("/index.html")
    public String goBack() {
        isAnSubmitError = false;
        return "index.html";
    }

    @GetMapping("/")
    public String start() {
        isAnSubmitError = false;
        return "index.html";
    }
}
