package web.group05.trentoticketing.Data;

import web.group05.trentoticketing.Data.Enums.Event_Location;
import web.group05.trentoticketing.Data.Enums.Event_Type;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
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
    private boolean poltrona_ticket;
    private double poltrona_price;
    private boolean piedi_ticket;
    private double piedi_price;
    private int tickets_sold;
    private int sale;

    public Event(int id, String name, Date date, Time time, Event_Type type, Event_Location location, boolean poltrona_ticket, double poltrona_price, boolean piedi_ticket, double piedi_price, int tickets_sold, int sale) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.type = type;
        this.location = location;
        this.poltrona_ticket = poltrona_ticket;
        this.poltrona_price = poltrona_price;
        this.piedi_ticket = piedi_ticket;
        this.piedi_price = piedi_price;
        this.tickets_sold = tickets_sold;
        this.sale = sale;
    }

    public Event() {
        this.id = 0;
        this.name = "";
        this.date = new Date(0);
        this.time = new Time(0);
        this.type = null;
        this.location = null;
        this.poltrona_ticket = false;
        this.poltrona_price = 0;
        this.piedi_ticket = false;
        this.piedi_price = 0;
        this.tickets_sold = 0;
        this.sale = 0;
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public Date getDate() { return this.date; }
    public Time getTime() { return this.time; }
    public Event_Type getType() { return this.type; }
    public Event_Location getLocation() { return this.location; }
    public double getPoltronaTicket() { return this.poltrona_ticket ? this.poltrona_price : -1; }
    public double getPiediTicket() { return this.piedi_ticket ? this.piedi_price : -1; }
    public int getTickets_sold() { return this.tickets_sold; }
    public int getSale() { return this.sale; }
}
