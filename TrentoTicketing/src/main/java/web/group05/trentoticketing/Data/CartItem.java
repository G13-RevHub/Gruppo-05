package web.group05.trentoticketing.Data;

public class CartItem {
    public int id;
    public int qty;
    public int type;
    public Event event;

    public CartItem() {
        id = -1;
        qty = 0;
        type = -1;
        event = null;
    }

    public CartItem(int i, int q, int t) {
        id = i;
        qty = q;
        type = t;
        event = null;
    }
    public CartItem(int i, int q, int t, Event e) {
        id = i;
        qty = q;
        type = t;
        event = e;
    }
}
