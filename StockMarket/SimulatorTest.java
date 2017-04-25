package StockMarket;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Created by Jasper on 25/04/2017.
 */
@RunWith(Arquillian.class)
public class SimulatorTest {
    Simulator simulator;
    @org.junit.Test
    public void runSimulation() throws Exception {
    }

    @org.junit.Test
    public void getSharePrice() throws Exception {
        for(String companyName : simulator.getCompanyNames()) {
            
        }
    }

    @org.junit.Test
    public void getNetWorth() throws Exception {
    }

    @org.junit.Test
    public void getShareIndex() throws Exception {
    }

    @org.junit.Test
    public void getMarketType() throws Exception {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Simulator.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {
        simulator = new Simulator();    }
}
