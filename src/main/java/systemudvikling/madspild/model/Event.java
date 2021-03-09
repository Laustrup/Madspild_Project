package systemudvikling.madspild.model;

public class Event {

    private String type;
    private String address;
    private String description;
    private String start;
    private String end;
    private boolean isGiveAway;

    public Event(String type, String address, String description, String start, String end, boolean isGiveAway) {
        this.type = type;
        this.address = address;
        this.description = description;
        this.start = start;
        this.end = end;
        this.isGiveAway = isGiveAway;
    }
}
