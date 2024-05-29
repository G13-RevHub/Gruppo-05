package web.group05.trentoticketing.Data;

public class Ticket {
    public static enum TicketType {
        POLTRONA,
        IN_PIEDI
    }
    public static String TicketTypeToString(TicketType tt) {
        switch (tt) {
            case POLTRONA: return "Poltrona";
            case IN_PIEDI: return "In Piedi";
            default: return null;
        }
    }

    private float price;
    private TicketType type;

    public Ticket() {
        price = 0;
        type = null;
    }
    public Ticket(float price, TicketType ticketType) {
        this.price = price;
        this.type = ticketType;
    }

    public float getPrice() { return price; }
    public TicketType getType() { return type; }
}
