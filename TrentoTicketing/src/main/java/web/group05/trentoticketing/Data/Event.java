package web.group05.trentoticketing.Data;

import web.group05.trentoticketing.Data.Enums.Event_Location;
import web.group05.trentoticketing.Data.Enums.Event_Type;

import java.sql.Time;
import java.util.Date;

public class Event {

    public int id;
    private String name;
    private Date date;
    private Time time;
    private Event_Type type;
    private Event_Location location;

    public Event(String name, Date date, Time time, Event_Type type, Event_Location location){
        this.name = name;
        this.date = date;
        this.time = time;
        this.type = type;
        this.location = location;
    }

    public Event() {
        this.name = "";
        this.date = new Date(0);
        this.time = new Time(0);
        this.type = Event_Type.NULL;
        this.location = Event_Location.NULL;
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public Date getDate() { return this.date; }
    public Time getTime() { return this.time; }
    public Event_Type getType() { return this.type; }
    public Event_Location getLocation() { return this.location; }

}
