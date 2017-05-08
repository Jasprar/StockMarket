package StockMarket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by shahs on 08/05/2017.
 */
class ShareTest {

    Share share;


    @Test
    void testSetAndGetSharePrice() {
        double x = 2.2;
        share.setSharePrice(2.2);
        double y = share.getSharePrice();
        assertEquals(x,y);
    }

}

