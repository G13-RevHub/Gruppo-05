package web.group05.trentoticketing.Helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Help {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
