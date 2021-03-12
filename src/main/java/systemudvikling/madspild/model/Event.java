package systemudvikling.madspild.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

public class Event {

    private String type;
    private String address;
    private String description;

    private double timeStart;
    private double timeEnd;
    private double present;
    private double time;

    private boolean isGiveAway;
    private String password;

    private Date date = new Date();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd:mm:yyyy");

    public Event(String type, String address, String description, String start,
                 String end, boolean isGiveAway, String password)
            throws IllegalArgumentException,InputMismatchException {

        this.type = type;
        this.address = address;
        this.description = description;

        String[] startAlt = start.split(":");
        String[] endAlt = end.split(":");

        convertTimeToDouble(startAlt, endAlt);

        present = convertDateToDouble();

        if (timeStart > present) {
            time = timeEnd - timeStart;
        }
        else {
            time = timeEnd - present;
        }

        this.isGiveAway = isGiveAway;
        this.password = password;
    }

    private void convertTimeToDouble(String[] startAlt, String[] endAlt) throws  IllegalArgumentException,
            InputMismatchException {

        double startMinutes = 0;
        double startHours = 0;

        double endMinutes = 0;
        double endHours = 0;

        boolean throwException = false;

        try {
            startHours = Double.parseDouble(startAlt[0]);
            startMinutes = Double.parseDouble(startAlt[1]);

            endHours = Double.parseDouble(endAlt[0]);
            endMinutes = Double.parseDouble(endAlt[1]);
        }
        catch (InputMismatchException e) {
            System.out.println("Will not accept words as time...");
            throwException = true;
        }

        if (throwException) {
            throw new InputMismatchException();
        }

        if (startHours > 24 || endHours > 24 || startMinutes > 59 || endMinutes > 59) {
            throw new IllegalArgumentException();
        }

        timeStart = startHours + (startMinutes/100);
        timeEnd = endHours + (endMinutes/100);
    }

    private Double convertDateToDouble() {
        String now = dateFormat.format(date);

        String[] nowArr = now.split(":");
        return 0.0;
    }

    public String getType() {
        return type;
    }

    public String getAddress() { return address; }

    public String getDescription() { return description; }

    //public String getTime() { return time; }

    public String getIsGiveAway() {
        if (isGiveAway) {
            return "Ja";
        }
        else {
            return "nej";
        }
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        String giveAway = "";

        if (isGiveAway) {
            giveAway = " gives væk ";
        }

        return "type= " + type + " location= " + address + " beskrivelse= " + description +
                " starttidspunkt= " + timeStart + " sluttidspunkt= " + timeEnd + giveAway + "adgangskode er " + password;
    }
}
