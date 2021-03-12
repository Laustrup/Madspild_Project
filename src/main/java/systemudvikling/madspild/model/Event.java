package systemudvikling.madspild.model;

public class Event {

    private String type;
    private String address;
    private String description;
    private String start;
    private String end;
    private boolean isGiveAway;
    private String password;

    public Event(String type, String address, String description, String start, String end, boolean isGiveAway, String password) {
        this.type = type;
        this.address = address;
        this.description = description;
        this.start = start;
        this.end = end;
        this.isGiveAway = isGiveAway;
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public String getAddress() { return address; }

    public String getDescription() { return description; }

    public String getStart() { return start; }

    public String getEnd() { return end; }

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
                " starttidspunkt= " + start + " sluttidspunkt= " + end + giveAway + "adgangskode er " + password;
    }
}
