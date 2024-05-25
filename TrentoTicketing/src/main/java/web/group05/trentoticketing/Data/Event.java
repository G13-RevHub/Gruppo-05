package web.group05.trentoticketing.Data;

import web.group05.trentoticketing.Data.Enums.Event_Location;
import web.group05.trentoticketing.Data.Enums.Event_Type;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Event implements Serializable {
    public static String EventLocationToString(Event_Location el) {
        switch (el) {
            case TRENTO: return "Trento";
            case POVO: return "Povo";
            case ARCO: return "Arco";
            case ROVERETO: return "Rovereto";
            case RIVA_DEL_GARDA: return "Riva del Garda";
            default: return null;
        }
    }
    public static String EventTypeToString(Event_Type et) {
        switch (et) {
            case MUSEO: return "Museo";
            case MOSTRE: return "Mostre";
            case CONCERTO: return "Concerto";
            case VISITA_GUIDATA: return "Visita Guidata";
            case EVENTO_SPORTIVO: return "Evento Sportivo";
            case SPETTACOLO_TEATRALE: return "Spettacolo Teatrale";
            default: return null;
        }
    }

    public int id;
    private String name;
    private Date date;
    private Time time;
    private Event_Type type;
    private Event_Location location;
    private Ticket ticket;
    private int tickets_sold;

    public Event(String name, Date date, Time time, Event_Type type, Event_Location location, Ticket ticket, int tickets_sold){
        this.name = name;
        this.date = date;
        this.time = time;
        this.type = type;
        this.location = location;
        this.ticket = ticket;
        this.tickets_sold = tickets_sold;
    }

    public Event() {
        this.name = "";
        this.date = new Date(0);
        this.time = new Time(0);
        this.type = null;
        this.location = null;
        ticket = null;
        this.tickets_sold = 0;
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public Date getDate() { return this.date; }
    public Time getTime() { return this.time; }
    public Event_Type getType() { return this.type; }
    public Event_Location getLocation() { return this.location; }
    public int getTickets_sold() { return this.tickets_sold; }
}
